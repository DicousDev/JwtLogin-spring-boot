package com.jwtLogin.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

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
	
	public void cadastrar(JwtRequest user) {
		Usuario userToSave =  new Usuario(user.getEmail(), passwordEncoder.encode(user.getPassword()));
		repository.save(userToSave);
	}
	
	public JwtResponse authenticate(JwtRequest user) {
		boolean passwordValid = isPasswordValid(user.getEmail(), user.getPassword());
		
		if(!passwordValid) {
			throw new RuntimeException("Senha inválida");
		}
		
		String token = generateToken(user);
		return new JwtResponse(user.getEmail(), token);
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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario user = repository.findByEmail(email);
		
		return User.builder()
				.username(user.getEmail())
				.password(user.getPassword())
				.roles("USER")
				.build();
	}
	
	private Boolean isPasswordValid(String username, String password) {
		UserDetails userDetails = loadUserByUsername(username);
		boolean passwordValid = passwordEncoder.matches(password, userDetails.getPassword());
		return passwordValid;
	}
	
    private Claims obterClaims (String token) throws ExpiredJwtException {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
