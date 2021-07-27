package com.library.main;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.library.controller.BookController;
import com.library.dao.ApiError;
import com.library.modal.BookEntity;
import com.library.modal.BorrowEntity;
import com.library.modal.LoginResponse;
import com.library.modal.LoginUser;

import dto.BorrowRequestDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class e2eTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String url = "http://localhost:";

	@Autowired
	private BookController controller;

	@Test
	void contextLoads() {

		assertThat(controller).isNotNull();

	}

	private void reserveBook(String bookId, HttpHeaders header) {
		BorrowRequestDto borrowRequest = new BorrowRequestDto();

		borrowRequest.setBookid(bookId);
		HttpEntity<BorrowRequestDto> borrowRequesthttpEntity = new HttpEntity<BorrowRequestDto>(borrowRequest, header);
		ResponseEntity<BorrowEntity> response = restTemplate.postForEntity(createURLWithPort("/api/borrow/addBook"),
				borrowRequesthttpEntity, BorrowEntity.class);

		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(response.getBody().getBookId(), bookId);
		ResponseEntity<List<BorrowEntity>> getBorrowList = getMyBorrowCart(header);
		assertTrue(getBorrowList.getBody().contains(response.getBody()));

	}

	private BookEntity getBookById(String bookId, HttpHeaders headers) {

		ResponseEntity<BookEntity> response = restTemplate.exchange(createURLWithPort("/api/books/" + bookId),
				HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<BookEntity>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		return response.getBody();
	}

	@Test
	void test_GivenWhenUserRequestBorowBook_thenAddedInBorrowList() {

		HttpHeaders header = getSignedTokenHeader("aparna@gmail.com", "Hello");

		reserveBook("123", header);

	}

	@Test
	void test_GivenBooknotAvaible_WhenUserRequestBook_thenBookNotFound() {

		HttpHeaders header = getSignedTokenHeader("amitkumar@gmail.com", "Hello");
		BorrowRequestDto borrowRequest = new BorrowRequestDto();

		borrowRequest.setBookid("128");

		HttpEntity<BorrowRequestDto> borrowRequesthttpEntity = new HttpEntity<BorrowRequestDto>(borrowRequest, header);

		ResponseEntity<ApiError> response = restTemplate.postForEntity(createURLWithPort("/api/borrow/addBook"),
				borrowRequesthttpEntity, ApiError.class);

		assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
		assertEquals(response.getBody().getMessage(), "Book not found");

	}

	@Test
	void test_GivenMultipleBookCopies_WhenUserRequestMoreThanOneCopy_ThenLimitViolation() {

		HttpHeaders header = getSignedTokenHeader("amitkumar@gmail.com", "Hello");

		reserveBook("121", header);

		BorrowRequestDto borrowRequestSecond = new BorrowRequestDto();
		borrowRequestSecond.setBookid("121");

		HttpEntity<BorrowRequestDto> borrowRequesthttpEntitySecond = new HttpEntity<BorrowRequestDto>(
				borrowRequestSecond, header);

		ResponseEntity<ApiError> responseMultipleCopyBorrow = restTemplate
				.postForEntity(createURLWithPort("/api/borrow/addBook"), borrowRequesthttpEntitySecond, ApiError.class);

		assertEquals(responseMultipleCopyBorrow.getBody().getMessage(),
				"User Already has crossed the limit-name : " + "[ BOOK_COPIES_BORROW_LIMIT ] "
						+ "limit description : [ NO OF COPIES OF SAME BOOKS THAT CAN BE BORROWED  ]");

	}

	@Test
	void test_GivenMultipleBookCopies_WhenUserRequestmoreThanTwoDistinctBook_ThenLimitVioltion() {

		HttpHeaders header = getSignedTokenHeader("abhijeet@gmail.com", "Hello");
		reserveBook("121", header);

		reserveBook("122", header);

		BorrowRequestDto borrowRequestThird = new BorrowRequestDto();

		borrowRequestThird.setBookid("120");

		HttpEntity<BorrowRequestDto> borrowRequesthttpEntitythird = new HttpEntity<BorrowRequestDto>(borrowRequestThird,
				header);

		ResponseEntity<ApiError> responseAddingThirdBook = restTemplate
				.postForEntity(createURLWithPort("/api/borrow/addBook"), borrowRequesthttpEntitythird, ApiError.class);

		assertEquals(responseAddingThirdBook.getBody().getMessage(),
				"User Already has crossed the limit-name : " + "[ MAX_BOOK_BORROW_LIMIT ] limit description : "
						+ "[  MAXIMUM NUMBER OF BOOK THAT CAN BE BORROWED ]");

	}

	@Test
	void test_GivenbWhenUserReturnBook_thenRemoveBookFromBorrowListAndAddCopyToLibrary() {

		HttpHeaders header = getSignedTokenHeader("amitkumar@gmail.com", "Hello");

		int initalnumberofQuantity = getBookById("125", header).getNoOfCopies();

		reserveBook("125", header);

		assertEquals(getBookById("125", header).getNoOfCopies(), initalnumberofQuantity - 1);

		BorrowRequestDto returnBookRequest = new BorrowRequestDto();
		returnBookRequest.setBookid("125");

		HttpEntity<BorrowRequestDto> returnBookEntity = new HttpEntity<BorrowRequestDto>(returnBookRequest, header);

		ResponseEntity responseReturnBook = restTemplate.postForEntity(createURLWithPort("/api/borrow/returnbook"),
				returnBookEntity, null);

		ResponseEntity<List<BorrowEntity>> getBorrowList = getMyBorrowCart(header);
		assertFalse(getBorrowList.getBody().contains(returnBookEntity.getBody()));

		assertEquals(HttpStatus.OK, responseReturnBook.getStatusCode());

		assertEquals(getBookById("125", header).getNoOfCopies(), initalnumberofQuantity);

	}

	@Test
	void test_GivenWhenUserAddLastBook_thenRemoveBookFromLibrary() {

		HttpHeaders header = getSignedTokenHeader("amitkumar@gmail.com", "Hello");

		int initalnumberofQuantity = getBookById("126", header).getNoOfCopies();

		reserveBook("126", header);

		assertEquals(getBookById("126", header).getNoOfCopies(), initalnumberofQuantity - 1);

		ResponseEntity<List<BookEntity>> response = restTemplate.exchange(createURLWithPort("/api/library/books/1"),
				HttpMethod.GET, new HttpEntity(header), new ParameterizedTypeReference<List<BookEntity>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertFalse(response.getBody().contains(getBookById("126", header)));

	}

	private ResponseEntity<List<BorrowEntity>> getMyBorrowCart(HttpHeaders header) {
		ResponseEntity<List<BorrowEntity>> responseMyBorrowedItem = restTemplate.exchange(
				createURLWithPort("/api/borrow/myborrowedItems"), HttpMethod.GET, new HttpEntity<>(header),
				new ParameterizedTypeReference<List<BorrowEntity>>() {
				});
		return responseMyBorrowedItem;
	}

	public HttpHeaders getSignedTokenHeader(String userName, String password) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + getSsoAccessToken(userName, password));
		return headers;
	}

	public String getSsoAccessToken(String username, String password) {

		LoginUser loginUser = new LoginUser();
		loginUser.setPassword(password);
		loginUser.setUsername(username);

		HttpEntity<LoginUser> userLogin = new HttpEntity<LoginUser>(loginUser);
		ResponseEntity<LoginResponse> response = restTemplate.exchange(createURLWithPort("/api/users/authenticate"),
				HttpMethod.POST, userLogin, new ParameterizedTypeReference<LoginResponse>() {
				});

		return response.getBody().getToken();
	}

	private String createURLWithPort(String urlstring) {

		return url + port + urlstring;
	}

}
