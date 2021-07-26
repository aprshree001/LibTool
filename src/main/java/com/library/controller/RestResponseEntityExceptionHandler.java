package com.library.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.library.dao.ApiError;
import com.library.exception.BadRequestException;
import com.library.exception.LibraryEntityNotFoundException;
import com.library.exception.LimitViolationException;
import com.library.exception.OutOfStockException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(LimitViolationException.class)
	public ResponseEntity<ApiError> handleCityNotFoundException(LimitViolationException ex, WebRequest request) {

		ApiError apiError = new ApiError();
		apiError.setMessage(ex.getMessage());
		apiError.setReason("Limit voilated");
		return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(LibraryEntityNotFoundException.class)
	public ResponseEntity<ApiError> handleEntityNotFound(LibraryEntityNotFoundException ex, WebRequest request) {

		ApiError apiError = new ApiError();
		apiError.setMessage(ex.getMessage());
		apiError.setReason("Not found entity ");
		return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(OutOfStockException.class)
	public ResponseEntity<ApiError> handleOutOfStockException(OutOfStockException ex, WebRequest request) {

		ApiError apiError = new ApiError();
		apiError.setMessage(ex.getMessage());
		apiError.setReason("Insufficient stock");
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, WebRequest request) {

		ApiError apiError = new ApiError();
		apiError.setMessage(ex.getMessage());
		apiError.setReason("Bad Request");
		return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);

	}

}