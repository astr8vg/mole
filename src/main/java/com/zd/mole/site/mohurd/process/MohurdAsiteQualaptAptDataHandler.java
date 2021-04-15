package com.zd.mole.site.mohurd.process;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.MonhurdCompListApt;
import com.zd.mole.task.entity.Task;

@Component
@Transactional
public class MohurdAsiteQualaptAptDataHandler implements ProcessHandler {

	private Log log = LogFactory.getLog(getClass());
	
	private final String CN_REGEX = "([\u4e00-\u9fa5\\（\\）\\Ⅰ\\Ⅱ\\、/\\-\\w\\d\\(\\)]+)";
	
	@PersistenceContext 
	private EntityManager em;
	
	@Override
	public List<Task> handler(Task task, String text) {
		String regex = "<tr class=\"data_row\">" 
					+ "\\s*<td><input class=\"icheck\" type=\"checkbox\" value='\\{\"apt_code\":\"([\\w\\d]+\\s*)\", \"apt_scope\":\"" + CN_REGEX + "\"\\}'></td>"
					+ "\\s*<td style=\"text-align: left;\">" + CN_REGEX
		            + "\\s*</td>"
		            + "\\s*</tr>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		int count = 0;
		while (m.find()) {
			MonhurdCompListApt apt = new MonhurdCompListApt();
			apt.setApt_code(m.group(1));
			apt.setApt_scope(m.group(2));
			log.debug("apt_code: " + m.group(1) + " apt_scope: " + m.group(2));
			em.merge(apt);
			count++;
		}
		if(count < 10) {
			log.warn("任务: " + task.getId() + " 请求路径" + task.getRequestUrl() + "未能采集预定量数据（预期10，实际" + count + "）,");
		}
		return null;
	}

}
