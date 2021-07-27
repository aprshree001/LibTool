package com.library.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.library.dao.BookRepo;
import com.library.dao.LibraryReo;
import com.library.exception.LibraryEntityNotFoundException;
import com.library.modal.BookEntity;
import com.library.modal.Library;
import com.library.service.BookServiceInterface;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RepositoryRestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	BookRepo bookRepository;

	@Autowired
	LibraryReo libraryrepo;

	@Autowired
	BookServiceInterface bookService;

	@PreAuthorize("hasRole('USER')")
	@GetMapping()
	public ResponseEntity<Page<BookEntity>> getBooks(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "3") int size) {

		log.trace(">> getBooks");
		Pageable paging = PageRequest.of(page, size);
		Page<BookEntity> response = bookRepository.findAll(paging);
		return new ResponseEntity<Page<BookEntity>>(response, HttpStatus.CREATED);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping()
	public ResponseEntity<BookEntity> addBook(@RequestBody BookEntity book) {
		log.trace(">> addBook");
		Optional<Library> library = libraryrepo.findById(book.getLibId());
		if (!library.isPresent()) {

			throw new LibraryEntityNotFoundException("Library id is wrong it doesnot exist");
		}
		BookEntity response = bookRepository.save(book);
		return new ResponseEntity<BookEntity>(response, HttpStatus.CREATED);
	}

}
