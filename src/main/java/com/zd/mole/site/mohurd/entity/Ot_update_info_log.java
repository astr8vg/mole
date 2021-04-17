package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ot_update_info_log")
public class Ot_update_info_log {
	@Id
	private String id;
	@Column(name = "updateEntity")
	private String updateEntity;
	@Column(name = "UpdateInfo")
	private String UpdateInfo;
	@Column(name = "UpdateType")
	private String UpdateType;
	@Column(name = "UpdateNature")
	private String UpdateNature;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	@Column(name = "oldInfo")
	private String oldInfo;
	@Column(name = "newInfo")
	private String newInfo;
	//注册类别
	@Column(name = "registerType")
	private String registerType;
	
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setUpdateEntity(String updateEntity){
		this.updateEntity = updateEntity;
	}
	public String getUpdateEntity(){
		return updateEntity;
	}
	public void setUpdateInfo(String UpdateInfo){
		this.UpdateInfo = UpdateInfo;
	}
	public String getUpdateInfo(){
		return UpdateInfo;
	}
	public void setUpdateType(String UpdateType){
		this.UpdateType = UpdateType;
	}
	public String getUpdateType(){
		return UpdateType;
	}
	public void setUpdateNature(String UpdateNature){
		this.UpdateNature = UpdateNature;
	}
	public String getUpdateNature(){
		return UpdateNature;
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
	public String getOldInfo() {
		return oldInfo;
	}
	public void setOldInfo(String oldInfo) {
		this.oldInfo = oldInfo;
	}
	public String getNewInfo() {
		return newInfo;
	}
	public void setNewInfo(String newInfo) {
		this.newInfo = newInfo;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
}
