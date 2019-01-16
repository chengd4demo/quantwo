package com.qt.air.cleaner.scheduled.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.air.cleaner.scheduled.domain.WeiXinDownload;

@Repository
public interface WeiXinDownloadRepository extends JpaRepository<WeiXinDownload, String> {
	WeiXinDownload findByAppIdAndMchNoAndDownloadDateAndTypeAndRemoved(String appid,String machNo,String type,Date downloadDate,boolean removed);
}
