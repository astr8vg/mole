package com.zd.mole.sys.monitor.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mole_sys_monitor")
public class Monitor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;
	
	/** 每秒byte数*/
	private Long bs;

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public Long getBs() {
		return bs;
	}

	public void setBs(Long bs) {
		this.bs = bs;
	}
}
