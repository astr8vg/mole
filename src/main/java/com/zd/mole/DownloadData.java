package com.zd.mole;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.zd.mole.net.HttpXMLRequest;
import com.zd.mole.net.ProxyManager;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.Task;
import com.zd.mole.task.TaskManager;
import com.zd.mole.task.TaskStatus;

public class DownloadData implements Runnable {
	
	private Task task;
	
	public DownloadData(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		task.setStatus(TaskStatus.Downloading);
		TaskManager.newInstance().update(task);
		//获取代理
		ProxyManager m = ProxyManager.newInstance();
		//设置流输出位置
		String outpath = task.getFolderURI()
				+ task.getFileURL();
		File file = new File(outpath);
		file.getParentFile().mkdirs();
		try (OutputStream out = new FileOutputStream(file)){
			String url = task.getHostURL() + task.getRequestURL();
			if (task.getMethod() == RequestMethod.POST) {
				HttpXMLRequest.sendPost(url, m.get(), task.getParam(), out);
			} else {
				HttpXMLRequest.sendGet(url, m.get(), out);
			}
			task.setStatus(TaskStatus.Downloaded);
			TaskManager.newInstance().update(task);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			task.setStatus(TaskStatus.DownloadFaild);
			TaskManager.newInstance().update(task);
			e1.printStackTrace();
		}
	}

}
