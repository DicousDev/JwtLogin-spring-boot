package com.jwtLogin.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

	@GetMapping
	public ResponseEntity<String> findProdutosAll() {
		return ResponseEntity.status(HttpStatus.OK).body(new String("produtos"));
	}
}
