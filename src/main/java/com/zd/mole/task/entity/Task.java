package com.zd.mole.task.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.zd.mole.task.RequestMethod;
import com.zd.mole.task.TaskStatus;

/**
 * 任务
 * @author ZhangDi
 *
 */
@Entity
@Table(
	name = "mole_task",
	indexes = {@Index(
        name = "idx_request_url_param",
        columnList = "requestURL, param",
        unique = false
    ),@Index(
		name = "idx_request_url",
        columnList = "requestURL",
        unique = false
	),@Index(
		name = "idx_remark",
		columnList = "remark",
        unique = false		
    ),@Index(
		name = "idx_code",
		columnList = "code",
        unique = false		
    )}
)
public class Task {
	
	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** 代码 */
	private String code;
	
	/** 主机地址 http://jzsc.mohurd.gov.cn/ */
	private String hostUrl;
	
	/** 请求地址(刨去主机地址) */
	private String requestUrl;
	
	/** http请求方式POST GET */
	private RequestMethod method;
	
	/** 请求参数 */
	private String param;
	
	/** 任务状态 */
	private TaskStatus status; 
	
	/** 处理类名称 */
	private String processHandlerClassName;
	
	/** 备注 */
	private String remark;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
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

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getProcessHandlerClassName() {
		return processHandlerClassName;
	}

	public void setProcessHandlerClassName(String processHandlerClassName) {
		this.processHandlerClassName = processHandlerClassName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
