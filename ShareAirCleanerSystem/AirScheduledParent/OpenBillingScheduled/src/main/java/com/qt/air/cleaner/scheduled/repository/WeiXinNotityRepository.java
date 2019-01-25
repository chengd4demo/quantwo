package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.WeiXinNotity;

@Repository
public interface WeiXinNotityRepository extends JpaSpecificationExecutor<WeiXinNotity>{

}
