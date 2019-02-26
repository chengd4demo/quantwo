package com.qt.air.cleaner.market.repository.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.generic.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {

}
