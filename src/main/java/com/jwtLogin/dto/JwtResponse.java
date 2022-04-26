package com.jwtLogin.dto;

public class JwtResponse {

	private String email;
	private String token;

	public JwtResponse(String email, String token) {
		this.email = email;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public String getToken() {
		return token;
	}
}
