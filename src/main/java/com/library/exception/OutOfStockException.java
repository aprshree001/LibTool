package com.library.exception;

public class OutOfStockException extends RuntimeException {

	public OutOfStockException(String errorMessage) {

		super(errorMessage);

	}

}
