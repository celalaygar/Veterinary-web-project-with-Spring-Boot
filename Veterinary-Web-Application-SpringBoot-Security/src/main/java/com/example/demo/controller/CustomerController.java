package com.example.demo.controller;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import com.example.demo.service.Imp.CustomerServiceImp;
import com.example.demo.util.ApiPaths;

@Controller
@RequestMapping(ApiPaths.CustomerBasicCtrl.CTRL)
public class CustomerController {
	@Autowired
	PetRepository petRepository;
	@Autowired
	CustomerServiceImp  customerServiceImp;
	
	@ResponseBody
	@GetMapping()
	public String hello() {
		
		return "hello customer";
	}
	
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public String getAllCustomers(Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<Customer> customers = customerServiceImp.findAll();
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		map.put("customers", customers);
		return "customer/customers";
	}
	
	//http://localhost:8182/customer/customers-page?page=1&size=3
	@ResponseBody
	@RequestMapping(value = "/customers-page", method = RequestMethod.GET)
	public String getAllCustomersPage(Map<String, Object> map,
								      @RequestParam("page") Optional<Integer> page, 
								      @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Page<Customer> customers = customerServiceImp.GetAllPagination(PageRequest.of(currentPage - 1, pageSize));
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		map.put("customers", customers);
		customers.getContent().forEach(aa->{
			System.out.println(aa.getCustomerid()+" "+aa.getEmail());
		});
		return "currentPage : "+currentPage +" <br>"
				+"pageSize : "+ pageSize +" <br>";
	}
	

	@RequestMapping(value = "/get-customers", method = RequestMethod.GET)
	public String getAnyCustomers(@RequestParam("name") String name,Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		List<Customer> customers=null;
		customers = customerServiceImp.findByFirstname(name);
		
		//control whether there is any customer having this firstname called name
		if(customers.size()>0) {
			map.put("customers", customers);
		}else {
			//control whether there is any customer having this lastname called name
			customers = customerServiceImp.findByLastname(name);
			if(customers.size()>0) {
				map.put("customers", customers);
			}else {
				customers = customerServiceImp.findAll();
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
			Boolean control=customerServiceImp.save(customer);
			if(control==false) {
				map.put("message", "Bir Problem Oluştu..");
			}else {
				map.put("message", "Kayıt işlemi başarlı");
			}
		}

		return "customer/customer-insert-panel";
	}

	@RequestMapping(value = "/show-customer/{customerid}", method = RequestMethod.GET)
	public String CustomerShowPanel(@PathVariable int customerid, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());

		Optional<Customer> customer = customerServiceImp.findById(customerid);
		if(customer.isPresent()) {
			map.put("title", "Müşteri Detayları");
			map.put("customer", customer.get());

			return "customer/show-customer";
		}else {
			List<Customer> customers = customerServiceImp.findAll();
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

		Optional<Customer> customer = customerServiceImp.findById(customerid);
		if(customer.isPresent()) {
			map.put("title", "Müşteri Güncelleme Bölümü");
			map.put("customer", customer.get());
			map.put("citys", new ArrayList<Citys>(Arrays.asList(Citys.values())));
			return "customer/customer-update-panel";
		}else {
			List<Customer> customers = customerServiceImp.findAll();
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
			Boolean control=customerServiceImp.save(customer);
			if(control==false) {
				map.put("message", "Bir Problem Oluştu..");
			}else {
				map.put("message", "Kayıt güncelleme işlemi başarlı.");
			}
		}
		List<Customer> customers = customerServiceImp.findAll();
		map.put("title", "Müşteriler");
		map.put("adminname", auth.getName());
		map.put("customers", customers);
		return "customer/customers";
	}
	@RequestMapping(value = "/delete-customer/{customerid}", method = RequestMethod.GET)
	public String CustomerDelete(@PathVariable int customerid, Map<String, Object> map) throws SQLException {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		Optional<Customer> customer = customerServiceImp.findById(customerid);
		if(customer.isPresent()) {
			Boolean control=customerServiceImp.delete(customer.get());
			if(control == true) {
				map.put("message", "Kayıt silinmiştir.");
			}else {
				map.put("message", " Kayıt silme işlemi başarısız.");
			}
		}else {
			map.put("message", " Kayıt bulunamamıştır.");
			
		}
		List<Customer> customers = customerServiceImp.findAll();
		map.put("title", "Müşteriler");
		map.put("customers", customers);
		return "customer/customers";
	}


}
