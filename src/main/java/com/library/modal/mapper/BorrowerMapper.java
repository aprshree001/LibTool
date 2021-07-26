package com.library.modal.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.library.dao.UserRepo;
import com.library.modal.BorrowEntity;
import com.library.utility.SecurityContextUtil;

import dto.BorrowRequestDto;

@Component
public class BorrowerMapper {

	@Autowired
	UserRepo userRepo;

	public BorrowEntity toBorrow(BorrowRequestDto requestDto) {

		BorrowEntity borrow = new BorrowEntity();
		borrow.setBookId(requestDto.getBookid());
		borrow.setUserId(userRepo.findByEmail(SecurityContextUtil.getUserNameFromContext()).getUserId());
		borrow.setNoOfBook(requestDto.getQuantity());
		return borrow;

	}

}
