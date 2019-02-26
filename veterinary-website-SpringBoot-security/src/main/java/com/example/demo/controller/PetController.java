package com.example.demo.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PetRepository;

@Controller
@RequestMapping("/pets")
public class PetController {
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	PetRepository petRepository;


	@RequestMapping(value = "/delete-pet/{pet_id}", method = RequestMethod.GET)
	public String DeletePet(@PathVariable long pet_id, Map<String, Object> map, @Valid @ModelAttribute("pet") Pet pet,
			BindingResult result, Model model) throws SQLException {

		try {
			Pet selected_pet = petRepository.findById(pet_id).get();
			int customerid = selected_pet.getCustomer().getCustomerid();
			selected_pet.setCustomer(null);
			if (result.hasErrors()) {
				return "customer/show-customer";
			} else {
				petRepository.delete(selected_pet);
			}
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			map.put("adminname", auth.getName());
			Customer customer = customerRepository.findById(customerid).get();
			List<Pet> pets = petRepository.findByCustomer(customer);
			map.put("title", "Müşteri Detayları");
			map.put("customer", customer);
			map.put("pets", pets);
			map.put("message", "Kayıt silinmiştir.");
			return "customer/show-customer";
		} catch (Exception e) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			map.put("adminname", auth.getName());
			List<Customer> customers = customerRepository.findAll();
			map.put("title", "Müşteriler");
			map.put("customers", customers);
			map.put("message","Kayıt bulunamamıştır.");
			return "customer/customers";
		}

	}

}
