package com.mcorvera.socialapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mcorvera.socialapp.beans.AuthenticationResponse;
import com.mcorvera.socialapp.beans.SignUpRequest;
import com.mcorvera.socialapp.model.ProviderNameEnum;
import com.mcorvera.socialapp.model.Role;
import com.mcorvera.socialapp.model.RoleName;
import com.mcorvera.socialapp.model.User;
import com.mcorvera.socialapp.model.security.AuthenticatedUser;
import com.mcorvera.socialapp.repository.RoleRepository;
import com.mcorvera.socialapp.repository.UserRepository;
import com.mcorvera.socialapp.security.JwtTokenProvider;

@Service
public class AuthenticationService implements AuthenticationServiceI {
	
	private static final Logger logger=LoggerFactory.getLogger(AuthenticationService.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Value("${app.jwtType}")
	private String jwtType;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	@Override
	public AuthenticationResponse authenticateUser(String usernameOrEmail, String password) throws AuthenticationCredentialsNotFoundException, AuthenticationException  {
		// TODO Auto-generated method stub
		AuthenticationResponse authenticationResponse =new AuthenticationResponse();
		try {
			Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usernameOrEmail.toLowerCase(), password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//Generate the json web token to user:
			String jwtToken=jwtTokenProvider.generateJwtToken((AuthenticatedUser) authentication.getPrincipal());	
			authenticationResponse.setToken(jwtToken);
			authenticationResponse.setTokenType(jwtType);
			authenticationResponse.setAuthenticated(true);
		}
		catch(AuthenticationCredentialsNotFoundException ex) {
			logger.error("Credentials not valid: "+ ex.getMessage());
			throw ex;
			
		}
		catch(AuthenticationException ex) {
			logger.error("Credentials not valid: "+ ex.getMessage());
			throw ex;
		}
		
		return authenticationResponse;
	}

	@Override
	public AuthenticationResponse registerUser(SignUpRequest signup) {
		// TODO Auto-generated method stub
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		
		try {
		
			if(userRepository.existsByEmail(signup.getEmail())) {
				authenticationResponse.setCreated(false);
				authenticationResponse.setError("the email already exist.");
				return authenticationResponse;
			}
			if (signup.getUsername()==null || signup.getUsername().isEmpty()) {
				signup.setUsername(signup.getName().trim()+"."+signup.getLastname().trim());
				if(userRepository.existsByUsername(signup.getUsername())) {
					signup.setUsername((signup.getName().trim()+"."+signup.getLastname().trim()+String.valueOf(Math.random())).toLowerCase());
				}
			}
			
			Role role=roleRepository.findByName(RoleName.ROL_USER);
			if(role==null) {
				authenticationResponse.setCreated(false);
				authenticationResponse.setError("the rol ROL_USER is not registered");
				return authenticationResponse;
			}
			List<Role> roles = new ArrayList<>();
			roles.add(role);
			
			User user =new User("#", ProviderNameEnum.local ,signup.getEmail(), signup.getUsername(), signup.getName(), signup.getLastname(), bcrypt.encode(signup.getPassword()), signup.getPhone(), roles);
			userRepository.save(user);
			
			Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signup.getEmail(), signup.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			//Generate the json web token to user:
			String jwtToken=jwtTokenProvider.generateJwtToken((AuthenticatedUser) authentication.getPrincipal());	
			authenticationResponse.setToken(jwtToken);
			authenticationResponse.setTokenType(jwtType);
			authenticationResponse.setAuthenticated(true);
			authenticationResponse.setCreated(true);
			authenticationResponse.setError("");
		}catch(Exception ex) {
			logger.error("Ha ocurrido un errror al crear el usuario: "+ ex.getMessage());
			throw ex;
		}
		
		return authenticationResponse;
	}

	@Override
	public Optional<AuthenticatedUser> getAuthenticatedUser() {
		// TODO Auto-generated method stub
		return null;
	}

}
