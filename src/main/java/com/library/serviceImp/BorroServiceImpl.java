package com.library.serviceImp;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.constants.LimitKeyDefination;
import com.library.dao.BookRepo;
import com.library.dao.BorrowRepo;
import com.library.dao.LimitRepository;
import com.library.dao.UserRepo;
import com.library.exception.LibraryEntityNotFoundException;
import com.library.exception.LimitViolationException;
import com.library.exception.OutOfStockException;
import com.library.modal.BookEntity;
import com.library.modal.BorrowEntity;
import com.library.modal.Limit;
import com.library.modal.mapper.BorrowerMapper;
import com.library.service.BorrowServiceInterface;
import com.library.utility.SecurityContextUtil;

import dto.BorrowRequestDto;

@Service
public class BorroServiceImpl implements BorrowServiceInterface {

	@Autowired
	BorrowRepo borrowRepo;

	@Autowired
	BookRepo bookRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	BookServiceImpl bookService;

	@Autowired
	LimitRepository limitRepository;

	@Autowired
	BorrowerMapper borrowMapper;

	@Transactional
	public BorrowEntity addBookToBorrowItems(BorrowRequestDto borrowRequestDto) {

		BookEntity book = findIfBookExist(borrowRequestDto.getBookid());

		if (book.getNoOfCopies() < borrowRequestDto.getQuantity()) {
			throw new OutOfStockException("Insufficient no of books copies available");
		}

		Optional<BorrowEntity> borrowEntityOptional = borrowRepo.findByUserIdAndBookId(
				
				userRepo.findByEmail(SecurityContextUtil.getUserNameFromContext()).getUserId(),
				borrowRequestDto.getBookid()
				
				);
		BorrowEntity borrowEntity;
		if (borrowEntityOptional.isPresent()) {
			borrowEntity = borrowEntityOptional.get();

			validateBorrowLimit(borrowRequestDto.getQuantity(),
					borrowEntity.getNoOfBook() + borrowRequestDto.getQuantity(),
					SecurityContextUtil.getUserNameFromContext());

			borrowEntity.setNoOfBook(borrowEntity.getNoOfBook() + borrowRequestDto.getQuantity());

		} else {
			validateBorrowLimit(borrowRequestDto.getQuantity(), borrowRequestDto.getQuantity(), SecurityContextUtil.getUserNameFromContext());
			borrowEntity = borrowMapper.toBorrow(borrowRequestDto);
		}

		book.setNoOfCopies(book.getNoOfCopies() - borrowRequestDto.getQuantity());
		bookRepo.save(book);
		return borrowRepo.save(borrowEntity);

	}

	@Transactional
	@Override
	public void returnBookFromBorrowedItems(BorrowRequestDto borrowRequestDto) {

		BookEntity book = findIfBookExist(borrowRequestDto.getBookid());
		Optional<BorrowEntity> borrowEntityOptional = borrowRepo.findByUserIdAndBookId(
				userRepo.findByEmail(SecurityContextUtil.getUserNameFromContext()).getUserId(),
				borrowRequestDto.getBookid());

		if (!borrowEntityOptional.isPresent())
			throw new LibraryEntityNotFoundException("User did not reserve this book");

		if (borrowRequestDto.getQuantity() > borrowEntityOptional.get().getNoOfBook())
			throw new LimitViolationException("User returning more books then he already borrowed");

		if (borrowEntityOptional.get().getNoOfBook() - borrowRequestDto.getQuantity() < 1) {
			borrowRepo.removeBorrowBookFromUser(
					userRepo.findByEmail(SecurityContextUtil.getUserNameFromContext()).getUserId(),
					borrowRequestDto.getBookid());

		} else {
			BorrowEntity borrowEntity = borrowEntityOptional.get();
			borrowEntity.setNoOfBook(borrowEntityOptional.get().getNoOfBook() - borrowRequestDto.getQuantity());
			borrowRepo.save(borrowEntity);
		}

		book.setNoOfCopies(book.getNoOfCopies() + borrowRequestDto.getQuantity());
		bookRepo.save(book);

	}

	private BookEntity findIfBookExist(String bookId) {
		Optional<BookEntity> OptionalBook = bookRepo.findById(bookId);
		if (!OptionalBook.isPresent())
			throw new LibraryEntityNotFoundException("Book not found");
		return OptionalBook.get();
	}

	private void validateBorrowLimit(Integer quantity, Integer alreadyBorrowedBooks, String userName) {
		Limit noOfBookCopiesLimit = limitRepository.findByLimitName(LimitKeyDefination.BOOK_COPIES_BORROW_LIMIT.name());
		if (alreadyBorrowedBooks > noOfBookCopiesLimit.getLimitValue()) {
			throw new LimitViolationException("User Already has crossed the limit-name : [ "
					+ LimitKeyDefination.BOOK_COPIES_BORROW_LIMIT.name() + " ] limit description : [ "
					+ LimitKeyDefination.BOOK_COPIES_BORROW_LIMIT.getMessage() + " ]");
		}

		Limit maxBookBorrowAllowed = limitRepository.findByLimitName(LimitKeyDefination.MAX_BOOK_BORROW_LIMIT.name());

		List<BorrowEntity> borrowedItemsList = borrowRepo
				.findBorrowByUserId(userRepo.findByEmail(userName).getUserId());

		if (borrowedItemsList != null && borrowedItemsList.size()+alreadyBorrowedBooks> maxBookBorrowAllowed.getLimitValue()) {
			throw new LimitViolationException("User Already has crossed the limit-name : [ "
					+ LimitKeyDefination.MAX_BOOK_BORROW_LIMIT.name() + " ] limit description : [ "
					+ LimitKeyDefination.MAX_BOOK_BORROW_LIMIT.getMessage() + " ]");
		}
	}

	@Override
	public List<BorrowEntity> findByUserId(String userId) {

		List<BorrowEntity> userborrowlist = borrowRepo.findByUserId(userId);

		return userborrowlist;
	}

}
