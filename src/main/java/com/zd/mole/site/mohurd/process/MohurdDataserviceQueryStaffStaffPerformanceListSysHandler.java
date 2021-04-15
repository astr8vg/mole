package com.zd.mole.site.mohurd.process;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.Ot_achievement_info;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;
import com.zd.mole.utils.RegexUtils;

/**
 * 人员信息\个人工程业绩
 * @author ZhangDi
 *
 */
@Component
@Transactional
public class MohurdDataserviceQueryStaffStaffPerformanceListSysHandler implements ProcessHandler {

	private Log log = LogFactory.getLog(getClass());
	
	private final static String CN_REGEX = "([\u4e00-\u9fa5（）ⅠⅡ、/\\-\\w\\d\\(\\)]+)";
	
	@PersistenceContext 
	private EntityManager em;
	
	@Resource
	private TaskService taskService;
	
	@Override
	public List<Task> handler(Task task, String text) {
		
		String regex = "<tr>" + INNER_HTML + "</tr>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while(m.find()) {
			String tr = m.group(1);
			Ot_achievement_info ai = new Ot_achievement_info();
			ai.setProjectNo(RegexUtils.find("<td data-header=\"项目编码\">(\\d+)</td>", tr));
			ai.setProjectName(RegexUtils.find("<td data-header=\"项目名称\" style=\"text-align:left;\">" + CN_REGEX + "</td>", tr));
			String projectArea2 = RegexUtils.find("<td data-header=\"项目属地\" style=\"text-align:left;\">\" + CN_REGEX + \"</td>", tr);
			ai.setProjectArea(RegexUtils.find("([\u4e00-\u9fa5])-.*", text));
			ai.setProjectArea2(projectArea2);
			ai.setProjectType(RegexUtils.find("<td data-header=\"项目类别\">" + CN_REGEX + "</td>", tr));
			ai.setDevelopmentDw(RegexUtils.find("<td data-header=\"建设单位\" style=\"text-align:left;\">" + CN_REGEX + "</td>", tr));
		}
		
		return null;
	}

}
