package com.zd.mole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.mole.net.proxy.ProxyHttpXMLRequest;
import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;

public class TaskConsumer implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private TaskStore store;

	private TaskService service;
	
	private Map<String, ProcessHandler> processHandlers;

	public TaskConsumer(TaskStore store, TaskService service, Map<String, ProcessHandler> processHandlers) {
		this.store = store;
		this.service = service;
		this.processHandlers = processHandlers;
	}

	@Override
	public void run() {
		while(true) {
			Task task = store.get();
			String httpUrl = task.getHostUrl() + task.getRequestUrl();
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept-Encoding", "gzip");
			try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
				if (task.getMethod() == RequestMethod.POST) {
					ProxyHttpXMLRequest.sendPost(httpUrl, headers, task.getParam(), out);
				} else {
					ProxyHttpXMLRequest.sendGet(httpUrl, headers, out);
				}
				String text = out.toString("utf-8");
				ProcessHandler ph = processHandlers.get(task.getProcessHandlerClassName());
				if(ph == null) {
					log.error("ProcessHandler：" + task.getProcessHandlerClassName()+ " 获取失败");
				}
				ph.handler(task, text);
				
				task.setStatus(TaskStatus.Succeed);
				service.update(task);
			} catch (IOException e) {
				log.error("保存失败[id:{}] {}", task.getId(), e.getMessage());
				task.setStatus(TaskStatus.Ready);
				service.update(task);
			}
		}
	}
}
