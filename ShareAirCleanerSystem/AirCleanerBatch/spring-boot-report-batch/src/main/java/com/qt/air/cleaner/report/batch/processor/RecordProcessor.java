package com.qt.air.cleaner.report.batch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.qt.air.cleaner.report.batch.model.Billing;
import com.qt.air.cleaner.report.batch.model.ScanCodeAmount;

public class RecordProcessor implements ItemProcessor<Billing, ScanCodeAmount> {
	private static final Logger logger = LoggerFactory.getLogger(RecordProcessor.class);

	@Override
	public ScanCodeAmount process(Billing item) throws Exception {
		logger.info("Processing ScanCodeAmount: {}", item);
		ScanCodeAmount scanCodeAmount = new ScanCodeAmount();
		scanCodeAmount.setCount(item.getCount());
		scanCodeAmount.setDeviceName(item.getMachNo());
		scanCodeAmount.setBillingTime(item.getCreateTime());
		scanCodeAmount.setTraderName(item.getTraderName());
		scanCodeAmount.setInvestorName(item.getInvestorName());
		logger.info("Processed ScanCodeAmount: {}", scanCodeAmount);
		return scanCodeAmount;
	}
}
