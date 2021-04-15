package com.zd.mole.site.mohurd.process;

import java.util.Date;
import java.util.List;
import java.util.Optional;
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
import com.zd.mole.site.mohurd.entity.Ot_person_info;
import com.zd.mole.site.mohurd.entity.Ot_practice_info;
import com.zd.mole.site.mohurd.entity.Ot_register_info;
import com.zd.mole.site.mohurd.entity.Sys_dict;
import com.zd.mole.site.mohurd.repository.OtCompanyInfoRepository;
import com.zd.mole.site.mohurd.service.SysDictService;
import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;
import com.zd.mole.utils.RegexUtils;

/**
 * 人员信息\执业注册信息
 * @author ZhangDi
 *
 */
@Component
@Transactional
public class MohurdDataserviceQueryStaffstaffDetailHandler implements ProcessHandler {

	private Log log = LogFactory.getLog(getClass());
	
	private final static String CN_REGEX = "([\u4e00-\u9fa5（）ⅠⅡ、/\\-\\w\\d\\(\\)]+)";
	
	@PersistenceContext 
	private EntityManager em;
	
	@Resource
	private TaskService taskService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private OtCompanyInfoRepository otCompanyInfoRepository;
	
	@Override
	public List<Task> handler(Task task, String text) {
		
		//基本信息
		Ot_person_info person_info = new Ot_person_info();
		person_info.setId(task.getCode());

		String cnname = RegexUtils.find("<div class=\"user_info spmtop\">"
				+ "\\s*<b><i class=\"fa fa-user\"></i> " + CN_REGEX + "<!-- <span>\\d{10}\\*{6}\\d[\\d\\w]</span> --></b>"
				+ "\\s*</div>", text);
		person_info.setName(cnname);
		
		String sex = RegexUtils.find("<dd class=\"query_info_dd1\"><span>性别：</span>" + CN_REGEX + "</dd>", text);
		List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = 'oLife_SexType' and label = :label")
				.setParameter("label", sex)
				.getResultList();
		String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
		sex = Optional.ofNullable(value).orElse(sex);
		person_info.setSex(sex);
		
		String custType = RegexUtils.find("<dd class=\"query_info_dd2\"><span>证件类型：</span>" + CN_REGEX + "</dd>", text);
		dicts = em.createQuery("from Sys_dict where type = 'oLife_Custtype' and label = :label")
				.setParameter("label", custType)
				.getResultList();
		value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
		custType = Optional.ofNullable(value).orElse(sex);
		person_info.setCustType(custType);
	
		String custId = RegexUtils.find("<dd class=\"query_info_dd2\"><span>证件号码：</span>(\\d{10}\\*{6}\\d[\\d\\w])</dd>", text);
		person_info.setCustId(custId);
		person_info.setCreate_by("1");
		person_info.setCreate_date(new Date());
		person_info.setUpdate_by("1");
		person_info.setUpdate_date(new Date());
		person_info.setDel_flag("0");
		em.merge(person_info);
		
		//执业注册信息
		String div = RegexUtils.find("<div class=\"activeTinyTabContent\" id=\"regcert_tab\">" + INNER_HTML + "</div>\\s*<div id=\"iframe_tab\">", text);
		
		String regex = "<dl>" + INNER_HTML + "</dl>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(div);
		int i = 0;
		while(m.find()) {
			String id = person_info.getId() + i;
			if(i == 0) {
				id =  person_info.getId();
			}
			String dl = m.group(1);
			Ot_practice_info ot_practice_info = new Ot_practice_info();
			ot_practice_info.setId(id);
			ot_practice_info.setCustId(person_info.getId());
			String RegisterType = RegexUtils.find("<dd><span>注册类别：</span><b>(" + CN_REGEX + ")</b></dd>", dl);
			ot_practice_info.setRegisterType(sysDictService.findOrSaveByTypeLabel("oLife_PerRegisterType", RegisterType));
			ot_practice_info.setMajor(RegexUtils.find("<dd><span>注册专业：</span>" + CN_REGEX + "</dd>", dl));
			ot_practice_info.setCertificate(RegexUtils.find("<dd><span>证书编号：</span>" + CN_REGEX + "</dd>", dl));
			ot_practice_info.setPracticeNo(RegexUtils.find("<dd><span>执业印章号：</span>" + CN_REGEX + "</dd>", dl));
			ot_practice_info.setValidDate(RegexUtils.find("<dd><span>有效期：</span>(\\d{4}年\\d{2}月\\d{2}日)</dd>", dl));
			
			String register = RegexUtils.find("<dt><span>注册单位：</span>" 
						+ "\\s*<a href=\"/dataservice/query/comp/compDetail/\\d+\"" 
						+ "\\s*data-qyid=\"(\\d+)\">" + "[\u4e00-\u9fa5（）ⅠⅡ、/\\-\\w\\d\\(\\)]+"
						+ "\\s*</a></dt>", dl);
			
			if("".equals(register)) {
				register = RegexUtils.find("<dt><span>注册单位：</span>" + 
						"\\s*" + CN_REGEX, dl); 
			}
			ot_practice_info.setRegister(register);
			ot_practice_info.setCreate_by("1");
			ot_practice_info.setCreate_date(new Date());
			ot_practice_info.setUpdate_by("1");
			ot_practice_info.setUpdate_date(new Date());
			ot_practice_info.setDel_flag("0");
			ot_practice_info.setCheck_status("0");
			em.persist(ot_practice_info);
			
			//注册企业
			Ot_register_info register_info = new Ot_register_info();
			register_info.setId(id);
			register_info.setRegisterName(person_info.getName());
			register_info.setCustId(person_info.getId());
			register_info.setCompanyno(register);
			register_info.setRegisterType(ot_practice_info.getRegisterType());
			register_info.setRegisterNo(ot_practice_info.getPracticeNo());
			register_info.setRegisterJor(ot_practice_info.getMajor());
			register_info.setCreate_by("1");
			register_info.setCreate_date(new Date());
			register_info.setUpdate_by("1");
			register_info.setUpdate_date(new Date());
			register_info.setDel_flag("0");
			em.persist(register_info);
			//未写注册专业，注册专业页面无该数据
			i++;
		}
		//个人工程业绩（任务）
		Task newTask = new Task();
		newTask.setCode(task.getCode());
		newTask.setHostUrl(task.getHostUrl());
		newTask.setRequestUrl("/dataservice/query/staff/staffPerformanceListSys/" + task.getCode());
		newTask.setMethod(RequestMethod.GET);
//		newTask.setStatus(TaskStatus.Ready);
		newTask.setProcessHandlerClassName("MohurdDataserviceQueryStaffStaffPerformanceListSysHandler");
		taskService.save(newTask);
		//个人变更记录（任务）
		Task newTask2 = new Task();
		newTask2.setCode(task.getCode());
		newTask2.setHostUrl(task.getHostUrl());
		newTask2.setRequestUrl("/dataservice/query/staff/staffWorkRecordList/" + task.getCode());
		newTask2.setMethod(RequestMethod.GET);
//		newTask2.setStatus(TaskStatus.Ready);
		newTask2.setProcessHandlerClassName("MohurdDataserviceQueryStaffStaffWorkRecordListHandler");
		taskService.save(newTask2);
		return null;
	}

}
