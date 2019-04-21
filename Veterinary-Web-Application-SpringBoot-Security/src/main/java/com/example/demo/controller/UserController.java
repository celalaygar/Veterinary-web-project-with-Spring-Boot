package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Citys;
import com.example.demo.model.Customer;
import com.example.demo.model.Pet;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.ApiPaths;

@Controller
@RequestMapping(ApiPaths.UserBasicCtrl.CTRL)
public class UserController {

	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/img/";
	
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder passwordEncoder;
	
	public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@RequestMapping(value = "/show-user/{email}/", method = RequestMethod.GET)
	public String UserShowPanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("user", users.get(0));
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user/{email}/", method = RequestMethod.GET)
	public String UserUpdatePanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Kayıt Güncelleme Sayfası");
		
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("user", users.get(0));
				map.put("citys", new ArrayList<Citys>(Arrays.asList(Citys.values())));
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
		if(auth.getName().equals(users.get(0).getEmail())) {

			if (result.hasErrors()) {
				map.put("message", "Kayıt işlmei başarısız..");
				return "user/user-details";
			}
			user.setImage(users.get(0).getImage());
			user.setReel_password(users.get(0).getReel_password());
			user.setPassword(users.get(0).getPassword());
			map.put("message", "Kayıt işlemi başarılı. Tekrar giriş yapınız.");
			userRepository.save(user);
			return "redirect:/logout";
		}else {
			map.put("message", users.get(0).getEmail()+" email adresi size ait değildir.");
		}

		return "user/user-details";

	}
	
	
	@RequestMapping(value = "/update-user-password/{email}/", method = RequestMethod.GET)
	public String UserPasswordUpdatePanel(@PathVariable String email, Map<String, Object> map) throws SQLException {

		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				map.put("message", email+" Hoşgeldiniz.");
				map.put("user", users.get(0));
				return "user/user-password-update-panel";
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
	
	@RequestMapping(value = "/update-user-password", method = RequestMethod.POST)
	public String UserPasswordUpdate(Map<String, Object> map,WebRequest request) throws SQLException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		List<User> users=userRepository.getUserByEmail(auth.getName());
		map.put("user", users.get(0));
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		if(request.getParameter("oldpassword1").equals(request.getParameter("oldpassword2"))){
			boolean control=passwordEncoder.matches( request.getParameter("oldpassword1"),users.get(0).getPassword());
			if(control) {
				//changing password
				users.get(0).setReel_password(request.getParameter("password"));
				users.get(0).setPassword(passwordEncoder.encode(request.getParameter("password")));
				userRepository.save(users.get(0));
				map.put("message", " Şifreniz başarıyla güncellenmiştir..");
				return "redirect:/logout";
			}else {
				map.put("message", "Mevcut şifrenizi yanlış girdiniz.");
			}
		}else {
			map.put("message", "Mevcut şifreler uyuşmuyor.");
		}
		return "user/user-details";
	}
	

	
	
	 @RequestMapping(value="/upload-image/{email}/",method=RequestMethod.POST)
	  public String upload(@PathVariable String email, Map<String, Object> map, @RequestParam("files") MultipartFile[] files) {
		 

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		map.put("adminname", auth.getName());
		map.put("title", "Doktor Detayları");
		List<User> users=userRepository.getUserByEmail(email);
		if(users.size()>0) {
			if(auth.getName().equals(users.get(0).getEmail())) {
				// upload image
				StringBuilder fileNames = new StringBuilder();
				for (MultipartFile file : files) {
					Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
					fileNames.append(file.getOriginalFilename()+" ");
					//fileNames.toString().replace(" ", "_");
					users.get(0).setImage(fileNames.toString());
					userRepository.save(users.get(0));
					try {
						Files.write(fileNameAndPath, file.getBytes());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				map.put("message", email+" Hoşgeldiniz.");
				//map.put("message_2", "Successfully uploaded files "+fileNames.toString());
				map.put("message_2", "Resminiz Başarıyla Yüklenmiştir.");
				map.put("user", users.get(0));
			}else {
				map.put("message", email+" email adresi size ait değildir.");
			}
		}else {
			
			map.put("message", email+" email adresine ait kayıt bulunamamıştır..");
		}
		return "user/user-details";
	}
}
