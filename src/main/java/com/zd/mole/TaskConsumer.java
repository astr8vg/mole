package com.zd.mole;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.net.HttpXMLRequest;
import com.zd.mole.net.proxy.ProxyHttpXMLRequest;
import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;

@Component
public class TaskConsumer implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private TaskStore store;

	@Resource
	private TaskService taskService;
	
	private Map<String, ProcessHandler> processHandlers;
	
	@Override
	public void run() {
		while(true) {
			Task task = store.get();
			String httpUrl = task.getHostUrl() + task.getRequestUrl();
			Map<String, String> headers = new HashMap<>();
			headers.put("Accept-Encoding", "gzip");
			try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
				if (task.getMethod() == RequestMethod.POST) {
//					HttpXMLRequest.sendPost(httpUrl, headers, null, task.getParam(), out);
					ProxyHttpXMLRequest.sendPost(httpUrl, headers, task.getParam(), out);
				} else {
//					HttpXMLRequest.sendGet(httpUrl, headers, null, out);
					ProxyHttpXMLRequest.sendGet(httpUrl, headers, out);
				}
				String text = out.toString("utf-8");
				ProcessHandler ph = processHandlers.get(task.getProcessHandlerClassName());
				if(ph == null) {
					log.error("ProcessHandler：" + task.getProcessHandlerClassName()+ " 获取失败");
				}
				ph.handler(task, text);
				taskService.update(task.getId(), TaskStatus.Succeed);
			} catch (IOException e) {
				log.error("保存失败IO[id:{}] {}", task.getId(), e.getMessage());
				taskService.update(task.getId(), TaskStatus.Ready);
			} catch (Exception e) {
				log.error("保存失败[id:{}] {}", task.getId(), e.getMessage());
				e.printStackTrace();
				taskService.update(task.getId(), TaskStatus.Failed);
			}
		}
	}

	public void setStore(TaskStore store) {
		this.store = store;
	}

	public void setProcessHandlers(Map<String, ProcessHandler> processHandlers) {
		this.processHandlers = processHandlers;
	}
}
