package com.zd.mole.site.mohurd.process;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zd.mole.process.ProcessHandler;
import com.zd.mole.task.Task;

public class MohurdDataserviceQueryCompCaDetailListHandler implements ProcessHandler {

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
		if(m.find()) {
			System.out.println(m.group(1));
			System.out.println(m.group(2));
			System.out.println(m.group(3));
			System.out.println(m.group(4));
			System.out.println(m.group(5));
			System.out.println(m.group(6));
		}
		
		return null;
	}

}
