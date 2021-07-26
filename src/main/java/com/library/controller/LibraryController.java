package com.library.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.library.dao.LibraryReo;
import com.library.modal.BookEntity;
import com.library.serviceImp.LibraryServiceImpl;

@RestController
public class LibraryController {

	@Autowired
	LibraryReo librepo;

	@Autowired
	LibraryServiceImpl libraryService;

	@GetMapping("/lib/{libId}")
	@ResponseBody
	public ResponseEntity<List<BookEntity>> getBooks(@PathVariable("libId") Integer libId) {

		List<BookEntity> listofbook = new ArrayList<BookEntity>();
		listofbook = libraryService.getAllBook(libId).stream().filter(book -> book.getNoOfCopies() > 0)
				.collect(Collectors.toList());
		if (listofbook.isEmpty()) {
			return new ResponseEntity<List<BookEntity>>(listofbook, HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<List<BookEntity>>(listofbook, HttpStatus.OK);
		}

	}

}
