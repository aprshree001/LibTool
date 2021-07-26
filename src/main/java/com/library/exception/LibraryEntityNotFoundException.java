package com.library.exception;

public class LibraryEntityNotFoundException extends RuntimeException {

	public LibraryEntityNotFoundException(String errorMessage) {
		
		super(errorMessage);
		
	}

}
