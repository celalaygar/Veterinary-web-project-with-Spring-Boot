package com.example.demo.controller;

import java.util.HashSet;
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
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PetRepository;

@Controller
@RequestMapping("/customers")
public class CustomerController {
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	PetRepository petRepository;

	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public String getAllCustomers(Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Customer> customers = customerRepository.findAll();
		map.put("adminname", auth.getName());
		map.put("customers", customers);
		return "customer/customers";
	}

	@RequestMapping(value = "/customer-insert-panel", method = RequestMethod.GET)
	public String CustomerRegisterPanel(Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", new Customer());
		return "customer/customer-insert-panel";
	}

	@RequestMapping(value = "/customer-insert-panel", method = RequestMethod.POST)
	public String saveRegisterPage(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model,
			Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", new Customer());
        if (result.hasErrors()) {
			return "customer-insert-panel";
        } else {
			customerRepository.save(customer);
        }

		map.put("message", "Kayıt işlemi başarlı");
		return "customer/customer-insert-panel";
	}

	@RequestMapping(value = "/show-customer/{customerid}", method = RequestMethod.GET)
	public String CustomerShowPanel(@PathVariable int customerid, Map<String, Object> map) {
		Customer customer = customerRepository.findById(customerid).get();
		List<Pet> pets=petRepository.findByCustomer(customer);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", customer);
		map.put("pet", new Pet());
		map.put("pets", pets);

		return "customer/show-customer";
	}

	@RequestMapping(value="/delete-customer/{customerid}",method = RequestMethod.GET)
	public String CustomerDelete(@PathVariable int customerid, Map<String, Object> map) {
		Customer customer=customerRepository.findById(customerid).get();
		customerRepository.delete(customer);
		
		Authentication auth  = SecurityContextHolder.getContext().getAuthentication();
		List<Customer> customers=customerRepository.findAll();
    	map.put("adminname", auth.getName());  
    	map.put("customers", customers);
		map.put("message", "Kayıt silinmiştir.");
		return "customer/customers";
	}
	
	@RequestMapping(value="/insert-pet/{customerid}",method = RequestMethod.POST)
	public String InsertPet(@PathVariable int customerid, Map<String, Object> map,@Valid @ModelAttribute("pet") Pet pet , BindingResult result, Model model) {
		Customer customer = customerRepository.findById(customerid).get();

		if (result.hasErrors()) {
			
			return "customer/show-customer";
		} else {
			
			pet.setCustomer(customer);
			petRepository.save(pet);
		}
		List<Pet> pets=petRepository.findByCustomer(customer);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", customer);
		map.put("pets", pets);
		map.put("message", "Kayıt işlemi başarılı");
		//return "redirect:/customers/show-customer/"+customerid;
		return "customer/show-customer";
	}

	
	@RequestMapping(value="/delete-pet/{pet_id}",method = RequestMethod.GET)
	public String DeletePet(@PathVariable long pet_id, Map<String, Object> map,@Valid @ModelAttribute("pet") Pet pet , BindingResult result, Model model) {

		Pet selected_pet = petRepository.findById(pet_id).get();
		int customerid=selected_pet.getCustomer().getCustomerid();
		selected_pet.setCustomer(null);
		if (result.hasErrors()) {
			
			return "customer/show-customer";
		} else {
			petRepository.delete(selected_pet);
			
		}
		Customer customer=customerRepository.findById(customerid).get();
		
		List<Pet> pets=petRepository.findByCustomer(customer);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", customer);
		map.put("pets", pets);
		map.put("message", "Kayıt silinmiştir.");
		return "customer/show-customer";
	}
}
