package com.example.demo.controller;

import java.security.Principal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Controller
public class MainController {

	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/")
	public String index(Map<String, Object> map) {
		System.out.println();
		if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			map.put("adminname", auth.getName());
		}
		map.put("title", "Veteriner Anasayfası");
		return "index";
	}

	@RequestMapping("/index")
	public String index2(Map<String, Object> map) {
    	if(SecurityContextHolder.getContext().getAuthentication() != null) {
        	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        	map.put("adminname", auth.getName()); 
    	}
		map.put("title", "Veteriner Anasayfası");
		return "index";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerPage(Model model,Map<String, Object> map) {
		List<Role> roles=roleRepository.findAll();
		map.put("title", "Doktor Kayıt Sayfası");
		map.put("roles", roles);
		model.addAttribute("user", new User());
		return "register";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveRegisterPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model,
			Map<String, Object> map,HttpServletRequest request)  throws SQLException {

		map.put("title", "Doktor Kayıt Sayfası");
		model.addAttribute("user", user);
		List<User> user_control=userRepository.getUserByEmail(user.getEmail());
		// to control whether there is user with this email
		if(user_control.size()>0) {
			map.put("message", "Bu email adresi mevcuttur...");

		}else {
			if (result.hasErrors()) {
				map.put("message", "Bu Hata Oluştu..");
				return "register";
			} else {
				int id=Integer.valueOf(request.getParameter("roles"));
				Role role = roleRepository.findById(id).get();
				user.setRoles(new HashSet<Role>() {    
					{
						add(role);
					}
				});
				user.setReel_password(user.getPassword());
				String pwd = user.getPassword();
				String encryptPwd = passwordEncoder.encode(pwd);
				user.setPassword(encryptPwd);
				map.put("message", "Kayıt işlemi başarılı.");
				userRepository.save(user);
			}
		}

		return "register" ;

	}

	@RequestMapping("/login")
	public String login(Map<String, Object> map) {
		map.put("title", "Doktor Giriş Sayfası");
		return "login";
	}

	@RequestMapping("/admin-panel")
	public String secure(Map<String, Object> map) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		map.put("adminname", auth.getName());
		map.put("title", "Yönetim Paneli");
		return "admin-panel";
	}

}
