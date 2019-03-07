package com.qt.air.cleaner.base.enums;

public enum ErrorCodeEnum {
	ES_1001("1001", "更新失败"),
	ES_1002("1002", "无相关信息"),
	ES_1003("1003", "新增失败"),
	ES_1004("1004", "删除失败"),
	ES_1005("1005", "密码不正确"),
	ES_1006("1006", "用户名不正确"),
	ES_1007("1007","分页查询失败"),
	ES_1008("1008","上传失败"),
	ES_1009("1009","custId为空"),
	ES_1010("1010","密码相同不需要更新"),
	ES_1011("1011","手机号或身份唯一识别码错误"),
	ES_1012("1012", "注册失败"),
	ES_1013("1013", "验证失败"),
	ES_1014("1014", "验证码错误"),
	ES_1015("1015", "验证码超时,请重新获取验证码"),
	ES_1016("1016", "新密码与旧密码相同,请重设定"),
	ES_1017("1017", "%s密码失败"),
	ES_1018("1018", "获取用户微信信息失败"),
	ES_1019("1019", "请使用微信支付"),
	ES_1020("1020", "支付请求错误,请重新支付"),
	ES_1021("1021", "创建预支付订单失败,请重新支付"),
	ES_1023("1023", "%s,请重新支付"),
	ES_1024("1024", "设备未成功开启"),
	ES_1025("1025", "系统未成功执行!"),
	ES_1026("1026", "输入密码错误"),
	ES_1027("1027", "申请提现失败"),
	ES_1028("1028","提现可用余额不足"),
	ES_1029("1029","提现已审核无法取消");
	
	/** 错误码 */
	private String errorCode;
	
	/** 错误信息 */
	private String message;
	
	private ErrorCodeEnum(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
