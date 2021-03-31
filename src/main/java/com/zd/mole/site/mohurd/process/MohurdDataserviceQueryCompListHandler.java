package com.zd.mole.site.mohurd.process;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.Task;

public class MohurdDataserviceQueryCompListHandler implements ProcessHandler {

	/*
	 * (non-Javadoc)
	 * @see com.zd.mole.process.ProcessDataHandler#trim(java.lang.String)
	 */
	@Override
	public String trim(String data) {
		return data;
	}

	@Override
	public List<Task> handler(Task task, String data) {
		String regex = "<td class=\"text-left primary\" data-header=\"企业名称\">"
				+"\\s*<a target=\"_blank\" href=\"([/\\w\\d]+)\">";

		List<Task> newTasks = new LinkedList<>();

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		
		while(m.find()) {
			Task newTask = new Task();
			String requestURL = m.group(1);
			newTask.setRequestURL(requestURL);
			newTask.setFileURL(requestURL.replaceAll("/", "\\\\") + "\\");
			String pk = requestURL.replaceFirst("/dataservice/query/comp/compDetail/", "");
			newTask.setFileName(pk);
			newTask.setProcessHandlerClassName(MohurdDataserviceQueryCompCompDetailHandler.class.getName());
			newTasks.add(newTask);
			//设置主键
			newTask.setCode(pk);
			System.out.println(m.group(1));
		}
		
		return newTasks;
	}
	
}
