package com.tracker.sgi.exception;

public class InvalidStockMovementException extends RuntimeException {
	public InvalidStockMovementException(String message) {
		super(message);
	}
}
