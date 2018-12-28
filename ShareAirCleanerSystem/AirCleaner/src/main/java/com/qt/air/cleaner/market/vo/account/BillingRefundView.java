package com.qt.air.cleaner.market.vo.account;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.account.BillingRefund;

public class BillingRefundView implements Serializable {
	private static final long serialVersionUID = 8773796144126424859L;
	private String id;
	private String name;//用户名
	private String phoneNum;//联系电话
	private String nickName;//昵称
	private String createTime;//申请退款时间
	private String refundType;//退款类型
	private Float totalFee;//金额
	private String refundStatus;//退款状态
	private String rejectReason; //驳回原因
	private String rejectReasonId; //原因Id
	@JsonIgnore
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
	public BillingRefundView(BillingRefund billingRefund) {
		super();
		this.setId(billingRefund.getId());
		this.setCreateTime(billingRefund.getCreateTime() == null? "" :df.format(billingRefund.getCreateTime()));
		this.setRefundType(billingRefund.getRefundBillingId());
		this.setTotalFee(billingRefund.getTotalFee());
		this.setRefundStatus(billingRefund.getRefundStatus());
		if(billingRefund.getBillingRefundReason()!= null) {
			this.setRejectReason(billingRefund.getBillingRefundReason().getRejectReason());
			this.setRejectReasonId(billingRefund.getBillingRefundReason().getId());
		}
		
	}
	
	public BillingRefundView() {
		super();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public String getNickName() {
		return nickName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getRefundType() {
		return refundType;
	}

	public Float getTotalFee() {
		return totalFee;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}

	public void setTotalFee(Float totalFee) {
		this.totalFee = totalFee;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(String rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	@Override
	public String toString() {
		return "BillingRefundView [id=" + id + ", nickName=" + nickName + ", createTime=" + createTime
				+ ", refundStatus=" + refundStatus + "]";
	}

	
	
	
}
