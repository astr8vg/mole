package com.zd.mole.site.mohurd.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
import com.zd.mole.site.mohurd.entity.Ot_company_behavior;
import com.zd.mole.site.mohurd.entity.Ot_person_behavior;
import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;
import com.zd.mole.task.service.TaskService;
import com.zd.mole.utils.RegexUtils;

@Component
@Transactional
public class MohurdAsiteCreditRecordQueryHandler implements ProcessHandler {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext 
	private EntityManager em;
	
	@Resource
	private TaskService taskService;

	@Override
	public List<Task> handler(Task task, String text) {
		int objectType = 0;
		//翻页捕获
		String regex = "<script>__pgfm\\('',\\{"
				+ "\"approval_unit\":\\[\"\"\\],"
				+ "\"object_type\":\\[\"(?<objectType>\\d+)\"\\],"
				+ "\"\\$total\":(?<total>\\d+),"
				+ "\"object_name\":\\[\"\"\\],"
				+ "\"\\$reload\":0,"
				+ "\"\\$pg\":(?<pg>\\d+),"
				+ "\"\\$pgsz\":(?<pgsz>\\d+)"
				+ "\\}\\)</script> <a sf='pagebar' sf:data=\"\\(\\{"
				+ "pg:\\d+,"
				+ "ps:\\d+,"
				+ "tt:\\d+,"
				+ "pn:\\d+,"
				+ "pc:\\d+,"
				+ "id:'',"
				+ "st:true"
				+ "\\}\\)\"></a>";
		Pattern p2 = Pattern.compile(regex);
		Matcher m2 = p2.matcher(text);
		if(m2.find()) {
			//征信对象类别 1：工程建设企业；2：从业人员
			objectType = Integer.parseInt(m2.group("objectType"));
			int total = Integer.parseInt(m2.group("total"));
			int pg = Integer.parseInt(m2.group("pg"));
			int pgsz = Integer.parseInt(m2.group("pgsz"));
			String param = "approval_unit="
					+ "&object_type=" + objectType
					+ "&$total=" + total
					+ "&object_name="
					+ "&$reload=0"
					+ "&$pg=" + (pg + 1)
					+ "&$pgsz=" + pgsz;
			if (pg * pgsz < total) {
				Task newTask = new Task();
				newTask.setHostUrl(task.getHostUrl());
				newTask.setRequestUrl(task.getRequestUrl());
				newTask.setMethod(task.getMethod());
				newTask.setParam(param);
				newTask.setStatus(TaskStatus.Ready);
				newTask.setProcessHandlerClassName("MohurdAsiteCreditRecordQueryHandler");
				taskService.save(newTask);
			}
		}
		
//		String tbody = text;
		String tbody = RegexUtils.find("<tbody class=\"cursorDefault\">" + "([\\s\\S]*?)" + "</tbody>", text);
		Pattern p1 = Pattern.compile("<tr>(\\s*<td data-header=[\\s\\S]*?)</tr>");
		Matcher m1 = p1.matcher(tbody);
		while(m1.find()) {
			String tr = m1.group(1);
//			String s = RegexUtils.find(
//					"<td data-header=\"诚信记录编号\" class=\"github-posi\">" + 
//					"\\s*<div class=\"github-fork-ribbon-wrapper left\">" + 
//					"\\s*<div class=\"github-fork-ribbon github-padding credit_bad\">" + 
//					"\\s*<a class=\"credit_icon\">" + CN_REGEX + "</a>", tr);
			//行为类型0:不良行为
			String BehaviorType = "0";
			//诚信记录编号
			String behaviorNo = RegexUtils.find("<span style=\"font-size:13px;margin-left: 5px \">([\\w\\d-]+)</span>", tr);
			//诚信记录主体 ot_company_info表id
			String companyNo = RegexUtils.find("<td data-header=\"诚信记录主体\" style=\"padding: 0 1em\" >" 
						+ "\\s*<a target=\"_blank\" href=\"/dataservice/query/comp/compDetail/(\\d+)\">" + CN_REGEX
						+ "\\s*</a></td>", tr);
			String personNo = RegexUtils.find("<td data-header=\"诚信记录主体\" style=\"padding: 0 1em\" >" 
						+ "\\s*<a target=\"_blank\" href=\"/dataservice/query/staff/staffDetail/(\\d+)\">" + CN_REGEX
						+ "\\s*</a></td>", tr);
			if("".equals(companyNo)) {
				companyNo = RegexUtils.find("<td data-header=\"诚信记录主体\" style=\"padding: 0 1em\" >" + CN_REGEX + "</td>", tr);
			}
			if("".equals(personNo)) {
				personNo =  RegexUtils.find("<td data-header=\"诚信记录主体\" style=\"padding: 0 1em\" >" + CN_REGEX + "</td>", tr);
			}
			//ot_company_info表id
			String entity_id = companyNo;
			//处理方式（比如行政处理）
			String Title = RegexUtils.find("<td data-header=\"决定内容\" style=\"text-align: left;\">"
							+ "\\s*<div style=\"margin-bottom:4px\">"
							+ "\\s*<span style=\"color:#cc0000\">【" + CN_REGEX + "】</span>", tr);
			//决定日期(数据库缺少)
			String dd = RegexUtils.find("<span style=\"color: #999;font-size:13px;margin-left: 5px \">决定日期：" + FORMAT_DATE + "</span>", tr);
			Date decisionDate = new Date(0);
			if(!"".equals(dd)) {
				try {
					decisionDate = new SimpleDateFormat("yyyy-MM-dd").parse(dd);
				} catch (ParseException e) {
					log.error("决定日期字段，日期转换错误原始字段为[{}]", dd);
					e.printStackTrace();
				}
			}
			//决定内容
			String Content = RegexUtils.find("</div>" 
					+ "\\s*" + CN_REGEX
					+ "\\s*</td>"
					+ "\\s*<td data-header=\"实施部门（文号）\">" , tr);
			
			//查看事由
			String remarks = RegexUtils.find(
					"<a class=\"viewReason formsubmit fra\" data-no=\"[\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、\\[\\]\\〔\\〕]+\" data-text=\"" + CN_REGEX + "\">查看事由</a>", tr);
			//文号
			String docNo = RegexUtils.find(
					"<a class=\"viewReason formsubmit fra\" data-no=\"([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、\\[\\]\\〔\\〕]+)\" data-text=\"" + CN_REGEX + "\">查看事由</a>", tr);
			//实施部门
			String dept = RegexUtils.find(
					"<td data-header=\"实施部门（文号）\">" + CN_REGEX + "<div style=\"color: #999;font-size:13px;\">", tr);
			//实施部门(文号)
			String ImplDept = dept + docNo;
			//发布有效期
			String vd = RegexUtils.find(
					"<td data-header=\"发布有效期\">" + FORMAT_DATE + "</td>", tr);
			Date ValidDate = new Date(0);
			if(!"".equals(vd)) {
				try {
					ValidDate = new SimpleDateFormat("yyyy-MM-dd").parse(vd);
				} catch (ParseException e) {
					log.error("决定日期字段，日期转换错误原始字段为[{}]", dd);
					e.printStackTrace();
				}
			}
			if(objectType == 1) {
				//工程建设企业诚信记录主体
				//保存到ot_company_behavior表
				Ot_company_behavior behavior = new Ot_company_behavior();
				behavior.setId(UUID.randomUUID().toString());
				behavior.setBehaviorNo(behaviorNo);
				behavior.setBehaviorType(BehaviorType);
				behavior.setCompanyNo(companyNo);
				behavior.setEntity_id(entity_id);
				behavior.setTitle(Title);
				behavior.setDecisionDate(decisionDate);
				behavior.setContent(Content);
				behavior.setRemarks(remarks);
				behavior.setDocNo(docNo);
				behavior.setDept(dept);
				behavior.setImplDept(ImplDept);
				behavior.setValidDate(ValidDate);
				behavior.setCreate_by("1");
				behavior.setUpdate_by("1");
				behavior.setCreate_date(new Date());
				behavior.setUpdate_date(new Date());
				behavior.setDel_flag("0");
				behavior.setRemarks(remarks);
				em.persist(behavior);
			} else if(objectType == 2) {
				//从业人员诚信记录主体
				//保存到ot_person_behavior表
				Ot_person_behavior behavior = new Ot_person_behavior();
				behavior.setId(UUID.randomUUID().toString());
				behavior.setBehaviorNo(behaviorNo);
				behavior.setBehaviorType(BehaviorType);
				behavior.setPersonNo(personNo);
				behavior.setEntity_id(entity_id);
				behavior.setTitle(Title);
				behavior.setDecisionDate(decisionDate);
				behavior.setContent(Content);
				behavior.setRemarks(remarks);
				behavior.setDocNo(docNo);
				behavior.setDept(dept);
				behavior.setImplDept(ImplDept);
				behavior.setValidDate(ValidDate);
				behavior.setCreate_by("1");
				behavior.setUpdate_by("1");
				behavior.setCreate_date(new Date());
				behavior.setUpdate_date(new Date());
				behavior.setDel_flag("0");
				behavior.setRemarks(remarks);
				em.persist(behavior);
			}
		}
		
		
		return null;
	}
}
