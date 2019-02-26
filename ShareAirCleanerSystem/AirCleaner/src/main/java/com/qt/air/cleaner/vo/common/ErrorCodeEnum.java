package com.qt.air.cleaner.vo.common;

/**
 * @ClassName: ErrorCodeEnum
 * @Author: mabin@easier.cn
 * @Date: 2017年11月7日
 */
public enum ErrorCodeEnum {
	ES_1001("1001", "提交失败！"),
	ES_1002("1002", "请选择投资商信息！"),
	ES_1003("1003", "设备批次信息部存在！"),
	ES_1004("1004", "删除失败"),
	ES_1005("1005", "密码不正确"),
	ES_1006("1006", "用户名不正确"),
	ES_1007("1007","分页查询失败"),
	ES_1008("1008","上传失败"),
	ES_1009("1009","custId为空"),
	ES_1010("1010","密码相同不需要更新"),
	ES_2001("1001","角色信息不存在"),
	ES_3001("3001", "用户账号已经存在"),
	ES_4001("4001", "确认失败"),
	ES_5001("5001", "请选择需要导入的数据文件"),
	ES_6001("6001", "所选择的数据文件无内容"),
	ES_7001("7001", "批量导入EXCEL失败"),
	ES_8001("8001", "设备批次不存在,请确认设备批次内容"),
	ES_9001("9001", "导入内容存在设备编码为空,导入失败"),
	ES_9010("9010", "导入内容设备编码已存在"),
	ES_9011("9011", "审核失败"),
	ES_9012("9012", "设定失败"),
	ES_9013("9013", "操作失败"),
	ES_9014("9014", "该代理商被绑定使用，无法删除,若确定需要删除，请在【平台设置】中取消所有该代理商的数据。"),
	ES_9015("9015", "该比例已被绑定使用，无法删除,若确定需要删除，请在【设备信息】中取消所有该分润比例数据。"),
	ES_0000("0000", "接口异常");
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
