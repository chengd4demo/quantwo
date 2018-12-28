package com.qt.air.cleaner.market.vo.generic;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.market.domain.generic.Company;

public class CompanyView implements Serializable{
	private static final long serialVersionUID = -6244516323929341411L;
		private String id;
		private String socialCreditCode;
		private String name;
		private String address;
		private String legalPerson;
		private String phoneNumber;
		private String weixin;
		private String email;
		private String setupTime;
		private String legend;
		@JsonIgnore
		public SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		public CompanyView() {
			super();		
		}
		public CompanyView(Company company) {
				super();
				this.setId(company.getId());
				this.setSocialCreditCode(company.getSocialCreditCode());
				this.setName(company.getName());
				this.setAddress(company.getAddress());
				this.setLegalPerson(company.getLegalPerson());
				this.setPhoneNumber(company.getPhoneNumber());
				this.setWeixin(company.getWeixin());
				this.setEmail(company.getEmail());
				this.setSetupTime(company.getSetupTime());				
			}	
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSocialCreditCode() {
			return socialCreditCode;
		}
		public void setSocialCreditCode(String socialCreditCode) {
			this.socialCreditCode = socialCreditCode;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getLegalPerson() {
			return legalPerson;
		}
		public void setLegalPerson(String legalPerson) {
			this.legalPerson = legalPerson;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getWeixin() {
			return weixin;
		}
		public void setWeixin(String weixin) {
			this.weixin = weixin;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getSetupTime() {
			return setupTime;
		}
		public void setSetupTime(String setupTime) {
			this.setupTime = setupTime;
		}
		public String getLegend() {
			return legend;
		}
		public void setLegend(String legend) {
			this.legend = legend;
		}
		
		@Override
		public String toString() {
			return "CompanyView [id=" + id + ", socialCreditCode=" + socialCreditCode + ", name=" + name + ", address="
					+ address + ", legalPerson=" + legalPerson + ", phoneNumber=" + phoneNumber + ", weixin=" + weixin
					+ ", email=" + email + ", setupTime=" + setupTime + ", legend=" + legend + ", dateFormat="
					+ dateFormat + "]";
		}
		
	}
