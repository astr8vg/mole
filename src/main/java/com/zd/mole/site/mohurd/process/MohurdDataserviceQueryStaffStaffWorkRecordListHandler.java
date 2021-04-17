package com.zd.mole.site.mohurd.process;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.Ot_update_info_log;
import com.zd.mole.site.mohurd.service.SysDictService;
import com.zd.mole.task.entity.Task;
import com.zd.mole.utils.RegexUtils;

/**
 * 人员信息\变更记录
 * @author ZhangDi
 *
 */
@Component
@Transactional
public class MohurdDataserviceQueryStaffStaffWorkRecordListHandler implements ProcessHandler {

	private final static String CN_REGEX_NAME = "([\u4e00-\u9fa5（）ⅠⅡ、/\\-\\w\\d\\(\\)\\[\\]]+)";
	
	@PersistenceContext 
	private EntityManager em;
	
	@Resource
	private SysDictService sysDictService;
	
	@Override
	public List<Task> handler(Task task, String text) {
		
		String tbody = RegexUtils.find("<tbody>" + INNER_HTML + "</tbody>", text);
		Pattern p = Pattern.compile("<tr>" + INNER_HTML + "</tr>");
		Matcher m = p.matcher(tbody);
		int index = 0;
		while(m.find()) {
			String tr = m.group(1);
			String registerType = RegexUtils.find("<td data-header=\"注册类别\">" + CN_REGEX_NAME + "</td>", tr);
			registerType = sysDictService.findOrSaveByTypeLabel("oLife_PerRegisterType", registerType, "注册类别");
			
			Pattern p2 = Pattern.compile("<div class=\"cbp_tmlabel\">\\s*<p>\\s*" + INNER_HTML + "\\s*</p>");
			Matcher m2 = p2.matcher(tr);
			while(m2.find()) {
				String id = task.getCode() + index;
				if(index == 0) {
					id = task.getCode();
				}
				String pTag = m2.group(1);
				String oldInfo = RegexUtils.find("所在企业由 \"<span>" + ProcessHandler.CN_REGEX_NO_S + "</span>\" 变更为", pTag);
				String newInfo = RegexUtils.find("\" 变更为 \"<span>" +ProcessHandler.CN_REGEX_NO_S + "</span>\"", pTag);
				Ot_update_info_log ot_update_info_log = new Ot_update_info_log();
				ot_update_info_log.setId(id);
				ot_update_info_log.setUpdateEntity(task.getCode());
				ot_update_info_log.setUpdateInfo(pTag);
				ot_update_info_log.setUpdateType("2");
				ot_update_info_log.setCreate_by("1");
				ot_update_info_log.setCreate_date(new Date());
				ot_update_info_log.setUpdate_by("1");
				ot_update_info_log.setUpdate_date(new Date());
				ot_update_info_log.setDel_flag("0");
				ot_update_info_log.setRegisterType(registerType);
				ot_update_info_log.setOldInfo(oldInfo);
				ot_update_info_log.setNewInfo(newInfo);
				em.merge(ot_update_info_log);
				index++;
			}
			
		}
		
		return null;
	}

}
