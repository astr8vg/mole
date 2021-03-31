package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ot_company_qual_info {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "CompanyCode")
	private String CompanyCode;
	@Column(name = "AptitudeType")
	private String AptitudeType;
	@Column(name = "AptitudeNo")
	private String AptitudeNo;
	@Column(name = "AptitudeName")
	private String AptitudeName;
	@Column(name = "IssueDate")
	private Date IssueDate;
	@Column(name = "ValidDate")
	private Date ValidDate;
	@Column(name = "Office")
	private String Office;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	private String check_status;
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return id;
	}
	public void setCompanyCode(String CompanyCode){
		this.CompanyCode = CompanyCode;
	}
	public String getCompanyCode(){
		return CompanyCode;
	}
	public void setAptitudeType(String AptitudeType){
		this.AptitudeType = AptitudeType;
	}
	public String getAptitudeType(){
		return AptitudeType;
	}
	public void setAptitudeNo(String AptitudeNo){
		this.AptitudeNo = AptitudeNo;
	}
	public String getAptitudeNo(){
		return AptitudeNo;
	}
	public void setAptitudeName(String AptitudeName){
		this.AptitudeName = AptitudeName;
	}
	public String getAptitudeName(){
		return AptitudeName;
	}
	public void setIssueDate(Date IssueDate){
		this.IssueDate = IssueDate;
	}
	public Date getIssueDate(){
		return IssueDate;
	}
	public void setValidDate(Date ValidDate){
		this.ValidDate = ValidDate;
	}
	public Date getValidDate(){
		return ValidDate;
	}
	public void setOffice(String Office){
		this.Office = Office;
	}
	public String getOffice(){
		return Office;
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
}
