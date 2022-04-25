package com.jwtLogin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jwtLogin.dto.JwtRequest;
import com.jwtLogin.dto.JwtResponse;
import com.jwtLogin.services.UsuarioService;

@RestController
@RequestMapping(value = "/users")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;

	@PostMapping(value = "/cadastrar")
	@ResponseStatus(HttpStatus.CREATED)
	public void cadastrar(@RequestBody JwtRequest user) {
		usuarioService.cadastrar(user);
	}
	
	@PostMapping(value = "/autenticar")
	public JwtResponse autenticar(@RequestBody JwtRequest user) {
		
		boolean isAutenticacao = usuarioService.autenticar(user);
		if(!isAutenticacao) {
			throw new RuntimeException("Senha inválida");
		}
		
		String token = usuarioService.generateToken(user);
		return new JwtResponse(user.getEmail(), token);
	}
}
