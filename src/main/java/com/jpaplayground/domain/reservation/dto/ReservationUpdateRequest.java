package com.jpaplayground.domain.reservation.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ReservationUpdateRequest {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss")
	private LocalDateTime timeToMeet;

}
