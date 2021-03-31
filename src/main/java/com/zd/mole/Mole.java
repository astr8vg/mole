package com.zd.mole;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.service.TaskService;

@Component
public class Mole {
	
	private Log log = LogFactory.getLog(getClass()); 
	
	private int consumerCount = 100;
	
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
