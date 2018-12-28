package com.qt.air.cleaner.market.repository.price;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.price.PriceValue;

@Repository
public interface PriceValueRepository extends JpaRepository<PriceValue, String> {
	PriceValue findByIdAndRemoved(String id, boolean removed);
	
	Page<PriceValue> findAll(Specification<PriceValue> spec, Pageable pageable);
	
}
