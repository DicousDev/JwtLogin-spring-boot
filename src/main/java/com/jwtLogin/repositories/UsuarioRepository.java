package com.jwtLogin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwtLogin.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);
}
