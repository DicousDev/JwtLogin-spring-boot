package com.jwtLogin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jwtLogin.dto.JwtResquest;
import com.jwtLogin.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService service;

	@PostMapping(value = "/cadastrar")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrar(@RequestBody JwtResquest user) {
		service.cadastrar(user);
	}
}
