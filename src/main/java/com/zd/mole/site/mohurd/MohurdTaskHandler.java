package com.zd.mole.site.mohurd;

import java.util.ArrayList;
import java.util.List;

import com.zd.mole.site.mohurd.process.MohurdDataserviceQueryCompListHandler;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.Task;
import com.zd.mole.task.TaskHandler;
import com.zd.mole.task.TaskStatus;

public class MohurdTaskHandler implements TaskHandler {

	@Override
	public List<Task> init() {
		//获取所有企业列表
		int total = 15;
//		int total = 268689;
		int pgsz = 15;
		int pgTotal = total / pgsz + ( total % pgsz == 0 ? 0 : 1 );
		List<Task> tasks = new ArrayList<>(pgTotal);
		for (int pg = 1; pg <= pgTotal; pg++) {
			Task task = new Task();
			task.setStatus(TaskStatus.ReadyToDownload);
			task.setHostURL("http://jzsc.mohurd.gov.cn");
			task.setRequestURL("/dataservice/query/comp/list");
			task.setParam("$reload=0&$total="+ total + "&$pg=" + pg + "&$pgsz=" + pgsz);
			task.setMethod(RequestMethod.POST);
	
			task.setFolderURI("d:\\jzsc.mohurd.gov.cn");
			task.setFileURL("\\dataservice\\query\\comp\\list\\" + String.valueOf(pg));
			task.setFileName(String.valueOf(pg));
			task.setProcessHandlerClassName(MohurdDataserviceQueryCompListHandler.class.getName());
			tasks.add(task);
		}
		return tasks;
	}

}
