package com.qt.air.cleaner.scheduled.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.WeiXinDownload;

@Repository
public interface WeiXinDownloadRepository  extends JpaRepository<WeiXinDownload,String> {
	WeiXinDownload findByAppIdAndMchNoAndRemoved(String appId,String machNo,Boolean removed);
}
