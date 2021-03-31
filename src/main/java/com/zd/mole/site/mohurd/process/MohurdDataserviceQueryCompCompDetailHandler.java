package com.zd.mole.site.mohurd.process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zd.mole.App;
import com.zd.mole.db.ThirdService;
import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.Ot_company_info;
import com.zd.mole.site.mohurd.entity.Sys_dict;
import com.zd.mole.task.Task;

public class MohurdDataserviceQueryCompCompDetailHandler implements ProcessHandler {
	
	private Log log = LogFactory.getLog(App.class);

	@Override
	public String trim(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> handler(Task task, String data) {

		Ot_company_info ci = new Ot_company_info();
		
		//企业名称
		String regex = "<div class=\"user_info spmtop\">"
				+ "\\s*<b><i class=\"fa fa-building-o\"></i>\\s*([\u4e00-\u9fa5]+)</b>"
				+ "\\s*</div>";
				
		String regex1 = "<tr>"
				+ "\\s*<th>统一社会信用代码</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"统一社会信用代码\">([\\d\\w]+)</td>"
				+ "\\s*</tr>";
		
		String regex11 = "<tr>"
				+ "\\s*<th>&nbsp;&nbsp;组织机构代码/<br/>营业执照编号</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"组织机构代码/营业执照编号\">([\\w\\d-&;/]+)</td>"
				+ "\\s*</tr>";

		String regex2 = "<tr>"
				+ "\\s*<th>企业法定代表人</th>"
				+ "\\s*<td data-header=\"企业法定代表人\">([\u4e00-\u9fa5]+)</td>"
				+ "\\s*<th>企业登记注册类型</th>"
				+ "\\s*<td data-header=\"企业登记注册类型\">[\u4e00-\u9fa5]+</td>"
				+ "\\s*</tr>";
		
		String regex3 = "<tr>"
				+ "\\s*<th>企业法定代表人</th>"
				+ "\\s*<td data-header=\"企业法定代表人\">[\u4e00-\u9fa5]+</td>"
				+ "\\s*<th>企业登记注册类型</th>"
				+ "\\s*<td data-header=\"企业登记注册类型\">([\u4e00-\u9fa5]+)</td>"
				+ "\\s*</tr>";

		String regex4 = "<tr>"
				+ "\\s*<th>企业注册属地</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"企业注册属地\">([\u4e00-\u9fa5]+)</td>"
				+ "\\s*</tr>";
		
		String regex5 = "<tr>"
				+ "\\s*<th>企业经营地址</th>"
				+ "\\s*<td colspan=\"3\" data-header=\"企业经营地址\">([\u4e00-\u9fa5\\d]+)</td>"
				+ "\\s*</tr>";
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		if( m.find() ) {
			String companyname = m.group(1);
			log.debug("企业名称：" + companyname);
			ci.setCompanyname(m.group(1));
		}
		
		Pattern p1 = Pattern.compile(regex1);
		Matcher m1 = p1.matcher(data);
		if( m1.find() ) {
			String companycode = m1.group(1);
			log.debug("统一社会信用代码：" + companycode);
			ci.setCompanycode(companycode);
			
		}
		
		Pattern p11 = Pattern.compile(regex11);
		Matcher m11 = p11.matcher(data);
		if( m11.find() ) {
			String companyOrgCode = m11.group(1);
			log.debug("组织机构代码号：" + companyOrgCode);
			ci.setCompanyOrgCode(companyOrgCode);
		}
		
		Pattern p2 = Pattern.compile(regex2);
		Matcher m2 = p2.matcher(data);
		if( m2.find() ) {
			String companyer = m2.group(1);
			ci.setCompanyer(companyer);
			log.debug("企业法定代表人：" + companyer);
		}
		
		Pattern p3 = Pattern.compile(regex3);
		Matcher m3 = p3.matcher(data);
		if( m3.find() ) {
			String companytype = m3.group(1);
			ci.setCompanytype(companytype);
			
			EntityManager em = ThirdService.newInstance().em();
			List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = 'oLife_ComRegisterType' and label = :label")
				.setParameter("label", companytype)
				.getResultList();
			String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
			companytype = Optional.ofNullable(value).orElse(companytype);
			em.close();
			
			log.debug("企业登记注册类型：" + companytype);
		}
		
		Pattern p4 = Pattern.compile(regex4);
		Matcher m4 = p4.matcher(data);
		if( m4.find() ) {
			String companyarea = m4.group(1);
			ci.setCompanyarea(companyarea);
			EntityManager em = ThirdService.newInstance().em();
			List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = 'oLife_ComAreaScope' and label = :label")
				.setParameter("label", companyarea)
				.getResultList();
			String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
			companyarea = Optional.ofNullable(value).orElse(companyarea);
			em.close();
			
			ci.setCompanytype(companyarea);
			log.debug("企业注册属地：" + companyarea);
		}
		
		Pattern p5 = Pattern.compile(regex5);
		Matcher m5 = p5.matcher(data);
		if( m5.find() ) {
			String companyaddress = m5.group(1);
			ci.setCompanyaddress(companyaddress);
			System.out.println(m5.group(1));
			log.debug("企业经营地址：" + companyaddress);
			
		}
		
		ThirdService.newInstance().saveOrUpdate(ci);
		

		List<Task> newTasks = new LinkedList<>();
		String regex10 = "<li class=\"activeTinyTab\"><A id=\"apt_tab\" data-contentid=\"apt_tabcontent\" "
				+ "data-url=\"(.+)\" href=\"javascript:void\\(0\\)\"><span>企业资质资格</span></A><i>/</i></li>";
		Pattern p10 = Pattern.compile(regex10);
		Matcher m10 = p10.matcher(data);
		if( m10.find() ) {
			Task newTask = new Task();
			String requestURL = m10.group(1);
			newTask.setRequestURL(requestURL);
			
			newTask.setFileURL(requestURL.replaceAll("/", "\\\\") + "\\");
			String fileName = requestURL.replaceFirst("/dataservice/query/comp/caDetailList/", "");
			newTask.setFileName(fileName);
			newTask.setProcessHandlerClassName(MohurdDataserviceQueryCompCaDetailListHandler.class.getName());
			
			newTasks.add(newTask);
			System.out.println(m10.group(1));
		}
		
		
		return newTasks;
	}
	
	public static void main(String[] args) {
		String companytype = "有限责任公司";
		EntityManager em = ThirdService.newInstance().em();
		List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = 'oLife_ComRegisterType' and label = :label")
			.setParameter("label", companytype)
			.getResultList();
		String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
		companytype = Optional.ofNullable(value).orElse(companytype);
		em.close();
		System.out.println(companytype);
		ThirdService.newInstance().close();
	}

}
