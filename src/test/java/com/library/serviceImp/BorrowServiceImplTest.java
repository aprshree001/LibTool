package com.library.serviceImp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.dao.BookRepo;
import com.library.dao.BorrowRepo;
import com.library.dao.LimitRepository;
import com.library.dao.UserRepo;
import com.library.exception.LimitViolationException;
import com.library.exception.OutOfStockException;
import com.library.modal.BookEntity;
import com.library.modal.BorrowEntity;
import com.library.modal.Limit;
import com.library.modal.UserDetail;
import com.library.modal.mapper.BorrowerMapper;

import dto.BorrowRequestDto;

@ExtendWith(MockitoExtension.class)
public class BorrowServiceImplTest {

	@Mock
	BookRepo bookRepo;

	@Mock
	BorrowerMapper borrowMapper;

	@Mock
	BorrowRepo borrowRepo;

	@Mock
	UserRepo userRepo;

	@Mock
	LimitRepository limitRepository;

	@InjectMocks
	private BorroServiceImpl borrowserviceimpl;

	@Test
	public void addBookToBorrowItemsTest() {

		BookEntity bookEntity = new BookEntity();
		bookEntity.setAuthorName("Dummy");
		bookEntity.setBookId("121");
		bookEntity.setLibId(1);
		bookEntity.setNoOfCopies(2);

		when(bookRepo.findById("121")).thenReturn(Optional.of(bookEntity));
		BorrowRequestDto borrowRequestDto2 = new BorrowRequestDto();
		borrowRequestDto2.setBookid("121");

		UserDetail userDetails = new UserDetail();
		userDetails.setEmail("tempUser@gmail.com");
		when(userRepo.findByEmail(anyString())).thenReturn(userDetails);
		BorrowEntity borrowEntity = new BorrowEntity();

		borrowEntity.setBookId(borrowRequestDto2.getBookid());
		borrowEntity.setNoOfBook(borrowRequestDto2.getQuantity());

		when(borrowMapper.toBorrow(borrowRequestDto2)).thenReturn(borrowEntity);

		Limit limit = new Limit();
		limit.setLimitValue(2);
		when(limitRepository.findByLimitName(anyString())).thenReturn(limit);

		when(bookRepo.save(bookEntity)).thenReturn(bookEntity);

		when(borrowRepo.save(borrowEntity)).thenReturn(borrowEntity);

		borrowserviceimpl.addBookToBorrowItems(borrowRequestDto2);

		verify(bookRepo, times(1)).save(bookEntity);
		verify(borrowRepo, times(1)).save(borrowEntity);
	}

	@Test()
	public void addBookToBorrowItemsTest_WhenOutOfStock() {

		BookEntity bookEntity = new BookEntity();
		bookEntity.setAuthorName("Dummy");
		bookEntity.setBookId("121");
		bookEntity.setLibId(1);
		bookEntity.setNoOfCopies(0);

		when(bookRepo.findById("121")).thenReturn(Optional.of(bookEntity));
		BorrowRequestDto borrowRequestDto2 = new BorrowRequestDto();
		borrowRequestDto2.setBookid("121");
		try {

			borrowserviceimpl.addBookToBorrowItems(borrowRequestDto2);
		} catch (Exception e) {
			assertTrue(e instanceof OutOfStockException);
		}

	}

	@Test
	public void addBookToBorrowItemsLimitVoilated() {

		BookEntity bookEntity = new BookEntity();
		bookEntity.setAuthorName("Dummy");
		bookEntity.setBookId("121");
		bookEntity.setLibId(1);
		bookEntity.setNoOfCopies(2);

		when(bookRepo.findById("121")).thenReturn(Optional.of(bookEntity));
		BorrowRequestDto borrowRequestDto2 = new BorrowRequestDto();
		borrowRequestDto2.setBookid("121");

		UserDetail userDetails = new UserDetail();
		userDetails.setEmail("tempUser@gmail.com");
		when(userRepo.findByEmail(anyString())).thenReturn(userDetails);
		BorrowEntity borrowEntity = new BorrowEntity();

		borrowEntity.setBookId(borrowRequestDto2.getBookid());
		borrowEntity.setNoOfBook(borrowRequestDto2.getQuantity());

		when(borrowMapper.toBorrow(borrowRequestDto2)).thenReturn(borrowEntity);

		Limit limit = new Limit();
		limit.setLimitValue(0);
		when(limitRepository.findByLimitName(anyString())).thenReturn(limit);

		when(bookRepo.save(bookEntity)).thenReturn(bookEntity);

		when(borrowRepo.save(borrowEntity)).thenReturn(borrowEntity);

		try {
			borrowserviceimpl.addBookToBorrowItems(borrowRequestDto2);

		} catch (Exception e) {

			assertTrue(e instanceof LimitViolationException);
	
			verify(limitRepository, times(1)).findByLimitName(anyString());

		}

	}

}
