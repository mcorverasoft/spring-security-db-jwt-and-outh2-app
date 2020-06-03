package com.mcorvera.socialapp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mcorvera.socialapp.model.security.AuthenticatedUser;

@Component
public class JwtFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider; 
	
	@Value("${app.jwtType}")
	private String jwtType;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// TODO Auto-generated method stub
				String bearerToken=((HttpServletRequest)request).getHeader("Authorization");
				String jwtToken;
				try {
					if(bearerToken==null || !StringUtils.hasText(bearerToken)  || !bearerToken.startsWith(jwtType) ) {
						SecurityContextHolder.getContext().setAuthentication(null);//No Authentication
						chain.doFilter(request, response);
						return;
					}else {				
						jwtToken= bearerToken.substring(jwtType.length()+1, bearerToken.length()-1);
						if (jwtTokenProvider.validateJwtToken(jwtToken)){
							AuthenticatedUser authenticatedUser = jwtTokenProvider.getUserFromJWT(jwtToken);  
							UsernamePasswordAuthenticationToken authentication= 
							new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
							SecurityContextHolder.getContext().setAuthentication(authentication);
							chain.doFilter(request, response);
							return;
						}else {
							SecurityContextHolder.getContext().setAuthentication(null);//No Authentication
							chain.doFilter(request, response);
							return;
						}
						
					}		
						
				}catch(Exception ex) {
					logger.error("Error in authentication: "+ex.getMessage());
					
				}
		
	}



}
