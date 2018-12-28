package com.qt.air.cleaner.report.batch.service;

import java.util.Date;
import java.util.List;

import com.qt.air.cleaner.report.batch.model.ScanCodeAmount;

public interface ScanCodeAmountService {
  ScanCodeAmount findByDeviceName(String deviceName,Date date);
  List<ScanCodeAmount> findAll();
}
