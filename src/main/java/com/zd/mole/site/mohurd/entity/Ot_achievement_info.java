package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ot_achievement_info")
public class Ot_achievement_info {
	@Id
	private String id;
	@Column(name = "CustId")
	private String custid;
	@Column(name = "ProjectNo")
	private String ProjectNo;
	@Column(name = "ProjectName")
	private String ProjectName;
	@Column(name = "ProjectArea")
	private String ProjectArea;
	@Column(name = "ProjectArea2")
	private String ProjectArea2;
	@Column(name = "ProjectType")
	private String ProjectType;
	@Column(name = "serveRole")
	private String serveRole;
	@Column(name = "developmentDw")
	private String developmentDw;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	private String check_status;
	public void setCustid(String custid){
		this.custid = custid;
	}
	public String getCustid(){
		return custid;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setProjectNo(String ProjectNo){
		this.ProjectNo = ProjectNo;
	}
	public String getProjectNo(){
		return ProjectNo;
	}
	public void setProjectName(String ProjectName){
		this.ProjectName = ProjectName;
	}
	public String getProjectName(){
		return ProjectName;
	}
	public void setProjectArea(String ProjectArea){
		this.ProjectArea = ProjectArea;
	}
	public String getProjectArea(){
		return ProjectArea;
	}
	public void setProjectType(String ProjectType){
		this.ProjectType = ProjectType;
	}
	public String getProjectType(){
		return ProjectType;
	}
	public void setServeRole(String serveRole){
		this.serveRole = serveRole;
	}
	public String getServeRole(){
		return serveRole;
	}
	public void setDevelopmentDw(String developmentDw){
		this.developmentDw = developmentDw;
	}
	public String getDevelopmentDw(){
		return developmentDw;
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
	public String getProjectArea2() {
		return ProjectArea2;
	}
	public void setProjectArea2(String projectArea2) {
		ProjectArea2 = projectArea2;
	}
}
