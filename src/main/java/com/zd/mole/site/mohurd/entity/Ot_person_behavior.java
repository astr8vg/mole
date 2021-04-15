package	com.zd.mole.site.mohurd.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ot_person_behavior")
public class Ot_person_behavior {
	@Id
	private String id;
	@Column(name = "behaviorNo")
	private String behaviorNo;
	@Column(name = "personNo")
	private String personNo;
	@Column(name = "Content")
	private String Content;
	@Column(name = "Title")
	private String Title;
	@Column(name = "ImplDept")
	private String ImplDept;
	@Column(name = "ValidDate")
	private Date ValidDate;
	@Column(name = "BehaviorType")
	private String BehaviorType;
	@Column(name = "EntityType")
	private String EntityType;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private String del_flag;
	private String check_status;
	private String entity_id;
	@Column(name = "decisionDate")
	private Date decisionDate;
	@Column(name = "docNo")
	private String docNo;
	private String dept;
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setBehaviorNo(String behaviorNo){
		this.behaviorNo = behaviorNo;
	}
	public String getBehaviorNo(){
		return behaviorNo;
	}
	public void setPersonNo(String personNo){
		this.personNo = personNo;
	}
	public String getPersonNo(){
		return personNo;
	}
	public void setContent(String Content){
		this.Content = Content;
	}
	public String getContent(){
		return Content;
	}
	public void setTitle(String Title){
		this.Title = Title;
	}
	public String getTitle(){
		return Title;
	}
	public void setImplDept(String ImplDept){
		this.ImplDept = ImplDept;
	}
	public String getImplDept(){
		return ImplDept;
	}
	public void setValidDate(Date ValidDate){
		this.ValidDate = ValidDate;
	}
	public Date getValidDate(){
		return ValidDate;
	}
	public void setBehaviorType(String BehaviorType){
		this.BehaviorType = BehaviorType;
	}
	public String getBehaviorType(){
		return BehaviorType;
	}
	public void setEntityType(String EntityType){
		this.EntityType = EntityType;
	}
	public String getEntityType(){
		return EntityType;
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
	public void setEntity_id(String entity_id){
		this.entity_id = entity_id;
	}
	public String getEntity_id(){
		return entity_id;
	}
	public Date getDecisionDate() {
		return decisionDate;
	}
	public void setDecisionDate(Date decisionDate) {
		this.decisionDate = decisionDate;
	}
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
}
