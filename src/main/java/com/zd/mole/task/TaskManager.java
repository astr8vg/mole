package com.zd.mole.task;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskManager {
	
	private static TaskManager tm = new TaskManager();
	
	private Queue<Task> readyToDownloadCache = new ConcurrentLinkedQueue<>();
	private Queue<Task> readyToParseCache = new ConcurrentLinkedQueue<>();
	
	private static int readyToDownloadCacheSize = 100;
	private static int readyToParseCacheSize = 100;
	
	private AtomicBoolean allFinish = new AtomicBoolean(false);
	
	private TaskService service = TaskService.newInstance();
	
	private TaskManager() {
		
		//更新所有下载中任务到准备、下载完成状态
		reset();
		
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					//获取数据库中的数据
					if(readyToDownloadCache.size() < readyToDownloadCacheSize) {
						List<Task> tasks = service.findByStatus(TaskStatus.Ready, readyToDownloadCacheSize);
						tasks.forEach(task -> {
							task.setStatus(TaskStatus.ReadyToDownload);
							service.update(task);
						});
						readyToDownloadCache.addAll(tasks);
					}
					if(readyToParseCache.size() < readyToParseCacheSize) {
						List<Task> tasks = service.findByStatus(TaskStatus.Downloaded, readyToDownloadCacheSize);
						tasks.forEach(task -> {
							task.setStatus(TaskStatus.ReadyToParse);
							service.update(task);
						});
						readyToParseCache.addAll(tasks);
					}
					if(readyToDownloadCache.size() == 0 && readyToParseCache.size() == 0 && service.isAllStop()) {
						allFinish.set(true);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		};
		
		Thread t = new Thread(r);
		t.setDaemon(true);
		t.start();
	}

	public static TaskManager newInstance() {
		return tm;
	}
	
	/**
	 * 重置任务状态
	 */
	private void reset() {
		service.reset();
	}

	/**
	 * 获取准备下载任务
	 * @return
	 */
	public Task getReadyToDownload() {
		return readyToDownloadCache.poll();
	}
	
	/**
	 * 获取准备解析任务
	 * @return
	 */
	public Task getReadyToParse() {
		return readyToParseCache.poll();
	}
	
	/**
	 * 是否已完成所有任务
	 * @return
	 */
	public boolean isAllFinsh() {
		return allFinish.get();
	}
	
	/**
	 * 创建任务、重复数据不会被创建
	 * @param task
	 */
	public void newTask(Task task) {
		task.setStatus(TaskStatus.Ready);
		service.save(task);
	}
	/**
	 * 更新任务状态
	 * @param task
	 */
	public void update(Task task) {
		service.update(task);
	}
	
	public void close() {
		service.close();
	}
}
