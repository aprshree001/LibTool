package com.library.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.dao.RoleRepository;
import com.library.modal.Role;
import com.library.service.RoleService;


@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {


	    @Autowired
	    private RoleRepository roleDao;

	    @Override
	    public Role findByName(String name) {
	        Role role = roleDao.findByName(name);
	        return role;
	    }

}
