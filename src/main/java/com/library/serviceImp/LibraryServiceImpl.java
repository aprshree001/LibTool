package com.library.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dao.LibraryReo;
import com.library.modal.BookEntity;
import com.library.service.libraryService;


@Service
public class LibraryServiceImpl implements libraryService {

	@Autowired
	LibraryReo librepo;

	public List<BookEntity> getAllBook(Integer lib_id) {

		List<BookEntity> responselist = librepo.findAllBookByLibId(lib_id);

		return responselist;

	}

}
