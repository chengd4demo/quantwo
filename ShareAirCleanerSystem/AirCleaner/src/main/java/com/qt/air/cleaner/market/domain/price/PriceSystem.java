
package com.qt.air.cleaner.market.domain.price;

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
import com.singalrain.framework.core.bo.GenericEntity;

@Entity
@Table(name = "PS_PRICE_SYSTEM")
public class PriceSystem extends GenericEntity {
	
	private static final long serialVersionUID = 3941769078518791358L;
	
	@Transient
	// 新增的价格体系管理状态
	public static final Integer PRICE_SYSTEM_NEW = 0;
	
	@Transient
	// 新增的价格体系管理状态
	public static final Integer PRICE_SYSTEM_CONFIRM = 1;
	
	@Column(name = "NAME", length = 255, nullable = false)
	private String name;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private PriceModel activeModel;
	
	@Transient
	private String priceModeId;
	
	@Column(name = "STATE")
	private Integer state;
	
	@Transient
	private String priceModelName;
	
	@Transient
	private String priceModelDescription;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = {
	        CascadeType.ALL
	}, mappedBy = "priceSystem")
	@JsonIgnore
	private Set<PriceModel> models;
		
	@Column(name = "DESCRIPTION", length = 4000)
	private String description;
	
	public String getName() {
		
		return name;
	}
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	public PriceModel getActiveModel() {
		
		return activeModel;
	}
	
	public void setActiveModel(PriceModel activeModel) {
		
		this.activeModel = activeModel;
	}
	
	public Set<PriceModel> getModels() {
		
		return models;
	}
	
	public void setModels(Set<PriceModel> models) {
		
		this.models = models;
	}
	
	public String getDescription() {
		
		return description;
	}
	
	public void setDescription(String description) {
		
		this.description = description;
	}
	
	public String getPriceModeId() {
		
		String priceModeId = "";
		if (this.getActiveModel() != null) {
			priceModeId = this.getActiveModel().getId();
		}
		return priceModeId;
	}
	
	public String getPriceModelName() {
		
		String priceModelName = "";
		if (this.getActiveModel() != null) {
			priceModelName = this.getActiveModel().getName();
		}
		return priceModelName;
	}
	
	public String getPriceModelDescription() {
		
		String priceModelDescription = "";
		if (this.getActiveModel() != null) {
			priceModelDescription = this.getActiveModel().getDescription();
		}
		return priceModelDescription;
	}

	@Override
	public String toString() {
		return "PriceSystem [name=" + name + ", activeModel=" + activeModel + ", priceModeId=" + priceModeId
				+ ", state=" + state + ", priceModelName=" + priceModelName + ", priceModelDescription="
				+ priceModelDescription + ", models=" + models + ", description=" + description + "]";
	}
	
	
}
