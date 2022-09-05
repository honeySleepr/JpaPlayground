package com.jpaplayground.common;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

	protected Boolean deleted = false;

	public void delete(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean isDeleted() {
		return deleted;
	}
}
