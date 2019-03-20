 package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Customer;
import com.example.demo.model.User;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	List<Customer> findByFirstname(String firstname);
	List<Customer> findByLastname(String lastname);

}
