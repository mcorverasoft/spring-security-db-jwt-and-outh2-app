package com.mcorvera.socialapp.service;

import java.util.List;
import java.util.Optional;

import com.mcorvera.socialapp.model.User;
import com.mcorvera.socialapp.model.security.AuthenticatedUser;

public interface HomeServiceI {
	
	public List<User> getAllListUser();

	public Optional<AuthenticatedUser> getAuthenticatedUser();

}
