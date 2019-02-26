package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
	Agent findByIdAndRemoved(String id,Boolean removed);
}
