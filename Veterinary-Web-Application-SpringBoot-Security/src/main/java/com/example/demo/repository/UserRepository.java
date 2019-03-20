 package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
	User findByEmail(String email);
	
	@Query("select u from User u Where u.email= :email")
	List<User> getUserByEmail(@Param("email") String email);
}
