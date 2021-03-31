package com.zd.mole.db;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ThirdService {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory( "third" );
	
	private static ThirdService thirdService = new ThirdService();
	 
	private ThirdService() {}
	
	public static ThirdService newInstance() {
		return thirdService;
	}
	
	public void saveOrUpdate(Object... entitys) {
		EntityManager em = emf.createEntityManager();
		for (Object object : entitys) {
			em.merge(object);
		}
		em.close();
	}
	
	/**
	 * 获取EntityManager
	 * 需要自己关闭
	 * @return
	 */
	public EntityManager em() {
		return emf.createEntityManager();
	}
	
	public void close() {
		emf.close();
	}
}
