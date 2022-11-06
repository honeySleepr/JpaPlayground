package com.jpaplayground.domain.reservation;

import com.jpaplayground.global.exception.BusinessException;
import com.jpaplayground.global.exception.ErrorCode;

public class ReservationException extends BusinessException {

	public ReservationException(ErrorCode errorCode) {
		super(errorCode);
	}
}
