package com.zd.mole.site.mohurd.service;

import java.util.List;
import java.util.Optional;

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
}
