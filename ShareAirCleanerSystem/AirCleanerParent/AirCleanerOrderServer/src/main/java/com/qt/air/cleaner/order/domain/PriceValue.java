
package com.qt.air.cleaner.order.domain;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.qt.air.cleaner.base.domain.GenericEntity;



@Entity
@Table(name = "PS_PRICE_VALUE")
public class PriceValue  extends GenericEntity  {

	private static final long serialVersionUID = 2649856566192339463L;

	@Column(name = "COST_TIME")
	private Integer costTime;

	@Column(name = "VALUE", precision = 2)
	private Float value;

	@Column(name = "DISCOUNT", precision = 2)
	private Float discount;

	@Column(name = "ACTIVE_START_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date activeStartTime;

	@Transient
	private String activeStartTimeStr;

	@Column(name = "ACTIVE_END_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Date activeEndTime;

	@Transient
	private String activeEndTimeStr;

	@OneToOne(fetch = FetchType.LAZY)
	private PriceModel priceModel;

	@Transient
	private String priceModelId;

	public Integer getCostTime() {

		return costTime;
	}

	public void setCostTime(Integer costTime) {

		this.costTime = costTime;
	}

	public Float getValue() {

		return value;
	}

	public void setValue(Float value) {

		this.value = value;
	}

	public Float getDiscount() {

		return discount;
	}

	public void setDiscount(Float discount) {

		this.discount = discount;
	}

	public Date getActiveStartTime() {

		return activeStartTime;
	}

	public void setActiveStartTime(Date activeStartTime) {

		this.activeStartTime = activeStartTime;
	}

	public Date getActiveEndTime() {

		return activeEndTime;
	}

	public void setActiveEndTime(Date activeEndTime) {

		this.activeEndTime = activeEndTime;
	}

	public PriceModel getPriceModel() {

		return priceModel;
	}

	public void setPriceModel(PriceModel priceModel) {

		this.priceModel = priceModel;
	}

	public Float getRealValue() {

		Date currentTime = Calendar.getInstance().getTime();
		if (discount != null && discount > 0 && discount < 1 && currentTime.before(this.getActiveEndTime())
				&& currentTime.after(this.getActiveStartTime())) {
			BigDecimal bigDeciaml = new BigDecimal(value * discount);
			return bigDeciaml.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		} else {
			return value;
		}
	}

	public String getActiveStartTimeStr() {

		return activeStartTimeStr;
	}

	public void setActiveStartTimeStr(String activeStartTimeStr) {

		this.activeStartTimeStr = activeStartTimeStr;
	}

	public String getActiveEndTimeStr() {

		return activeEndTimeStr;
	}

	public void setActiveEndTimeStr(String activeEndTimeStr) {

		this.activeEndTimeStr = activeEndTimeStr;
	}

	public String getPriceModelId() {

		return priceModelId;
	}

	public void setPriceModelId(String priceModelId) {

		this.priceModelId = priceModelId;
	}

}
