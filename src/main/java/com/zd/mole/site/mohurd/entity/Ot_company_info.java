package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ot_company_info")
public class Ot_company_info {
	@Id
	private String id;
	private String companycode;
	@Column(name = "companyOrgCode")
	private String companyOrgCode;
	private String companyname;
	private String companyer;
	private String companytype;
	private String companyarea;
	private String companyemail;
	private String companyaddress;
	private String no;
	private String belongorg;
	private Date startdate;
	private Date enddate;
	private String status;
	private String province;
	private String registcapi;
	private String econkind;
	private String scope;
	private Date termstart;
	private Date teamend;
	private Date checkdate;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	private String check_status;
	private String manage_area;
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setCompanycode(String companycode){
		this.companycode = companycode;
	}
	public String getCompanycode(){
		return companycode;
	}
	public void setCompanyOrgCode(String companyOrgCode){
		this.companyOrgCode = companyOrgCode;
	}
	public String getCompanyOrgCode(){
		return companyOrgCode;
	}
	public void setCompanyname(String companyname){
		this.companyname = companyname;
	}
	public String getCompanyname(){
		return companyname;
	}
	public void setCompanyer(String companyer){
		this.companyer = companyer;
	}
	public String getCompanyer(){
		return companyer;
	}
	public void setCompanytype(String companytype){
		this.companytype = companytype;
	}
	public String getCompanytype(){
		return companytype;
	}
	public void setCompanyarea(String companyarea){
		this.companyarea = companyarea;
	}
	public String getCompanyarea(){
		return companyarea;
	}
	public void setCompanyemail(String companyemail){
		this.companyemail = companyemail;
	}
	public String getCompanyemail(){
		return companyemail;
	}
	public void setCompanyaddress(String companyaddress){
		this.companyaddress = companyaddress;
	}
	public String getCompanyaddress(){
		return companyaddress;
	}
	public void setNo(String no){
		this.no = no;
	}
	public String getNo(){
		return no;
	}
	public void setBelongorg(String belongorg){
		this.belongorg = belongorg;
	}
	public String getBelongorg(){
		return belongorg;
	}
	public void setStartdate(Date startdate){
		this.startdate = startdate;
	}
	public Date getStartdate(){
		return startdate;
	}
	public void setEnddate(Date enddate){
		this.enddate = enddate;
	}
	public Date getEnddate(){
		return enddate;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getStatus(){
		return status;
	}
	public void setProvince(String province){
		this.province = province;
	}
	public String getProvince(){
		return province;
	}
	public void setRegistcapi(String registcapi){
		this.registcapi = registcapi;
	}
	public String getRegistcapi(){
		return registcapi;
	}
	public void setEconkind(String econkind){
		this.econkind = econkind;
	}
	public String getEconkind(){
		return econkind;
	}
	public void setScope(String scope){
		this.scope = scope;
	}
	public String getScope(){
		return scope;
	}
	public void setTermstart(Date termstart){
		this.termstart = termstart;
	}
	public Date getTermstart(){
		return termstart;
	}
	public void setTeamend(Date teamend){
		this.teamend = teamend;
	}
	public Date getTeamend(){
		return teamend;
	}
	public void setCheckdate(Date checkdate){
		this.checkdate = checkdate;
	}
	public Date getCheckdate(){
		return checkdate;
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
	public void setCheck_status(String check_status){
		this.check_status = check_status;
	}
	public String getCheck_status(){
		return check_status;
	}
	public void setManage_area(String manage_area){
		this.manage_area = manage_area;
	}
	public String getManage_area(){
		return manage_area;
	}
}
