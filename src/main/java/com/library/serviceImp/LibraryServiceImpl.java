package com.library.serviceImp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dao.LibraryReo;
import com.library.modal.BookEntity;
import com.library.modal.Library;
import com.library.service.libraryService;

@Service
public class LibraryServiceImpl implements libraryService {

	@Autowired
	LibraryReo librepo;

	public List<BookEntity> getAllBook(Integer lib_id) {

		Optional<Library> response = librepo.findById(lib_id);
		List<BookEntity> booklist = response.get().getBooks().stream().filter(e -> e.getNoOfCopies() > 0)
				.collect(Collectors.toList());

		return booklist;

	}

}
