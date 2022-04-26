package com.jwtLogin.dto;

public class JwtRequest {

	private String email;
	private String password;
	
	public JwtRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
