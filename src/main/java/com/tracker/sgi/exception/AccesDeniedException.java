package com.tracker.sgi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccesDeniedException extends RuntimeException {
	public AccesDeniedException(String message) {
		super(message);
	}
}
