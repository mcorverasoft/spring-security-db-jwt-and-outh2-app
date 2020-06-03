package com.mcorvera.socialapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcorvera.socialapp.beans.AuthenticationResponse;
import com.mcorvera.socialapp.beans.LoginRequest;
import com.mcorvera.socialapp.beans.SignUpRequest;
import com.mcorvera.socialapp.service.AuthenticationServiceI;

@RestController
@RequestMapping("/app")
public class AuthenticationRestController {
	
	@Autowired
	private AuthenticationServiceI authenticationServicei; 
	
	
	@PostMapping("/login")
	public AuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest login) {
		AuthenticationResponse authenticationResponse=new AuthenticationResponse();
		try {
			authenticationResponse=authenticationServicei.authenticateUser(login.getUsernameOrEmail(), login.getPassword());
		}catch( AuthenticationCredentialsNotFoundException ex){
			authenticationResponse.setError(ex.getMessage());
			
		}catch(AuthenticationException ex) {
			authenticationResponse.setError(ex.getMessage());
		}
		catch(Exception ex) {
			authenticationResponse.setError(ex.getMessage());
		}
		return authenticationResponse;
	}
	
	
	@PostMapping("/signup")
	public AuthenticationResponse authenticateUser(@Valid @RequestBody SignUpRequest signup) {
		AuthenticationResponse authenticationResponse=new AuthenticationResponse();
		try {
			authenticationResponse=authenticationServicei.registerUser(signup);
		}catch(Exception ex){
			authenticationResponse.setError(ex.getMessage());
		}
		return authenticationResponse;
	}

}
