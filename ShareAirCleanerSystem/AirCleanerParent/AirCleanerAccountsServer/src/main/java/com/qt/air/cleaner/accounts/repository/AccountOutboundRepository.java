package com.qt.air.cleaner.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.accounts.domain.AccountOutBound;

@Repository
public interface AccountOutboundRepository extends JpaRepository<AccountOutBound, String> {
	AccountOutBound findByIdAndRemoved(String id, Boolean removed);
	@Modifying
	@Query(value="update AccountOutBound set removed=true,lastOperateTime=:updateTime where id=:id ")
	void cancellUpdate(@Param("id") String id,@Param("updateTime") java.util.Date updateTime);
}
