package com.zd.mole;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;

public class TaskProduct implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private TaskStore store;
	
	private TaskService service;
	
	/** 单次从数据库中获取的数据量 */
	private int readyCount = 100;
	
	public TaskProduct(TaskStore store, TaskService service) {
		this.store = store;
		this.service = service;
	}
	
	@Override
	public void run() {
		int resetCount = service.reset();
		log.info("重置任务状态{}个", resetCount);
		while(true) {
			List<Task> tasks = service.findByStatus(TaskStatus.Ready, readyCount);
			for (Task task : tasks) {
				task.setStatus(TaskStatus.ReadyToDispose);
				service.update(task);
				store.put(task);
			}
			if(tasks.size() == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
