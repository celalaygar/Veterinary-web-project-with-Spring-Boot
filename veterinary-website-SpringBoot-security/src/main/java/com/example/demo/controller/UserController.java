package com.example.demo.controller;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/show-user/{email}", method = RequestMethod.GET)
	public String UserShowPanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("message", email+" Hoşgeldiniz.");
				map.put("user", users.get(0));
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user/{email}", method = RequestMethod.GET)
	public String UserUpdatePanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Kayıt Güncelleme Sayfası");
		
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("user", users.get(0));
				return "user/user-update-panel";
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user/", method = RequestMethod.POST)
	public String UserUpdate(@Valid @ModelAttribute("user") User user, BindingResult result,
			Map<String, Object> map)  throws SQLException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Kayıt Güncelleme Sayfası");
		List<User> users=userRepository.getUserByEmail(auth.getName());
		// to control whether there is user with this email
		
		if(auth.getName().equals(users.get(0).getEmail())) {

			if (result.hasErrors()) {
				map.put("message", "Kayıt işlmei başarısız..");
				return "user/user-details";
			}
			String pwd = user.getPassword();
			String encryptPwd = passwordEncoder.encode(pwd);
			user.setPassword(encryptPwd);
			map.put("message", "Kayıt işlemi başarılı. Tekrar giriş yapınız.");
			userRepository.save(user);
			return "redirect:/logout";
		}else {
			map.put("message", users.get(0).getEmail()+" email adresi size ait değildir.");
		}

		return "user/user-details";

	}
}
