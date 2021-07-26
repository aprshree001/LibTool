package com.library.serviceImp;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.User;
import com.library.dao.UserRepo;
import com.library.exception.BadRequestException;
import com.library.modal.Role;
import com.library.modal.UserDetail;
import com.library.service.RoleService;
import com.library.service.UserServiceInterface;

@Service(value = "userService")
public class UserServiceImpl implements UserServiceInterface, UserDetailsService {

	@Autowired
	UserRepo userRepo;
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	private RoleService roleService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserDetail user = userRepo.findByEmail(username);

		User userSpringContext = new User(user.getEmail(), user.getPassword(), getAuthority(user));
		return userSpringContext;
	}

	private Set<SimpleGrantedAuthority> getAuthority(UserDetail userdetail) {
		if (userdetail != null) {
			Set<SimpleGrantedAuthority> authorities = new HashSet<>();
			userdetail.getRoles().forEach(role -> {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
			});
			return authorities;
		}
		return null;
	}

	@Override
	public UserDetail findOne(String username) {
		return userRepo.findByEmail(username);
	}

	@Override
	public UserDetail save(UserDetail user) {

		if (userRepo.findByEmail(user.getEmail()) != null)
			throw new BadRequestException("User  Email already exist ");

		user.setPassword(bcryptEncoder.encode(user.getPassword()));

		Role role = roleService.findByName("USER");
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(role);

		if (user.getEmail().split("@")[1].equals("admin.edu")) {
			role = roleService.findByName("ADMIN");
			roleSet.add(role);
		}

		user.setRoles(roleSet);
		return userRepo.save(user);
	}

}
