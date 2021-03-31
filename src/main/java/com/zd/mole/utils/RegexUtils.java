package com.zd.mole.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	public static String find(String regex, String text) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		if (m.find()) {
			return m.group(1);
		}
		return "";
	}
}
