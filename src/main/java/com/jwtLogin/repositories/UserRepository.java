package com.jwtLogin.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwtLogin.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
}
