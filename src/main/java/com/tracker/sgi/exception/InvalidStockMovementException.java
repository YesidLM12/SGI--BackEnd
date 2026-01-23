package com.tracker.sgi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidStockMovementException extends RuntimeException {
	public InvalidStockMovementException(String message) {
		super(message);
	}
}
