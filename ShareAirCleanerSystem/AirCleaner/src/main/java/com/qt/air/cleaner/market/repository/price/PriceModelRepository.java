
package com.qt.air.cleaner.market.repository.price;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.market.domain.price.PriceModel;

@Repository
public interface PriceModelRepository extends JpaRepository<PriceModel, String> {
	PriceModel findByIdAndRemoved(String id, boolean removed);

	Page<PriceModel> findAll(Specification<PriceModel> spec, Pageable pageable);
	List<PriceModel> findByRemoved(Boolean removed);
}
