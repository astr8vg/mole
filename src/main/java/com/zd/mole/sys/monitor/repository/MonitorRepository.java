package com.zd.mole.sys.monitor.repository;

import org.springframework.data.repository.CrudRepository;

import com.zd.mole.sys.monitor.entity.Monitor;

public interface MonitorRepository extends CrudRepository<Monitor, String> {
	
}
