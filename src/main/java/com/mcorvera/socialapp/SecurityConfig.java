package com.mcorvera.socialapp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mcorvera.socialapp.security.JwtFilter;
import com.mcorvera.socialapp.security.JwtUnauthorizedHandlerEntryPoint;
import com.mcorvera.socialapp.security.oauth2.CustomAuthenticationFailureHandler;
import com.mcorvera.socialapp.security.oauth2.CustomAuthenticationSuccessHandler;
import com.mcorvera.socialapp.security.oauth2.CustomAuthorizationRequestRepository;
import com.mcorvera.socialapp.security.oauth2.CustomOauth2UserService;
import com.mcorvera.socialapp.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Autowired
	private JwtFilter jwtfilt;

    @Bean
    public JwtFilter getJwtFilter() {
    	return new JwtFilter();
    }
    
    @Autowired
    private JwtUnauthorizedHandlerEntryPoint jwtUnauthorizedHandlerEntryPoint;
    
    @Autowired
	private CustomAuthorizationRequestRepository customAuthorizationRequestRepository;

    @Autowired
	private CustomOauth2UserService customOauth2UserService;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    
    @Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	

	
	
	
    
//    @Autowired
//	private CustomOAuth2UserService oauth2UserService;

    
    @Bean
    @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}


	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		// TODO Auto-generated method stub
		authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bcrypt);
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http
			
		.cors()
        	.and()
        .csrf()
        	.disable()
        .formLogin()
        	.disable()
        .httpBasic()
        	.disable()
        .exceptionHandling()
        	.authenticationEntryPoint(jwtUnauthorizedHandlerEntryPoint)
        	.and()
		.authorizeRequests()
				.antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/error", 
                        "/webjars/**").permitAll()
				.antMatchers("/app/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.logout()
				.logoutSuccessUrl("/logout").permitAll()
			.and()
			.oauth2Login()
				.authorizationEndpoint()
				.baseUri("/oauth2/authorize/")
				.authorizationRequestRepository(customAuthorizationRequestRepository)
				.and()
			.userInfoEndpoint()
				.userService(customOauth2UserService)
				.and()
            .redirectionEndpoint()
            	.baseUri("/oauth2/callback/*")
            	.and()
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler(customAuthenticationFailureHandler)
            .and()
            .addFilterBefore(jwtfilt , UsernamePasswordAuthenticationFilter.class );
            
    
//			.failureHandler((request, response, exception) -> {
//			    request.getSession().setAttribute("error.message", exception.getMessage());
//			    authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
//            })
//				.authorizationEndpoint()
//					.baseUri("/oauth2/authorize")
//					.and()
//				.redirectionEndpoint()
//					.baseUri("/oauth2/callback/*")
//					.and()
//				.userInfoEndpoint()
//					.userService(oauth2UserService)
			
			
				
				
			
	}

    
}


