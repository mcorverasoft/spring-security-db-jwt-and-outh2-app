package com.mcorvera.socialapp.util;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface CookieAdminI {
	
	void save(String name, String value, int maxTime, HttpServletResponse response);
	Optional<Cookie> getCookie(String name, HttpServletRequest request);
	void removeCookie(String name, HttpServletRequest request, HttpServletResponse response);
	Optional<Cookie[]> getAllCookie(HttpServletRequest request);


}
