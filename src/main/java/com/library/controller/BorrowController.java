package com.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.library.dao.BorrowRepo;
import com.library.dao.UserRepo;
import com.library.modal.BorrowEntity;
import com.library.service.BorrowServiceInterface;
import com.library.utility.SecurityContextUtil;

import dto.BorrowRequestDto;

@RepositoryRestController
@RequestMapping("/api/borrow")
public class BorrowController {

	@Autowired
	BorrowServiceInterface borrowService;

	@Autowired
	BorrowRepo borrowRepository;

	@Autowired
	UserRepo userRep;

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/addBook")
	public ResponseEntity<BorrowEntity> addBook(@RequestBody BorrowRequestDto borrowRequest) {
	
		BorrowEntity response = borrowService.addBookToBorrowItems(borrowRequest);
		return new ResponseEntity<BorrowEntity>(response, HttpStatus.OK);
	
	}

	@PreAuthorize("hasRole('USER')")
	@GetMapping("/myborrowedItems")
	public ResponseEntity<List<BorrowEntity>> getBookBorrowedByMe() {

		List<BorrowEntity> response = borrowService
				.findByUserId(userRep.findByEmail(SecurityContextUtil.getUserNameFromContext()).getUserId());

		return new ResponseEntity<List<BorrowEntity>>(response, HttpStatus.OK);

	}

	@PreAuthorize("hasRole('USER')")
	@PostMapping("/returnbook")
	public ResponseEntity returnBook(@RequestBody BorrowRequestDto bookrequest) {
		borrowService.returnBookFromBorrowedItems(bookrequest);
		return new ResponseEntity(HttpStatus.OK);
	}

}
