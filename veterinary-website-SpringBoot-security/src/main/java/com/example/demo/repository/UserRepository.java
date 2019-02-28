 package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
	User findByEmail(String email);
	List<User> getUserByEmail(String email);
}
