package com.library.constants;

public enum LimitKeyDefination {

	BOOK_COPIES_BORROW_LIMIT("NO OF COPIES OF SAME BOOKS THAT CAN BE BORROWED "),
	MAX_BOOK_BORROW_LIMIT(" MAXIMUM NUMBER OF BOOK THAT CAN BE BORROWED");

	private String message;

	LimitKeyDefination(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
