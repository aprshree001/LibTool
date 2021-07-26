package com.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.library.modal.UserDetail;


@RepositoryRestResource(path = "UserDetail" )
public interface UserRepo extends CrudRepository<UserDetail, String> {

	UserDetail findByEmail(String email);

}
