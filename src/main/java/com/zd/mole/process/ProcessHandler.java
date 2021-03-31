package com.zd.mole.process;

import java.util.List;

import com.zd.mole.task.Task;

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
	
	/**
	 * 保存数据时修剪数据
	 * @param data
	 * @return
	 */
	String trim (String data);
	
	/**
	 * 处理数据
	 * @param task
	 * @param data
	 * @return 返回新任务，如果为空值，则不创建新任务
	 */
	List<Task> handler(Task task, String data);
}
