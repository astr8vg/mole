package com.zd.mole.site.mohurd.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.zd.mole.site.mohurd.entity.Sys_dict;

@Service
@Transactional
public class SysDictService {

	@PersistenceContext 
	private EntityManager em;
	
	public String findByTypeLabel(String type, String label) {
		List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = :type and label = :label")
				.setParameter("type", type)
				.setParameter("label", label)
				.getResultList();
		String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
		return Optional.ofNullable(value).orElse(label);
	}
	
	public String findOrSaveByTypeLabel(String type, String label) {
		List<Sys_dict> dicts = em.createQuery("from Sys_dict where type = :type and label = :label")
				.setParameter("type", type)
				.setParameter("label", label)
				.getResultList();
		String value = dicts.stream().findFirst().orElse(new Sys_dict()).getValue();
		if(value == null) {
			String uuid = UUID.randomUUID().toString();
			Sys_dict dict = new Sys_dict();
			dict.setCreate_by("1");
			dict.setCreate_date(new Date());
			dict.setUpdate_by("1");
			dict.setUpdate_date(new Date());
			dict.setDel_flag(false);
			dict.setType(type);
			dict.setLabel(label);
			dict.setValue(uuid);
			dict.setId(uuid);
			dict.setSort(new BigDecimal(0));
			em.persist(dict);
			return dict.getValue();
		}
		return Optional.ofNullable(value).orElse(label);
	}
	
}
