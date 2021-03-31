package com.zd.mole.task;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TaskService {
	
	private EntityManagerFactory emf = Persistence.createEntityManagerFactory( "mole" );
	
	private static TaskService taskService = new TaskService();
	 
	private TaskService() {}
	
	public static TaskService newInstance() {
		return taskService;
	}
	
	public void save (Task task) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		long count = (long) em.createQuery("select count(*) from Task where requestURL = :requestURL and param = :param")
				.setParameter("requestURL", task.getRequestURL())
				.setParameter("param", task.getParam())
				.getSingleResult();
        if (count > 0) {
        	System.out.println( "任务已存在" );
        } else {
        	em.persist(task);
        	System.out.println( "任务已添加" );
        }
        em.getTransaction().commit();
        em.close();
	}
	
	public void update(Task task) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.merge(task);
        em.getTransaction().commit();
        em.close();
    	System.out.println( "任务已更新" );
	}
	
	public int reset() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		int count = em.createQuery("update Task set status = :newStatus where status in :status")
					.setParameter("newStatus", TaskStatus.Ready)
					.setParameter("status", Arrays.asList(
							TaskStatus.ReadyToDownload, 
							TaskStatus.Downloading, 
							TaskStatus.DownloadFaild,
							TaskStatus.ParseFileNotFound
					))
					.executeUpdate();
		count += em.createQuery("update Task set status = :newStatus where status in :status")
					.setParameter("newStatus", TaskStatus.Downloaded)
					.setParameter("status", Arrays.asList(
							TaskStatus.ReadyToParse, 
							TaskStatus.Parseing, 
							TaskStatus.SaveingToThird, 
							TaskStatus.SaveFailed
					))
					.executeUpdate();
		em.getTransaction().commit();
        em.close();
		return count;
	}
	
	public List<Task> findByStatus(TaskStatus status, int count) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Task> tasks = em.createQuery("from Task where status = :status")
				.setParameter("status", status)
				.setFirstResult(0)
				.setMaxResults(count)
				.getResultList();
		em.getTransaction().commit();
        em.close();
		return tasks;
	}
	
	public boolean isAllStop() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		long count = (long) em.createQuery("select count(*) from Task where status not in :statusList")
				.setParameter("statusList", 
						Arrays.asList(TaskStatus.SaveSucceed, TaskStatus.SaveFailed, TaskStatus.ParseFileNotFound) )
				.getSingleResult();
		em.getTransaction().commit();
        em.close();
		return count == 0;
	}

	public void close() {
		emf.close();
	}
}
