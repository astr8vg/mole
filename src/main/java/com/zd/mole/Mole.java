package com.zd.mole;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.net.proxy.ProxyHttpXMLRequest;
import com.zd.mole.net.proxy.ProxyManager;
import com.zd.mole.sys.monitor.entity.Monitor;
import com.zd.mole.sys.monitor.repository.MonitorRepository;

@Component
public class Mole {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 
	
	private int consumerCount = 20;
	
	@Resource
	private TaskProduct product;
	
	@Resource
	private TaskConsumer consumer;
	
	@Resource
	private MonitorRepository monitorRepository;
	
	public void start() {
		//网络带宽 10M
		int moleNetworkBandwidth = 128 * 1024 * 10;
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
			int interval = 1000; 
			Monitor monitor = monitorRepository.findById((short) 1).orElse(new Monitor());
			if(monitor.getId() == null)
				monitorRepository.save(monitor);
			while(true) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				monitor.setBs(ProxyHttpXMLRequest.getBs());
				monitorRepository.save(monitor);
			}
		});
		t.setDaemon(true);
		t.start();
		log.info("监控创建完成");
	}
}
