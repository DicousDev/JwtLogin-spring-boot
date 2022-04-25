package com.jwtLogin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwtLogin.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
