package com.qt.air.cleaner.market.repository.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.WinningConfing;
@Repository
public interface WinningConfingRepository extends JpaRepository<WinningConfing, String>  {
	Page<WinningConfing> findAll(Specification<WinningConfing> specification, Pageable pageable);
	WinningConfing findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query(value="update WinningConfing set state=1 where id=:id and removed=false")
	void updateSave(@Param("id") String id);
}
