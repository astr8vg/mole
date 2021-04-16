package com.zd.mole.process;

import java.util.List;

import com.zd.mole.task.entity.Task;

public interface ProcessHandler {
	
	/**
	 * 可以捕获/a/b/c
	 * http://www.baidu.com/a/b/c
	 * https://www.baidu.com/a/b/c
	 * http://baidu.com/a/b/c
	 * https://baidu.com/a/b/c
	 * /a/b/c
	 */
	String URL_REGEX = "((https?://)?([\\w\\d]+\\.)+[\\w\\d]+)?(/?.+)";
	
	String CN_REGEX = "\\s*([\u4e00-\u9fa5\\·\\（\\）\\【\\】\\《\\》\\〔\\〕\\、\\，\\。\\：\\；\\”\\“\\w\\d\\s\\(\\)\\[\\]\\{\\}\\.-]+)\\s*";
	String CN_REGEX_NO_S = "([\u4e00-\u9fa5\\·\\（\\）\\【\\】\\《\\》\\〔\\〕\\、\\，\\。\\：\\；\\”\\“\\w\\d\\s\\(\\)\\[\\]\\{\\}\\.-]+)";
	
	String INNER_HTML = "([\\s\\S]*?)";
	String INNER_HTML2 = "((?:\\r\\n|.)*?)";
	
	String NUMBER = "([\\d\\.]+)";
	
	String FORMAT_DATE = "(\\d{4}-\\d{2}-\\d{2})";
	
	/**
	 * 处理数据
	 * @param task
	 * @param data
	 * @return 返回新任务，如果为空值，则不创建新任务
	 */
	List<Task> handler(Task task, String text);
}
