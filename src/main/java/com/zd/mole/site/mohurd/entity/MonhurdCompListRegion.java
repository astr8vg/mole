package com.zd.mole.site.mohurd.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mole_mohurd_comp_list_region")
public class MonhurdCompListRegion {
	
	@Id
	private String region_id;
	private String region_name;
	private String region_fullname;
	private Integer region_com_count;
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}
	public String getRegion_fullname() {
		return region_fullname;
	}
	public void setRegion_fullname(String region_fullname) {
		this.region_fullname = region_fullname;
	}
	public Integer getRegion_com_count() {
		return region_com_count;
	}
	public void setRegion_com_count(Integer region_com_count) {
		this.region_com_count = region_com_count;
	}

}
