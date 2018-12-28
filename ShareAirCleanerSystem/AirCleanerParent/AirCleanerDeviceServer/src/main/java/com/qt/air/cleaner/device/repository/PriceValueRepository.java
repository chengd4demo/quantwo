package com.qt.air.cleaner.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.device.domain.PriceValue;

@Repository
public interface PriceValueRepository extends JpaRepository<PriceValue, String> {

}
