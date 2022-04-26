package com.jwtLogin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtLogin.dto.JwtRequest;
import com.jwtLogin.dto.JwtResponse;
import com.jwtLogin.entities.Usuario;
import com.jwtLogin.repositories.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {
	
	@Value("${spring.jwt.expiration}")
	private String expiration;
	@Value("${spring.jwt.secretKey}")
	private String secretKey;
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	public void cadastrar(JwtRequest user) {
		Usuario userToSave =  new Usuario(user.getName(), user.getEmail(), passwordEncoder.encode(user.getPassword()));
		repository.save(userToSave);
	}
	
	public JwtResponse authenticate(JwtRequest user) {
		
		boolean userExists = isUserExists(user.getEmail());
		if(!userExists) {
			throw new RuntimeException("Usuário não encontrado");
		}
		
		boolean passwordValid = isPasswordValid(user.getEmail(), user.getPassword());
		if(!passwordValid) {
			throw new RuntimeException("Senha inválida");
		}
		
		Usuario userFound = repository.findByEmail(user.getEmail());
		String token = jwtService.generateToken(user);
		return new JwtResponse(userFound.getName(), user.getEmail(), token);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario user = repository.findByEmail(email);
		return User.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.roles("USER")
				.build();
	}
	
	private Boolean isUserExists(String email) {
		Usuario user = repository.findByEmail(email);
		return user != null;
	}
	
	private Boolean isPasswordValid(String username, String password) {
		UserDetails userDetails = loadUserByUsername(username);
		boolean passwordValid = passwordEncoder.matches(password, userDetails.getPassword());
		return passwordValid;
	}
}
