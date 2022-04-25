package com.jwtLogin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jwtLogin.dto.JwtRequest;
import com.jwtLogin.dto.JwtResponse;
import com.jwtLogin.services.UsuarioService;

@RestController
@RequestMapping(value = "/authenticate")
public class AuthController {

	@Autowired
	private UsuarioService usuarioService;

	@PostMapping(value = "/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrar(@RequestBody JwtRequest user) {
		usuarioService.cadastrar(user);
	}
	
	@PostMapping
	public ResponseEntity<?> authenticate(@RequestBody JwtRequest user) {
		
		try {
			JwtResponse usuarioAutenticado = usuarioService.authenticate(user);
			return ResponseEntity.status(HttpStatus.OK).body(usuarioAutenticado);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e);
		}
	}
}
