package com.zd.mole.site.mohurd.process;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.Ot_company_info;
import com.zd.mole.site.mohurd.entity.Sys_dict;
import com.zd.mole.task.entity.Task;
import com.zd.mole.utils.RegexUtils;

@Component
@Transactional
public class MohurdDataserviceQueryCompCompDetailHandler implements ProcessHandler {
	
	private Log log = LogFactory.getLog(getClass());

	@PersistenceContext 
	private EntityManager em;
	
	@Override
	public String trim(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> handler(Task task, String data) {

		Ot_company_info ci = new Ot_company_info();
		
		ci.setId(task.getCode());
		ci.setCreate_by("1");
		ci.setUpdate_by("1");
		ci.setCreate_date(new Date());
		ci.setUpdate_date(new Date());
		ci.setDel_flag("0");
		//企业名称
		String regex = "<div class=\"user_info spmtop\">"
				+ "\\s*<b><i class=\"fa fa-building-o\"></i>\\s*" + CN_REGEX
				+ "(<span class='qy_src_type'>造价企业</span>)?</b>"
				+ "\\s*</div>";
		String companyname = RegexUtils.find(regex, data);
		log.debug("企业名称：" + companyname);
		ci.setCompanyname(companyname);
		
		String regex1 = "<tr>"
				+ "\\s*<th>统一社会信用代码</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"统一社会信用代码\">([\\d\\w]+)</td>"
				+ "\\s*</tr>";
		String companycode = RegexUtils.find(regex1, data);
		log.debug("统一社会信用代码：" + companycode);
		ci.setCompanycode(companycode);
		
		String regex11 = "<tr>"
				+ "\\s*<th>&nbsp;&nbsp;组织机构代码/<br/>营业执照编号</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"组织机构代码/营业执照编号\">([\u4e00-\u9fa5\\w\\d-&;/\\(\\)\\（\\）\\s]+)</td>"
				+ "\\s*</tr>";
		String companyOrgCode = RegexUtils.find(regex11, data);
		log.debug("组织机构代码号/营业执照编号：" + companyOrgCode);
		ci.setCompanyOrgCode(companyOrgCode);
		
		String regex2 = "<tr>"
				+ "\\s*<th>企业法定代表人</th>"
				+ "\\s*<td data-header=\"企业法定代表人\">([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\·]+)</td>"
				+ "\\s*<th>企业登记注册类型</th>"
				+ "\\s*<td data-header=\"企业登记注册类型\">";
		String companyer = RegexUtils.find(regex2, data);
		ci.setCompanyer(companyer);
		log.debug("企业法定代表人：" + companyer);
		
		String regex3 = "<th>企业登记注册类型</th>"
				+ "\\s*<td data-header=\"企业登记注册类型\">([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、]+)</td>"
				+ "\\s*</tr>";
		String companytype = RegexUtils.find(regex3, data);
		List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = 'oLife_ComRegisterType' and label = :label")
			.setParameter("label", companytype)
			.getResultList();
		String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
		companytype = Optional.ofNullable(value).orElse(companytype);
		ci.setCompanytype(companytype);
		log.debug("企业登记注册类型：" + companytype);

		String regex4 = "<tr>"
				+ "\\s*<th>企业注册属地</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"企业注册属地\">([\u4e00-\u9fa5]+)</td>"
				+ "\\s*</tr>";
		String companyarea = RegexUtils.find(regex4, data);
		List<Sys_dict> dicts4 = em.createQuery("from Sys_dict where type = 'oLife_ComAreaScope' and label = :label")
			.setParameter("label", companyarea)
			.getResultList();
		String value4 = dicts4.stream().findFirst().orElse(new Sys_dict()).getValue();
		companyarea = Optional.ofNullable(value4).orElse(companyarea);
		ci.setCompanyarea(companyarea);
		log.debug("企业注册属地：" + companyarea);
		
		String regex5 = "<tr>"
				+ "\\s*<th>企业经营地址</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"企业经营地址\">(.+)</td>"
				+ "\\s*</tr>";
		String companyaddress = RegexUtils.find(regex5, data);
		ci.setCompanyaddress(companyaddress);
		log.debug("企业经营地址：" + companyaddress);
		
		//TODO jdbc 异常捕获
		em.merge(ci);

		List<Task> newTasks = new LinkedList<>();
		String regex10 = "<li class=\"activeTinyTab\"><A id=\"apt_tab\" data-contentid=\"apt_tabcontent\" "
				+ "data-url=\"(.+)\" href=\"javascript:void\\(0\\)\"><span>企业资质资格</span></A><i>/</i></li>";
		Pattern p10 = Pattern.compile(regex10);
		Matcher m10 = p10.matcher(data);
		if( m10.find() ) {
			Task newTask = new Task();
			newTask.setCode(task.getCode());
			String requestUrl = m10.group(1);
			newTask.setRequestUrl(requestUrl);
			
			newTask.setProcessHandlerClassName(MohurdDataserviceQueryCompCaDetailListHandler.class.getSimpleName());
			
			newTasks.add(newTask);
			log.debug("新增任务请求地址：" + requestUrl);
		}
		
//		return newTasks;
		return null;
	}
	
}
