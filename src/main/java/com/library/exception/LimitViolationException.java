package com.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class LimitViolationException extends RuntimeException {
	
	
	public LimitViolationException(String errorMessage) {
		
		super(errorMessage);
		
	}

}
