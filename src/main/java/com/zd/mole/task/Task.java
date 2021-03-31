package com.zd.mole.task;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zd.mole.process.ProcessHandler;

/**
 * 任务
 * @author ZhangDi
 *
 */
@Entity
@Table(
	name = "mole_task",
	indexes = @Index(
        name = "idx_request_url",
        columnList = "requestURL",
        unique = false
    )
)
public class Task {
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 主机地址 http://jzsc.mohurd.gov.cn/ */
	private String hostURL;
	
	/** 请求地址(刨去主机地址) */
	private String requestURL;
	
	/** http请求方式POST GET */
	private RequestMethod method;
	
	/** 请求参数 */
	private String param;
	
	/** 本地目录 */
	private String folderURI;
	
	/** 文件路径 */
	private String fileURL;
	
	/** 文件名 */
	private String fileName;
	
	/** 任务状态 */
	private TaskStatus status; 
	
	/** 处理类名称 */
	private String processHandlerClassName;
	
	/** 处理类 */
	@Transient
	private ProcessHandler processDataHandler;
	
	/** 代码 */
	private String code;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getHostURL() {
		return hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}

	public String getFolderURI() {
		return folderURI;
	}

	public void setFolderURI(String folderURI) {
		this.folderURI = folderURI;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getProcessHandlerClassName() {
		return processHandlerClassName;
	}

	public void setProcessHandlerClassName(String processHandlerClassName) {
		this.processHandlerClassName = processHandlerClassName;
	}

	public ProcessHandler getProcessDataHandler() {
		return processDataHandler;
	}

	public void setProcessDataHandler(ProcessHandler processDataHandler) {
		this.processDataHandler = processDataHandler;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
