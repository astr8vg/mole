package com.zd.mole.parser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zd.mole.parser.DataParser;
import com.zd.mole.task.Task;

/**
 * html数据解析器
 * @author ZhangDi
 *
 */
public class HtmlDataParser implements DataParser {
	
	@Override
	public void parser(Task task, InputStream in) {
		InputStreamReader inputStreamReader = new InputStreamReader(in);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		//拼接html
		String html = bufferedReader.lines().reduce("", String::concat);
//		System.out.println(html);
//		task.getMatchRules().stream().forEach(rule -> {
//			Pattern p = Pattern.compile(rule.getRegex());
////			System.out.println(rule.getRegex());
//			Matcher m = p.matcher(html);
//			List<String> values = new ArrayList<>();
//			while(m.find()) {
//				for (int i = 1; i <= m.groupCount(); i++) {
//					String t = m.group(i);
//					System.out.println(t);
//					values.add(t);
//				}
//			}
//			rule.setValues(values);
//		});
	}

}
