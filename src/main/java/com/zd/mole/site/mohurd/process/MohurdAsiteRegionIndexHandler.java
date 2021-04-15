package com.zd.mole.site.mohurd.process;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zd.mole.process.ProcessHandler;
import com.zd.mole.site.mohurd.entity.MonhurdCompListRegion;
import com.zd.mole.task.entity.Task;

//@Component
@Transactional
public class MohurdAsiteRegionIndexHandler implements ProcessHandler {


	@PersistenceContext 
	private EntityManager em;
	
	@Override
	public List<Task> handler(Task task, String text) {
		
		text = "[{\"region_id\":\"110000\",\"region_name\":\"北京\",\"region_fullname\":\"北京市\"},{\"region_id\":\"120000\",\"region_name\":\"天津\",\"region_fullname\":\"天津市\"},{\"region_id\":\"130000\",\"region_name\":\"河北\",\"region_fullname\":\"河北省\"},{\"region_id\":\"140000\",\"region_name\":\"山西\",\"region_fullname\":\"山西省\"},{\"region_id\":\"150000\",\"region_name\":\"内蒙古\",\"region_fullname\":\"内蒙古自治区\"},{\"region_id\":\"210000\",\"region_name\":\"辽宁\",\"region_fullname\":\"辽宁省\"},{\"region_id\":\"220000\",\"region_name\":\"吉林\",\"region_fullname\":\"吉林省\"},{\"region_id\":\"230000\",\"region_name\":\"黑龙江\",\"region_fullname\":\"黑龙江省\"},{\"region_id\":\"310000\",\"region_name\":\"上海\",\"region_fullname\":\"上海市\"},{\"region_id\":\"320000\",\"region_name\":\"江苏\",\"region_fullname\":\"江苏省\"},{\"region_id\":\"330000\",\"region_name\":\"浙江\",\"region_fullname\":\"浙江省\"},{\"region_id\":\"340000\",\"region_name\":\"安徽\",\"region_fullname\":\"安徽省\"},{\"region_id\":\"350000\",\"region_name\":\"福建\",\"region_fullname\":\"福建省\"},{\"region_id\":\"360000\",\"region_name\":\"江西\",\"region_fullname\":\"江西省\"},{\"region_id\":\"370000\",\"region_name\":\"山东\",\"region_fullname\":\"山东省\"},{\"region_id\":\"410000\",\"region_name\":\"河南\",\"region_fullname\":\"河南省\"},{\"region_id\":\"420000\",\"region_name\":\"湖北\",\"region_fullname\":\"湖北省\"},{\"region_id\":\"430000\",\"region_name\":\"湖南\",\"region_fullname\":\"湖南省\"},{\"region_id\":\"440000\",\"region_name\":\"广东\",\"region_fullname\":\"广东省\"},{\"region_id\":\"450000\",\"region_name\":\"广西\",\"region_fullname\":\"广西壮族自治区\"},{\"region_id\":\"460000\",\"region_name\":\"海南\",\"region_fullname\":\"海南省\"},{\"region_id\":\"500000\",\"region_name\":\"重庆\",\"region_fullname\":\"重庆市\"},{\"region_id\":\"510000\",\"region_name\":\"四川\",\"region_fullname\":\"四川省\"},{\"region_id\":\"520000\",\"region_name\":\"贵州\",\"region_fullname\":\"贵州省\"},{\"region_id\":\"530000\",\"region_name\":\"云南\",\"region_fullname\":\"云南省\"},{\"region_id\":\"540000\",\"region_name\":\"西藏\",\"region_fullname\":\"西藏自治区\"},{\"region_id\":\"610000\",\"region_name\":\"陕西\",\"region_fullname\":\"陕西省\"},{\"region_id\":\"620000\",\"region_name\":\"甘肃\",\"region_fullname\":\"甘肃省\"},{\"region_id\":\"630000\",\"region_name\":\"青海\",\"region_fullname\":\"青海省\"},{\"region_id\":\"640000\",\"region_name\":\"宁夏\",\"region_fullname\":\"宁夏回族自治区\"},{\"region_id\":\"650000\",\"region_name\":\"新疆\",\"region_fullname\":\"新疆维吾尔自治区\"}]";

		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			List<LinkedHashMap<String, String>> list = mapper.readValue(text, ArrayList.class);
			list.forEach(l -> {
				MonhurdCompListRegion r = new MonhurdCompListRegion();
				r.setRegion_id(l.get("region_id"));
				r.setRegion_name(l.get("region_name"));
				r.setRegion_fullname(l.get("region_fullname"));
				em.merge(r);
			});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return null;
	}
}
