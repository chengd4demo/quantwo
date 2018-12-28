package com.qt.air.cleaner.market.repository.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.WeiXinNotity;

@Repository
public interface WeiXinNotityRepository extends JpaRepository<WeiXinNotity, String>{

	Page<WeiXinNotity> findAll(Specification<WeiXinNotity> specification, Pageable pageable);

}
