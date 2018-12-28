package com.qt.air.cleaner.market.repository.generic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qt.air.cleaner.market.domain.generic.Saler;

public interface SalerRepository extends JpaRepository<Saler, String> {
	List<Saler> findByRemoved(Boolean removed);
	Page<Saler> findAll(Specification<Saler> specification, Pageable pageable);
	Saler findByIdAndRemoved(String id,Boolean removed);
	@Modifying
	@Query("update Saler s set s.removed = true where s.id = :id")
	void deleteSaler(@Param("id") String id);
}

