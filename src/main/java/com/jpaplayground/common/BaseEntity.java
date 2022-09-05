package com.jpaplayground.common;

import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

	protected boolean deleted = false;

	public void delete() {
		this.deleted = true;
	}
}
