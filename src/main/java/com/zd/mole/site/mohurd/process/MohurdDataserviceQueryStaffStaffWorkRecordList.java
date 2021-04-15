package com.zd.mole.site.mohurd.process;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.entity.Task;
import com.zd.mole.utils.RegexUtils;

/**
 * 人员信息\变更记录
 * @author ZhangDi
 *
 */
@Component
@Transactional
public class MohurdDataserviceQueryStaffStaffWorkRecordList implements ProcessHandler {

	@PersistenceContext 
	private EntityManager em;
	
	@Override
	public List<Task> handler(Task task, String text) {
		
		String s = RegexUtils.find(
				"<div  class=\"curQy\"><span><small>当前所在企业: </small>" + CN_REGEX + "</span> <i class=\"fa fa-plus-circle\" title=\"注册记录\"></i> </div>" 
				, text);
		
		String regex = "<li>((?:\\r\\n|.)*?)</li>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(regex);
		while(m.find()) {
			String li = m.group(1);
			RegexUtils.find(
					"<div class=\"cbp_tmlabel\">" 
					+ "\\s*<p>"
					+ "\\s*所在企业由 \"<span>" + CN_REGEX + "</span>\" 变更为 \"<span>[\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、]+</span>" 
					+ "\\s*</p>" 
					+ "\\s*</div>", li);
		}
		return null;
	}

}
