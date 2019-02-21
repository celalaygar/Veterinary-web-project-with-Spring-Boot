package com.example.demo.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	
	@RequestMapping(value="/insert-pet/{customerid}",method = RequestMethod.POST)
	public String InsertPet(@PathVariable int customerid, Map<String, Object> map,@Valid @ModelAttribute("pet") Pet pet , BindingResult result, Model model) {
		Customer customer=customerRepository.findById(customerid).get();
		
		return "redirect:/customers/show-customer/"+customerid;
	}
}
