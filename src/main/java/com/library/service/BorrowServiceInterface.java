package com.library.service;


import java.util.List;

import com.library.modal.BorrowEntity;

import dto.BorrowRequestDto;

public interface BorrowServiceInterface {

	public BorrowEntity addBookToBorrowItems(BorrowRequestDto userrequest);
	public void returnBookFromBorrowedItems(BorrowRequestDto userrequest);
	public List<BorrowEntity> findByUserId(String userId);

}
