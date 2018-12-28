package com.qt.air.cleaner.domain;

import java.io.Serializable;

public interface BaseEntity<E extends Serializable> extends Serializable {
	public E getId();
}
