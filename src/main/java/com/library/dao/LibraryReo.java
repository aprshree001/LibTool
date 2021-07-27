package com.library.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.library.modal.BookEntity;
import com.library.modal.Library;


@RepositoryRestResource(path = "Library" )
public interface LibraryReo extends CrudRepository<Library, Integer> {


}
