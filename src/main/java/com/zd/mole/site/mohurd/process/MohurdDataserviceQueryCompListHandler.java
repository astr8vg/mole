package com.zd.mole.site.mohurd.process;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.entity.Task;

@Component
public class MohurdDataserviceQueryCompListHandler implements ProcessHandler {

	private Log log = LogFactory.getLog(getClass());
	
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
			String requestUrl = m.group(1);
			newTask.setRequestUrl(requestUrl);
			String pk = requestUrl.replaceFirst("/dataservice/query/comp/compDetail/", "");
			newTask.setProcessHandlerClassName(MohurdDataserviceQueryCompCompDetailHandler.class.getSimpleName());
			newTasks.add(newTask);
			//设置主键
			newTask.setCode(pk);
			
			log.debug(m.group(1));
		}
		
		return newTasks;
	}
	
}
