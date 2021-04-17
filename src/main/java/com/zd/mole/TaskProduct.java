package com.zd.mole;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;

@Component
public class TaskProduct implements Runnable {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	private TaskStore store;
	
	/** 单次从数据库中获取的数据量 */
	private int readyCount = 100;
	
	@Resource
	private TaskService taskService;
	
	@Override
	public void run() {
		int resetCount = taskService.reset();
		log.info("重置任务状态{}个", resetCount);
		readyCount = store.getStoreSize();
		if(readyCount > 200) {
			readyCount = 200;
		}
		while(true) {
			List<Task> tasks = taskService.findByStatus(TaskStatus.Ready, readyCount);
			for (Task task : tasks) {
				taskService.update(task.getId(), TaskStatus.InTheQueue);
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

	public void setStore(TaskStore store) {
		this.store = store;
	}

}
