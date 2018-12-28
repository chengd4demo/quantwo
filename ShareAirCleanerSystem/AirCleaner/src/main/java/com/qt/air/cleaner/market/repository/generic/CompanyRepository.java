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

import com.qt.air.cleaner.market.domain.generic.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
	List<Company> findByRemoved(Boolean removed);
	Page<Company> findAll(Specification<Company> specification, Pageable pageable);
	Company findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update Company I set I.removed = true where I.id = :id")
	void deleteCompany(@Param("id") String id);
	
	
}
