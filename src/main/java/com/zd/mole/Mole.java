package com.zd.mole;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zd.mole.net.proxy.ProxyHttpXMLRequest;
import com.zd.mole.net.proxy.ProxyManager;
import com.zd.mole.sys.monitor.entity.Monitor;
import com.zd.mole.sys.monitor.repository.MonitorRepository;

@Component
public class Mole {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 
	
	@Value("${mole.consumer.maxcount}")
	private int maxConsumer = 150;

	@Value("${mole.consumer.count}")
	private int consumerCount = 100;
	
	@Resource
	private TaskProduct product;
	
	@Resource
	private TaskConsumer consumer;
	
	@Resource
	private MonitorRepository monitorRepository;
	
	public void start() {
		//网络带宽 10M
		int moleNetworkBandwidth = 1024 * 500;
		//目标服务器的带宽
		int targetNetworkBandwidth = 128 * 1024 * 10;
		//设置快速代理保持数量
		ProxyManager.getInstance().setFastProxyCount(consumerCount);
		TaskStore store = new TaskStore(consumerCount * 10);
		log.info("任务仓库创建完成");
		product.setStore(store);
		Executors.newSingleThreadExecutor().execute(product);
		log.info("生成者创建完成");
		consumer.setStore(store);
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		for (int i = 0; i < consumerCount; i++) {
			executor.execute(consumer);
		}
		log.info("消费者创建完成");
		
		Thread t = new Thread(() -> {
			int interval = 5000;
			while(true) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Monitor monitor = new Monitor();
				monitor.setParamName("bps");
				long bps = ProxyHttpXMLRequest.getBps();
				monitor.setParamValue(String.valueOf(bps));
				try {
					monitorRepository.save(monitor);
					if(bps < moleNetworkBandwidth && consumerCount < maxConsumer) {
						executor.execute(consumer);
						consumerCount++;
						log.info("\"监控线程：增加工作线程到{}个", consumerCount);
						
						Monitor monitor2 = new Monitor();
						monitor2.setParamName("TaskConsumerThread");
						monitor2.setParamValue(String.valueOf(consumerCount));
						monitorRepository.save(monitor);
					}
				} catch (Exception e) {
					log.error("监控线程：{} ", e.getMessage());
				}
			}
		});
		t.setDaemon(true);
		t.start();
		log.info("监控创建完成");
	}
}
