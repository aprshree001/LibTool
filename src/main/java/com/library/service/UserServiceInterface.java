package com.library.service;

import com.library.exception.BadRequestException;
import com.library.modal.UserDetail;

public interface UserServiceInterface {

	UserDetail save(UserDetail user);

	UserDetail findOne(String username);

}
