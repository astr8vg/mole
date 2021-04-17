package com.zd.mole.task.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;

@Service
@Transactional
public class TaskService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext 
	private EntityManager em;
	
	public void save(Task task) {
		long count = 0;
		if(task.getParam() == null) {
			count = (long) em
					.createQuery("select count(*) from Task where requestUrl = :requestUrl and param is null")
					.setParameter("requestUrl", task.getRequestUrl())
					.getSingleResult();
		} else {
			count = (long) em
				.createQuery("select count(*) from Task where requestUrl = :requestUrl and param = :param")
				.setParameter("requestUrl", task.getRequestUrl())
				.setParameter("param", task.getParam())
				.getSingleResult();
		}
		if(count > 0) {
			log.warn("已存在路径和参数相同的任务，未插入新任务：{} {}", task.getRequestUrl(), task.getParam());
		} else {
			em.persist(task);
		}
	}
	
	public int update(long id, TaskStatus status) {
		int count =	em.createQuery("update Task set status = :newStatus where id = :id")
					.setParameter("newStatus", status)
					.setParameter("id", id)
					.executeUpdate();
		return count;
	}

	@Deprecated
	public void update(Task task) {
		em.merge(task);
//		log.debug( "任务" + task.getId() + "已更新" );
	}
	
	public int reset() {
		int count = em.createQuery("update Task set status = :newStatus where status in :status")
					.setParameter("newStatus", TaskStatus.Ready)
					.setParameter("status", Arrays.asList(
							TaskStatus.InTheQueue,
							TaskStatus.Failed
					))
					.executeUpdate();
		return count;
	}
	public List<Task> findByStatus(TaskStatus status, int count) {
		
		List<Task> tasks = em.createQuery("from Task where status = :status")
				.setParameter("status", status)
				.setFirstResult(0)
				.setMaxResults(count)
				.getResultList();
		return tasks;
	}

}
