package com.qt.air.cleaner.market.repository.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.generic.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
	List<Agent> findByRemoved(Boolean removed);
	Page<Agent> findAll(Specification<Agent> specification, Pageable pageable);
	Agent findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update Agent I set I.removed = true where I.id = :id")
	void deleteAgent(@Param("id") String id);
}
