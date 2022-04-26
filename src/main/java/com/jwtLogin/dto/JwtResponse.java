package com.jwtLogin.dto;

public class JwtResponse {

	private String name;
	private String email;
	private String token;

	public JwtResponse(String name, String email, String token) {
		this.name = name;
		this.email = email;
		this.token = token;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}
}
