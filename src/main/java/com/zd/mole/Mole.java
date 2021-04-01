package com.zd.mole;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.service.TaskService;

@Component
public class Mole {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 
	
	private int consumerCount = 1;
	
	@Resource
	private TaskService taskService;
	
	private Map<String, ProcessHandler> processHandlers;
	
	public void start() {
		TaskStore store = new TaskStore(1000);
		log.info("任务仓库创建完成");
		TaskProduct product = new TaskProduct(store, taskService);
		Executors.newSingleThreadExecutor().execute(product);
		log.info("生成者创建完成");
		TaskConsumer consumer = new TaskConsumer(store, taskService, processHandlers);
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		for (int i = 0; i < consumerCount; i++) {
			executor.execute(consumer);
		}
		log.info("消费者创建完成");
	}

	public void setProcessHandlers(Map<String, ProcessHandler> processHandlers) {
		this.processHandlers = processHandlers;
	}
}
