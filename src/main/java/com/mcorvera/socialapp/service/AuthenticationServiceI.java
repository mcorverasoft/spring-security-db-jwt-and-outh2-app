package com.mcorvera.socialapp.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;

import com.mcorvera.socialapp.beans.AuthenticationResponse;
import com.mcorvera.socialapp.beans.SignUpRequest;
import com.mcorvera.socialapp.model.security.AuthenticatedUser;

public interface AuthenticationServiceI {

	public AuthenticationResponse authenticateUser(String usernameOrEmail, String password) throws AuthenticationCredentialsNotFoundException, AuthenticationException ;
	
	public AuthenticationResponse registerUser(SignUpRequest signup);
	
	public Optional<AuthenticatedUser> getAuthenticatedUser(); //Get Profile of user
	
}
