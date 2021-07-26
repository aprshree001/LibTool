package com.library.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.library.modal.Role;


@RepositoryRestResource(path = "Role" )
public interface RoleRepository extends CrudRepository<Role, Long> {

	public Role findByName(String name);

}
