package com.zd.mole.site.mohurd.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.Ot_company_qual_info;
import com.zd.mole.site.mohurd.service.SysDictService;
import com.zd.mole.task.entity.Task;

@Component
@Transactional
public class MohurdDataserviceQueryCompCaDetailListHandler implements ProcessHandler {

	private Log log = LogFactory.getLog(getClass());
	
	@PersistenceContext 
	private EntityManager em;

	@Autowired
	private SysDictService sysDictService;
	
	@Override
	public String trim(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> handler(Task task, String data) {
		
		String regex = "<tr class=\"row\">"
				+ "\\s*<td data-header=\"序号\">\\d+</td>"
				+ "\\s*<td data-header=\"资质类别\">([\u4e00-\u9fa5]+)</td>"
				+ "\\s*<td data-header=\"资质证书号\">([\\w\\d]+)</td>"
				+ "\\s*<td data-header=\"资质名称\" style=\"text-align:left;[\\w\\d]+\">"
				+ "\\s*([\u4e00-\u9fa5]+)"
				+ "\\s*</td>"
				+ "\\s*<td data-header=\"发证日期\">(\\d{4}-\\d{2}-\\d{2})</td>"
				+ "\\s*<td data-header=\"证书有效期\">(\\d{4}-\\d{2}-\\d{2})</td>"
				+ "\\s*<td data-header=\"发证机关\">([\u4e00-\u9fa5]+)</td>"
				+ "\\s*<td class=\"view\">"
				+ "\\s*<a title=\"查看证书完整信息\" href=\"javascript:;\" class=\"layeropenwin\" data-qyid=\"[\\w\\d]+\" data-certno=\"[\\w\\d]+\">证书信息<div class=\"hide\">[\\w\\d]+</div></a>"
				+ "\\s*</td>"
				+ "\\s*</tr>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		while(m.find()) {
			Ot_company_qual_info cqi = new Ot_company_qual_info();
			cqi.setCreate_by("1");
			cqi.setUpdate_by("1");
			cqi.setCreate_date(new Date());
			cqi.setUpdate_date(new Date());
			cqi.setDel_flag("0");
			cqi.setCompanyCode(task.getCode());
			cqi.setAptitudeType(sysDictService.findByTypeLabel("oLife_CredentialsType", m.group(2)));
			cqi.setAptitudeNo(m.group(2));
			cqi.setAptitudeName(m.group(3));
			try {
				cqi.setIssueDate(new SimpleDateFormat("yyyy-MM-dd").parse(m.group(4)));
			} catch (ParseException e) {
				log.error(e);
				throw new RuntimeException(e);
			}
			try {
				cqi.setIssueDate(new SimpleDateFormat("yyyy-MM-dd").parse(m.group(5)));
			} catch (ParseException e) {
				log.error(e);
				throw new RuntimeException(e);
			}
			cqi.setOffice(m.group(6));
			log.debug("资质类别：" + m.group(1));
			log.debug("资质证书号：" + m.group(2));
			log.debug("资质名称：" + m.group(3));
			log.debug("发证日期：" + m.group(4));
			log.debug("证书有效期：" + m.group(5));
			log.debug("发证机关：" + m.group(6));
			
			em.persist(cqi);
		}
		
		return null;
	}

}
