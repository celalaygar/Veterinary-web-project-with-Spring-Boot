package com.example.demo.controller;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.Citys;
import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.util.ApiPaths;

@Controller
@RequestMapping(ApiPaths.CustomerBasicCtrl.CTRL)
public class CustomerController {
	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	PetRepository petRepository;

	@ResponseBody
	@GetMapping()
	public String hello() {
		
		return "hello customer";
	}
	
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public String getAllCustomers(Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Customer> customers = customerRepository.findAll();
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		map.put("customers", customers);
		return "customer/customers";
	}

	@RequestMapping(value = "/get-customers", method = RequestMethod.GET)
	public String getAnyCustomers(@RequestParam("name") String name,Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		List<Customer> customers=null;
		customers = customerRepository.findByFirstname(name);
		
		//control whether there is any customer having this firstname called name
		if(customers.size()>0) {
			map.put("customers", customers);
		}else {
			//control whether there is any customer having this lastname called name
			customers = customerRepository.findByLastname(name);
			if(customers.size()>0) {
				map.put("customers", customers);
			}else {
				customers = customerRepository.findAll();
				map.put("customers", customers);
				map.put("message", " Kayıt bulunamamıştır.");
			}
		}
		return "customer/customers";
	}
	
	@RequestMapping(value = "/insert-customer", method = RequestMethod.GET)
	public String CustomerRegisterPanel(Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("title", "Müşteri Ekleme Bölümü");
		map.put("adminname", auth.getName());
		map.put("customer", new Customer());
		map.put("citys", new ArrayList<Citys>(Arrays.asList(Citys.values())));
		return "customer/customer-insert-panel";
	}

	@RequestMapping(value = "/insert-customer", method = RequestMethod.POST)
	public String saveRegisterPage(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,
			Model model, Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", new Customer());
		map.put("title", "Müşteri Ekleme Bölümü");
		if (result.hasErrors()) {
			return "customer-insert-panel";
		} else {
			customerRepository.save(customer);
		}

		map.put("message", "Kayıt işlemi başarlı");
		return "customer/customer-insert-panel";
	}

	@RequestMapping(value = "/show-customer/{customerid}", method = RequestMethod.GET)
	public String CustomerShowPanel(@PathVariable int customerid, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		try {
			Customer customer = customerRepository.findById(customerid).get();
			map.put("title", "Müşteri Detayları");
			map.put("customer", customer);

			return "customer/show-customer";
		} catch (Exception e) {
			List<Customer> customers = customerRepository.findAll();
			map.put("title", "Müşteriler");
			map.put("customers", customers);
			map.put("message", " Kayıt bulunamamıştır.");
			return "customer/customers";
		}
		
	}

	@RequestMapping(value = "/update-customer/{customerid}", method = RequestMethod.GET)
	public String CustomerUpdatePanel(@PathVariable int customerid, Map<String, Object> map)  throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());

		try {
			Customer customer = customerRepository.findById(customerid).get();
			map.put("title", "Müşteri Güncelleme Bölümü");
			map.put("customer", customer);
			map.put("citys", new ArrayList<Citys>(Arrays.asList(Citys.values())));
			return "customer/customer-update-panel";
		} catch (Exception e) {
			List<Customer> customers = customerRepository.findAll();
			map.put("title", "Müşteriler");
			map.put("customers", customers);
			map.put("message", " Kayıt bulunamamıştır.");
			return "customer/customers";
		}
	}

	@RequestMapping(value = "/update-customer", method = RequestMethod.POST)
	public String CustomerUpdate(@Valid @ModelAttribute("customer") Customer customer, BindingResult result,
			Map<String, Object> map)  throws SQLException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("customer", new Customer());
		if (result.hasErrors()) {
			map.put("title", "Müşteri Ekle");
			return "customer-insert-panel";
		} else {
			customerRepository.save(customer);
		}
		List<Customer> customers = customerRepository.findAll();
		map.put("message", "Kayıt güncelleme işlemi başarlı.");
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		map.put("customers", customers);
		return "customer/customers";
	}
	@RequestMapping(value = "/delete-customer/{customerid}", method = RequestMethod.GET)
	public String CustomerDelete(@PathVariable int customerid, Map<String, Object> map) throws SQLException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		try {
			Customer customer = customerRepository.findById(customerid).get();
			petRepository.deleteAll(customer.getPets());
			customer.setPets(null);
			customerRepository.delete(customer);
			map.put("message", "Kayıt silinmiştir.");

		} catch (Exception e) {
			map.put("message", " Kayıt bulunamamıştır.");
		}
		
		List<Customer> customers = customerRepository.findAll();
		map.put("title", "Müşteriler");
		map.put("customers", customers);
		return "customer/customers";
	}


}
