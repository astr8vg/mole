package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ot_register_info")
public class Ot_register_info {
	@Id
	private String id;
	@Column(name = "RegisterName")
	private String RegisterName;
	@Column(name = "CustId")
	private String CustId;
	@Column(name = "RegisterType")
	private String RegisterType;
	@Column(name = "RegisterJor")
	private String RegisterJor;
	@Column(name = "RegisterNo")
	private String RegisterNo;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	private String companyno;
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setRegisterName(String RegisterName){
		this.RegisterName = RegisterName;
	}
	public String getRegisterName(){
		return RegisterName;
	}
	public void setCustId(String CustId){
		this.CustId = CustId;
	}
	public String getCustId(){
		return CustId;
	}
	public void setRegisterType(String RegisterType){
		this.RegisterType = RegisterType;
	}
	public String getRegisterType(){
		return RegisterType;
	}
	public void setRegisterNo(String RegisterNo){
		this.RegisterNo = RegisterNo;
	}
	public String getRegisterNo(){
		return RegisterNo;
	}
	public void setRegisterJor(String RegisterJor){
		this.RegisterJor = RegisterJor;
	}
	public String getRegisterJor(){
		return RegisterJor;
	}
	public void setCreate_by(String create_by){
		this.create_by = create_by;
	}
	public String getCreate_by(){
		return create_by;
	}
	public void setCreate_date(Date create_date){
		this.create_date = create_date;
	}
	public Date getCreate_date(){
		return create_date;
	}
	public void setUpdate_by(String update_by){
		this.update_by = update_by;
	}
	public String getUpdate_by(){
		return update_by;
	}
	public void setUpdate_date(Date update_date){
		this.update_date = update_date;
	}
	public Date getUpdate_date(){
		return update_date;
	}
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	public String getRemarks(){
		return remarks;
	}
	public void setDel_flag(String del_flag){
		this.del_flag = del_flag;
	}
	public String getDel_flag(){
		return del_flag;
	}
	public void setCompanyno(String companyno){
		this.companyno = companyno;
	}
	public String getCompanyno(){
		return companyno;
	}
}
