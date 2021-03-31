package com.zd.mole.site.mohurd.entity;import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mole_mohurd_comp_list_apt")
public class MonhurdCompListApt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String apt_code;
	
	private String apt_scope;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApt_code() {
		return apt_code;
	}
	public void setApt_code(String apt_code) {
		this.apt_code = apt_code;
	}
	public String getApt_scope() {
		return apt_scope;
	}
	public void setApt_scope(String apt_scope) {
		this.apt_scope = apt_scope;
	}
	
	
}
