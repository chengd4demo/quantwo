package com.qt.air.cleaner.market.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String>  {
	@Modifying
	@Query("update Account A set A.removed = true where A.id = :id")
	void deleteAccount(@Param("id") String id);
}
