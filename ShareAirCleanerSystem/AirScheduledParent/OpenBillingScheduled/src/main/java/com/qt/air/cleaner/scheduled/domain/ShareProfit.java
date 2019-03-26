package com.qt.air.cleaner.scheduled.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PS_SHARE_PROFIT")
public class ShareProfit implements Serializable{
		private static final long serialVersionUID = 5670288974528865542L;
		//代理
		public final static String ACCOUNT_TYPE_AGENT_DL = "DL";
		//区域总代
		public final static String ACCOUNT_TYPE_AGENT_ZD = "ZD";
		//商家
		public final static String ACCOUNT_TYPE_TRADER = "TR";
		//投资商
		public final static String ACCOUNT_TYPE_INVESTOR = "IR";
		//公司
		public final static String ACCOUNT_TYPE_COMPANY = "CY";
		//促销员
		public final static String ACCOUNT_TYPE_SALER = "SR";
		//消费者
		public final static String ACCOUNT_TYPE_CUSTOMER = "CR";
		@Id
		@GeneratedValue(generator = "guid")
		@GenericGenerator(name = "guid", strategy = "guid")
		private String id;
		/**
		 * 分润比例
		 */
		@Column(name = "SCALE", precision = 2, nullable = true, insertable = true)
		private Float scale = 0.00f;
		
		/**
		 * 手续费
		 */
		@Column(name = "FREE", precision = 2, nullable = true, insertable = true)
		private Float free = 0.00f;;
		
		/**
		 * 名称
		 */
		@Column(name = "NAME",  length = 32, nullable = true, insertable = true)
		private String name ;
		
		/**
		 * 所属上级
		 */
		@Column(name = "PID",  length = 32, nullable = true, insertable = true)
		private String pid ;
		
		/**
		 * 代理商ID
		 */
		@Transient
		private String agentId ;
		@Column(name = "AGENT_ID",  length = 32, nullable = true, insertable = true)
		private String agent_id;
		
		public String getAgent_id() {
			return agent_id;
		}

		public void setAgent_id(String agent_id) {
			this.agent_id = agent_id;
		}

		/**
		 * 分润对象
		 */
		@Column(name = "TYPE")
		private String type;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Float getScale() {
			return scale;
		}

		public void setScale(Float scale) {
			this.scale = scale;
		}

		public Float getFree() {
			return free;
		}

		public void setFree(Float free) {
			this.free = free;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAgentId() {
			return this.getAgent_id();
		}

		

}
