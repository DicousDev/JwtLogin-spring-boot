package com.jwtLogin.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jwtLogin.dto.JwtRequest;
import com.jwtLogin.entities.Usuario;
import com.jwtLogin.repositories.UsuarioRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UsuarioService implements UserDetailsService {
	
	private String expiration = "30";
	private String secretKey = "eyJSb2xlIjoiVXNlc";
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void cadastrar(JwtRequest user) {
		Usuario userToSave =  new Usuario(user.getEmail(), passwordEncoder.encode(user.getPassword()));
		repository.save(userToSave);
	}
	
	public Boolean autenticar(JwtRequest user) {
		UserDetails userDetails = loadUserByUsername(user.getEmail());
		boolean passwordValid = passwordEncoder.matches(user.getPassword(), userDetails.getPassword());
		return passwordValid;
	}
	
	public String generateToken(JwtRequest user) {
		long expiration = Long.valueOf(this.expiration);
		LocalDateTime timeExpiration = LocalDateTime.now().plusMinutes(expiration);
		Instant instant = timeExpiration.atZone(ZoneId.systemDefault()).toInstant();
		Date date = Date.from(instant);
		
        Map<String, Object> claims = new HashMap<>();
        claims.put("Email", user.getEmail());
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(user.getEmail())
				.setExpiration(date)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
	
    public Boolean tokenValido(String token) {
        try {
            var claims = obterClaims(token);
            var expiracao = claims.getExpiration();
            var expiracaoLocalDate = expiracao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return !LocalDateTime.now().isAfter(expiracaoLocalDate);
        } catch (Exception e) {
            return false;
        }
    }
    
    public String objetLoginUsuario (String token) {
        return (String) obterClaims(token).getSubject();
    }
	
    private Claims obterClaims (String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
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
}
