package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ot_person_info")
public class Ot_person_info {
	@Id
	private String id;
	@Column(name = "Name")
	private String Name;
	@Column(name = "Sex")
	private String Sex;
	@Column(name = "CustId")
	private String CustId;
	private String profession;
	@Column(name = "CustType")
	private String CustType;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	private String check_status;
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setName(String Name){
		this.Name = Name;
	}
	public String getName(){
		return Name;
	}
	public void setSex(String Sex){
		this.Sex = Sex;
	}
	public String getSex(){
		return Sex;
	}
	public void setCustId(String CustId){
		this.CustId = CustId;
	}
	public String getCustId(){
		return CustId;
	}
	public void setProfession(String profession){
		this.profession = profession;
	}
	public String getProfession(){
		return profession;
	}
	public void setCustType(String CustType){
		this.CustType = CustType;
	}
	public String getCustType(){
		return CustType;
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
