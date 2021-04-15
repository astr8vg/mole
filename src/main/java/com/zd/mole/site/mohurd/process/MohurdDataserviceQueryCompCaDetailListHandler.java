package com.zd.mole.site.mohurd.process;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.zd.mole.utils.RegexUtils;

@Component
@Transactional
public class MohurdDataserviceQueryCompCaDetailListHandler implements ProcessHandler {

	private Log log = LogFactory.getLog(getClass());
	
	@PersistenceContext 
	private EntityManager em;

	@Autowired
	private SysDictService sysDictService;
	
	@Override
	public List<Task> handler(Task task, String data) {
		String regex = "<tr class=\"row\">((?:\\r\\n|.)*?)</tr>";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(data);
		while(m.find()) {
			String tr = m.group(1);

			Ot_company_qual_info cqi = new Ot_company_qual_info();
			cqi.setCreate_by("1");
			cqi.setUpdate_by("1");
			cqi.setCreate_date(new Date());
			cqi.setUpdate_date(new Date());
			cqi.setDel_flag("0");
			cqi.setCompanyCode(task.getCode());

			String aptitudeType = RegexUtils.find("<td data-header=\"资质类别\">([\u4e00-\u9fa5]+)</td>", tr);
			cqi.setAptitudeType(sysDictService.findByTypeLabel("oLife_CredentialsType", aptitudeType));
			cqi.setAptitudeNo(RegexUtils.find("<td data-header=\"资质证书号\">([\u4e00-\u9fa5\\w\\d-&;/\\(\\)\\（\\）\\s]+)</td>", tr));
			String aptitudeName = RegexUtils.find(
					"<td data-header=\"资质名称\" style=\"text-align:left;[\\w\\d\\s-:;]+\">"
					+ "\\s*" + "([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、\\Ⅰ\\Ⅱ/]+)"
					+ "\\s*(<i.+</i>)*" 
					+ "\\s*</td>", tr);
			cqi.setAptitudeName(aptitudeName);
			try {
				cqi.setIssueDate(new SimpleDateFormat("yyyy-MM-dd").parse(RegexUtils.find("<td data-header=\"发证日期\">(\\d{4}-\\d{2}-\\d{2})</td>", tr)));
			} catch (ParseException e) {
				log.error(e);
			}
			try {
				cqi.setValidDate(new SimpleDateFormat("yyyy-MM-dd").parse(RegexUtils.find("<td data-header=\"证书有效期\">(\\d{4}-\\d{2}-\\d{2})</td>", tr)));
			} catch (ParseException e) {
				log.error(e);
			}
			cqi.setOffice(RegexUtils.find("<td data-header=\"发证机关\">" + CN_REGEX +"</td>", tr));
			em.persist(cqi);
		}
		
		return null;
	}
	
}
