package com.qt.air.cleaner.market.domain.platform;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PS_SHARE_PROFIT")
public class ShareProfit implements Serializable{
		private static final long serialVersionUID = 5670288974528865542L;
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
		private String id = null;
		/**
		 * 分润比例
		 */
		@Column(name = "SCALE", precision = 2, nullable = true, insertable = true)
		private Float scale = 0.00f;
		
		/**
		 * 手续费
		 */
		@Column(name = "FREE", precision = 2, nullable = true, insertable = true)
		private Float free = 0.00f;
		
		/**
		 * 分润对象
		 */
		@Column(name = "TYPE")
		private String type;

		public String getId() {
			return id;
		}

		public Float getScale() {
			return scale;
		}

		public Float getFree() {
			return free;
		}

		public String getType() {
			return type;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setScale(Float scale) {
			this.scale = scale;
		}

		public void setFree(Float free) {
			this.free = free;
		}

		public void setType(String type) {
			this.type = type;
		}
}
