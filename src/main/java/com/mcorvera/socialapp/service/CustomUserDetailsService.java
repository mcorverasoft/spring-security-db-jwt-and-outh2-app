package com.mcorvera.socialapp.service;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mcorvera.socialapp.model.User;
import com.mcorvera.socialapp.model.security.AuthenticatedUser;
import com.mcorvera.socialapp.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;


	@Override
	public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
		User user =  this.userRepository.findByEmailOrUsername(emailOrUsername,emailOrUsername)
								.orElseThrow(()->new UsernameNotFoundException("This e-mail is not registered"));
		
		
		
		UserDetails userAuthenticated= AuthenticatedUser.getUserAuthenticated(user, null /*null for local provider*/)		;
		return userAuthenticated;
	}
	
	
	public UserDetails loadUserByUsername(Long id) throws UsernameNotFoundException {
		User user =  this.userRepository.findById(id)
								.orElseThrow(()->new UsernameNotFoundException("This e-mail is not registered"));
	
		
		UserDetails userAuthenticated= AuthenticatedUser.getUserAuthenticated(user, null /*null for local provider*/)	;
		return userAuthenticated;
	}


}
