package com.zd.mole.task.service;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.zd.mole.task.TaskStatus;
import com.zd.mole.task.entity.Task;

@Service
@Transactional
public class TaskService {
	
//	private Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext 
	private EntityManager em;

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
