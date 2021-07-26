package com.library.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import com.library.modal.BookEntity;

@RepositoryRestResource(path = "books")
public interface BookRepo extends JpaRepository<BookEntity, String>, JpaSpecificationExecutor<BookEntity> {

	@PreAuthorize("hasRole('USER')")
	@Override
	List<BookEntity> findAllById(Iterable<String> ids);

	@PreAuthorize("hasRole('USER')")
	List<BookEntity> findByTitle(String title);

	@PreAuthorize("hasRole('USER')")
	public List<BookEntity> findByAuthorName(String authorName);

	@PreAuthorize("hasRole('USER')")
	@Override
	public Optional<BookEntity> findById(String id);

	@RestResource(exported = true)
	@PreAuthorize("hasRole('USER')")
	public List<BookEntity> findAll();

}