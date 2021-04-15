package com.zd.mole.site.mohurd.process;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.entity.Task;
import com.zd.mole.utils.RegexUtils;

public class MohurdDataserviceQueryProjectProjectDetailHandler implements ProcessHandler {

	@Override
	public List<Task> handler(Task task, String text) {
		String s = RegexUtils.find("<div class=\"user_info spmtop\">" 
				+ "\\s*<b title=\"平原县阳光尚景\"><i class=\"fa fa-cubes\"></i> 平原县阳光尚景</b>"
				+ "\\s*</div>", text);
		
		s = RegexUtils.find("<dd><span>项目编号：</span>(\\d+)</dd>", text);
		s = RegexUtils.find("<dd><span>省级项目编号：</span>(\\d+)</dd>", text);
		s = RegexUtils.find("<dd><span>所在区划：</span>" + CN_REGEX + "</dd>", text);
		s = RegexUtils.find("<dd><span>建设单位：</span>" + CN_REGEX + "</dd>", text);
		s = RegexUtils.find("<dd style=\"width: 66%\"><span>建设单位组织机构代码（统一社会信用代码）：</span>([\u4e00-\u9fa5\\w\\d-&;/\\(\\)\\（\\）\\s]+)</dd>", text);
		s = RegexUtils.find("<dd><span>项目分类：</span>" + CN_REGEX + "</dd>", text);
		s = RegexUtils.find("<dd><span>建设性质：</span>" + CN_REGEX + "</dd>", text);
		s = RegexUtils.find("<dd><span>工程用途：</span>" + CN_REGEX + "</dd>", text);
		s = RegexUtils.find("<dd><span>总投资：</span>([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、\\.]+)</dd>", text);
		s = RegexUtils.find("<dd><span>总面积：</span>([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、\\.]+)</dd>", text);
		s = RegexUtils.find("<dd><span>立项级别：</span>区县级" + CN_REGEX + "</dd>", text);
		s = RegexUtils.find("<dd><span>立项文号：</span>([\u4e00-\u9fa5\\（\\）\\-\\w\\d\\(\\)\\、\\.\\{\\}]+)</dd>", text);
		
		Pattern p = Pattern.compile("<div class=\"activeTinyTabContent\" id=\"tab_ztb\">" + INNER_HTML + "</div>");
		Matcher m = p.matcher(s);
		if(m.find()) {
			String tabZtb = m.group(1);
			p = Pattern.compile("<tr class=\"row\">" + INNER_HTML + "</tr>");
			m = p.matcher(tabZtb);
			while(m.find()) {
				s = "<td data-header=\"施工图审查合格书编号\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"省级施工图审查合格书编号\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"勘察单位名称\">\\s*" + CN_REGEX + "\\s*</td>";
				s = "<td data-header=\"勘察单位名称\">"
						+ "\\s*<a href=\"/dataservice/query/comp/compDetail/(\\d+)\" target=\"_blank\">" + CN_REGEX + "</a>" 
						+ "\\s*</td>";
				s = "<td data-header=\"设计单位名称\">" 
					+ "\\s*<a href=\"/dataservice/query/comp/compDetail/(\\d+)\" target=\"_blank\">" + CN_REGEX + "</a>"
					+ "\\s*</td>";
				s = "<td data-header=\"施工图审查机构名称\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"审查完成日期\">(d{4}-d{2}-d{2})</td>";
				String pk = RegexUtils.find(
						"<td data-header=\"查看\" class=\"view\"><a href=\"javascript:;\" data-title=\"施工图审查详情信息（施工图审查合格书编号：" + CN_REGEX + "）\" data-url=\"/dataservice/query/project/censorInfo/(\\d+)\" class=\"layeropenwin\"><i class=\"fa fa-search-plus\"></i></a></td>",
						tabZtb);
			}
		}
		//合同备案
		p = Pattern.compile("<div id=\"tab_htba\">" + INNER_HTML + "</div>");
		m = p.matcher(s);
		if(m.find()) {
			String tabHtba = m.group(1);
			p = Pattern.compile("<tr class=\"row\">" + INNER_HTML + "</tr>");
			m = p.matcher(tabHtba);
			while(m.find()) {
				s = "<td data-header=\"合同类别\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"合同备案编号\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"省级合同备案编号\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"合同金额（万元）\">" + NUMBER + "</td>";
				s = "<td data-header=\"合同签订日期\">(d{4}-d{2}-d{2})</td>";
				String pk = RegexUtils.find(
						"<td data-header=\"查看\" class=\"view\"><a href=\"javascript:;\" data-title=\"合同备案详情信息（合同备案编号：3714261604080101-HB-001）\" data-url=\"/dataservice/query/project/contractInfo/(\\d+)\" class=\"layeropenwin\"><i class=\"fa fa-search-plus\"></i></a></td>" 
						, text);
			}
		}
		//施工许可
		p = Pattern.compile("<div id=\"tab_sgxk\" >" + INNER_HTML + "</div>");
		m = p.matcher(s);
		if(m.find()) {
			String tabHtba = m.group(1);
			p = Pattern.compile("<tr class=\"row\">" + INNER_HTML + "</tr>");
			m = p.matcher(tabHtba);
			while(m.find()) {
				s = "<td data-header=\"施工许可证编号\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"省级施工许可证编号\">" + CN_REGEX + "</td>";
				s = "<td data-header=\"合同金额（万元）\">" + NUMBER + "</td>";
				s = "<td data-header=\"面积（平方米）\">" + NUMBER + "</td>";
				s = "<td data-header=\"发证日期\">(d{4}-d{2}-d{2})</td>";
				String pk = RegexUtils.find(
						"<td data-header=\"查看\" class=\"view\"><a href=\"javascript:;\" data-title=\"施工许可详情信息（施工许可证编号：3714261604080101-SX-001）\" data-url=\"/dataservice/query/project/buildliseInfo/(\\d+)\" class=\"layeropenwin\"><i class=\"fa fa-search-plus\"></i></a></td>" 
						, text);
			}
		}

		//竣工验收备案
		p = Pattern.compile("<div id=\"tab_jgysba\">" + INNER_HTML + "</div>");
		m = p.matcher(s);
		
		return null;
	}

}
