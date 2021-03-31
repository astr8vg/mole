package com.zd.mole.parser;

import java.io.InputStream;

import com.zd.mole.task.Task;

/**
 * 解析器接口
 * @author ZhangDi
 *
 */
public interface DataParser {

	void parser (Task task, InputStream in);

}