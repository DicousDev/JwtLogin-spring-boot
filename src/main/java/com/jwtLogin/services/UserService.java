package com.jwtLogin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtLogin.dto.JwtResquest;
import com.jwtLogin.entities.User;
import com.jwtLogin.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	public void cadastrar(JwtResquest user) {
		User userToSave =  new User(user.getEmail(), passwordEncoder.encode(user.getPassword()));
		repository.save(userToSave);
	}
}
