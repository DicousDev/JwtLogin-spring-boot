package com.jwtLogin.dto;

public class JwtResquest {

	private String email;
	private String password;
	
	public JwtResquest(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
