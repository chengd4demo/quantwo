package com.qt.air.cleaner.market.repository.account;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.account.AccountOutBound;

@Repository
public interface AccountOutBoundRepository extends JpaRepository<AccountOutBound, String>{

	Page<AccountOutBound> findAll(Specification<AccountOutBound> specification, Pageable pageable);
	AccountOutBound findByIdAndRemoved(String id, Boolean removed);
	@Query("select count(t.id) from AccountOutBound t where (t.createTime between :start and :end) and t.state=0 and t.removed = false")
	Long findCount(@Param("start") Date start, @Param("end") Date end);
	@Query(value="SELECT (CASE WHEN count( t.id )>=10  OR sum( t.AMOUNT)>=2000 then 'false' else 'true' END) as result FROM ACT_ACCOUNT_OUTBOUND t WHERE trunc( t.OPERATE_TIME ) = trunc(SYSDATE) AND t.STATE in(1,2,4,6,7,8) AND t.account_id=:accountId",nativeQuery=true)
	boolean findFrequency(@Param("accountId") String accountId);
}
