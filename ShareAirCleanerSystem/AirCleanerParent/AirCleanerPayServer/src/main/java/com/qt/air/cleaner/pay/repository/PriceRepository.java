package com.qt.air.cleaner.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.pay.domain.PriceValue;

@Repository
public interface PriceRepository extends JpaRepository<PriceValue, String> {

}
