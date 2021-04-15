package com.zd.mole.site.mohurd.process;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;
import com.zd.mole.utils.RegexUtils;

/**
 * 人员信息\列表
 * @author ZhangDi
 *
 */
@Component
@Transactional
public class MohurdDataserviceQueryStaffListHandler implements ProcessHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());

	@PersistenceContext 
	private EntityManager em;
	
	@Resource
	private TaskService taskService;

	@Override
	public List<Task> handler(Task task, String text) {
		//获取链接
		String regex = "<tr>\\s*<td data-header=\"序号\">" + INNER_HTML + "</tr>";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while(m.find()) {
			String tr = m.group(1);
			Task newTask = new Task();
			newTask.setStatus(TaskStatus.Ready);
			newTask.setHostUrl("http://jzsc.mohurd.gov.cn"); 
			String code = RegexUtils.find(
					"<td data-header=\"姓名\" class=\"primary\">" + 
					"\\s*<a href=\"/dataservice/query/staff/staffDetail/(\\d+)\" target=\"_blank\">"
					, tr); 
			newTask.setRequestUrl("/dataservice/query/staff/staffDetail/" + code);
			newTask.setCode(code);
			newTask.setMethod(RequestMethod.GET);
			newTask.setProcessHandlerClassName("MohurdDataserviceQueryStaffstaffDetailHandler");
			taskService.save(newTask);
		}
		
		//翻页
		regex ="<script>__pgfm\\('',\\{"
				+ "\"ry_type\":\\[\"\"\\],"
				+ "\"\\$total\":(?<total>\\d+),"
				+ "\"ry_qymc\":\\[\"\"\\],"
				+ "\"\\$reload\":0,"
				+ "\"ry_cardno\":\\[\"(?<rycardno>\\d+)\"\\],"
				+ "\"\\$pg\":(?<pg>\\d+),"
				+ "\"ry_name\":\\[\"\"\\],"
				+ "\"reg_seal_code\":\\[\"\"\\],"
				+ "\"\\$pgsz\":(?<pgsz>\\d+),"
				+ "\"ry_reg_type\":\\[\"\"\\]\\}\\)"
				+ "</script> <a sf='pagebar' sf:data=\""
				+ "\\(\\{pg:\\d+,ps:\\d+,tt:\\d+,pn:\\d+,pc:\\d+,id:'',st:true\\}\\)\"></a>"; 
		p = Pattern.compile(regex);
		m = p.matcher(text);
		if(m.find()) {
			int total = Integer.parseInt(m.group("total"));
			int pgsz = Integer.parseInt(m.group("pgsz"));
			int pg = Integer.parseInt(m.group("pg"));
			String rycardno = m.group("rycardno");
			String param = "ry_type="
					+ "&ry_reg_type="
					+ "&ry_name="
					+ "&reg_seal_code="
					+ "&ry_qymc=&ry_cardno=" + rycardno
					+ "&$reload=0"
					+ "&$total=" + total
					+ "&$pg=" + (pg + 1)
					+ "&$pgsz=" + pgsz;
			if(pg + 1 > 30) {
				//如果超过30页
				log.warn("超过30页{}", task.getRequestUrl() + " " + task.getParam());
			} else if (pg * pgsz < total) {
				Task nextPageTask = new Task();
				nextPageTask.setParam(param);
				nextPageTask.setMethod(RequestMethod.POST);
				nextPageTask.setHostUrl(task.getHostUrl());
				nextPageTask.setRequestUrl(task.getRequestUrl());
				nextPageTask.setStatus(TaskStatus.Ready);
				nextPageTask.setProcessHandlerClassName(getClass().getSimpleName());
				taskService.save(nextPageTask);
			}
		}
		
		return null;
	}

}
