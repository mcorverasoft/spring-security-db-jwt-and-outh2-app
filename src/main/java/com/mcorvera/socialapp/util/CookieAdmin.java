package com.mcorvera.socialapp.util;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class CookieAdmin implements CookieAdminI{

	@Override
	public void save(String name, String value, int maxTime, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Cookie cookie= new Cookie(name, value);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(maxTime);
		response.addCookie(cookie);
	}


	@Override
	public Optional<Cookie> getCookie(String name, HttpServletRequest request) {
		Cookie[] cookies= request.getCookies();
		if(cookies!=null) {
			for(Cookie cookie: cookies) {
				if(cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}
	
	@Override
	public void removeCookie(String name, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		Cookie[] cookies= request.getCookies();
		for(Cookie cookie: cookies) {
			if(cookie.getName().equals(name)) {
				cookie.setValue("");
				cookie.setMaxAge(0);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}

	@Override
	public Optional<Cookie[]> getAllCookie(HttpServletRequest httpSrequest) {
		// TODO Auto-generated method stub
		return Optional.of(httpSrequest.getCookies());
		
	}




}
