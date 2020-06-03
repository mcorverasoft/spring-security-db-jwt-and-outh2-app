package com.mcorvera.socialapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcorvera.socialapp.model.User;
import com.mcorvera.socialapp.model.security.AuthenticatedUser;
import com.mcorvera.socialapp.service.HomeServiceI;

@RestController
public class HomeController {
	@Autowired
	private HomeServiceI homeService;
	
	@Value("${app.jwtSecret}")
    private String jwtSecret;

	@GetMapping("/")
	public String homepage() {
		
		return "hola mundo";
	}
	@GetMapping("/list-user")
	public List<User> getUserList() {
		List<User> users= homeService.getAllListUser();
		return users;
	}
	
	@GetMapping("/auth-user")
	public AuthenticatedUser getAuthenticatedUser() {
		AuthenticatedUser authenticatedUser= homeService.getAuthenticatedUser().get();
		return authenticatedUser;
	}
	
	
	@GetMapping("/read-property")
	public String getProperty(){
		return this.jwtSecret;
	}
}


