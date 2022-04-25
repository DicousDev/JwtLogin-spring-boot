package com.jwtLogin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtLogin.dto.JwtRequest;
import com.jwtLogin.entities.User;
import com.jwtLogin.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	public void cadastrar(JwtRequest user) {
		User userToSave =  new User(user.getEmail(), passwordEncoder.encode(user.getPassword()));
		repository.save(userToSave);
	}
	
	public UserDetails autenticar(JwtRequest user) {
		UserDetails userDetails = loadUserByUsername(user.getEmail());
		boolean passwordValid = passwordEncoder.matches(user.getPassword(), userDetails.getPassword());
		
		if(!passwordValid) {
			throw new RuntimeException("Senha inválida.");
		}
		
		return userDetails;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repository
						.findByEmail(email)
						.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));;
		
		return org.springframework.security.core.userdetails.User
				.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.build();
	}
}
