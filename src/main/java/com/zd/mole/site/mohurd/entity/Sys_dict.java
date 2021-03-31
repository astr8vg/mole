package	com.zd.mole.site.mohurd.entity;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sys_dict {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String value;
	private String label;
	private String type;
	private String description;
	private BigDecimal sort;
	private String parent_id;
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private String remarks;
	private Boolean del_flag;
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setValue(String value){
		this.value = value;
	}
	public String getValue(){
		return value;
	}
	public void setLabel(String label){
		this.label = label;
	}
	public String getLabel(){
		return label;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	public void setParent_id(String parent_id){
		this.parent_id = parent_id;
	}
	public String getParent_id(){
		return parent_id;
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
	public BigDecimal getSort() {
		return sort;
	}
	public void setSort(BigDecimal sort) {
		this.sort = sort;
	}
	public void setDel_flag(Boolean del_flag) {
		this.del_flag = del_flag;
	}
	public Boolean getDel_flag() {
		return del_flag;
	}
}
