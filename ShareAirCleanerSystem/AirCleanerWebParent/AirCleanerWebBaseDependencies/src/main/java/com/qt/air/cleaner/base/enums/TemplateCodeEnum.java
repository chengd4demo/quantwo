package com.qt.air.cleaner.base.enums;

public enum TemplateCodeEnum {
	TEMP_SIGN("sign", "SMS_159771420", "身份校验"), 
	TEMP_CHANGE_PHONE("chagePhone", "SMS_159781441","手机号码变更校验"), 
	TEMP_CHANGE_APPLY("changeApply", "SMS_159771477", "修改密码校验"),
	TEMP_DEFAULT("default", "SMS_159771253", "默认");
	private TemplateCodeEnum(String type, String templateCode, String desc) {
		this.type = type;
		this.templateCode = templateCode;
		this.desc = desc;
	}

	/** 短信消息类型 */
	private String type;

	/** 短信消息Code */
	private String templateCode;
	
	/** 短信消息说明 */
	private String desc;

	public String getType() {
		return type;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
