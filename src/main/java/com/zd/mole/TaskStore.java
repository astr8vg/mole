package com.zd.mole;

import java.util.LinkedList;

import com.zd.mole.task.entity.Task;

public class TaskStore {
	
	private LinkedList<Task> store = new LinkedList<>();
	
	/** 仓库大小 */
	private int storeSize = 1000;
	
	public TaskStore(int storeSize) {
		this.storeSize = storeSize;
	}
	
	public void put(Task task) {
		synchronized (store) {
			while(store.size() > storeSize) {
				try {
					store.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			store.push(task);
			store.notifyAll();
		}
	}
	
	public Task get() {
		Task task = null;
		synchronized (store) {
			while((task = store.poll()) == null) {
				store.notifyAll();
				try {
					store.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return task;
	}

	public void setStore(LinkedList<Task> store) {
		this.store = store;
	}
}
