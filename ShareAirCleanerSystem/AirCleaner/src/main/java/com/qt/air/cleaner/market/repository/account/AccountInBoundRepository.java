package com.qt.air.cleaner.market.repository.account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.AccountInBound;

@Repository
public interface AccountInBoundRepository extends JpaRepository<AccountInBound, String> {
	List<AccountInBound> findByRemoved(Boolean removed);
	Page<AccountInBound> findAll(Specification<AccountInBound> specification, Pageable pageable);
	AccountInBound findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query(value="update AccountInBound set state=1 where id=:id and removed=false")
	void updateState(@Param("id") String id);
}
