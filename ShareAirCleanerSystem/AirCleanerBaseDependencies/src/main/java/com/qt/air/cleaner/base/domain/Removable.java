package com.qt.air.cleaner.base.domain;

import javax.persistence.Transient;

public interface Removable {
	@Transient
	Boolean isEntityRemoved();

	void setRemoved(Boolean arg0);
}