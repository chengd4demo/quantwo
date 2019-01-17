
package com.qt.air.cleaner.scheduled.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qt.air.cleaner.base.domain.GenericEntity;

@Entity
@Table(name = "PS_PRICE_MODEL")
public class PriceModel extends GenericEntity {
	
	private static final long serialVersionUID = -5617491401753727023L;
	
	@Column(name = "NAME", length = 255)
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {
	        CascadeType.ALL
	}, mappedBy = "priceModel")
	@JsonIgnore
	private Set<PriceValue> values;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private PriceSystem priceSystem;
	
	@Transient
	private String priceSystemId;
	
	@Transient
	private String priceSystemName;
	
	@Transient
	private String value;
	
	@Column(name = "DESCRIPTION", length = 4000)
	private String description;
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public Set<PriceValue> getValues() {
		
		return values;
	}
	
	public void setValues(Set<PriceValue> values) {
		
		this.values = values;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public PriceSystem getPriceSystem() {
		
		return priceSystem;
	}
	
	public void setPriceSystem(PriceSystem priceSystem) {
		
		this.priceSystem = priceSystem;
	}
	
	public String getPriceSystemId() {
		
		String priceSystemId = "";
		if (this.getPriceSystem() != null) {
			priceSystemId = this.getPriceSystem().getId();
		}
		return priceSystemId;
	}
	
	public String getPriceSystemName() {
		
		String priceSystemName = "";
		if (this.getPriceSystem() != null) {
			priceSystemName = this.getPriceSystem().getName();
		}
		return priceSystemName;
	}
	
	public String getValue() {
		
		StringBuffer value = new StringBuffer();
		if (values != null && !values.isEmpty()) {
			String flag = "";
			for (PriceValue pValue : values) {
				value.append(flag);
				value.append("时长：" + pValue.getCostTime() + "分钟，");
				value.append("单价：" + pValue.getValue() + "元，");
				value.append("折扣：" + pValue.getDiscount() + "倍，");
				value.append("实价：" + pValue.getRealValue() + "元");
				flag = "<br>";
			}
		}
		return value.toString();
	}
	
}
