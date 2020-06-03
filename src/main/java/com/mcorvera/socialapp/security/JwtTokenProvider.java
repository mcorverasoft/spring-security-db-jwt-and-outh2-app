package com.mcorvera.socialapp.security;
import io.jsonwebtoken.*;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mcorvera.socialapp.model.security.AuthenticatedUser;
import com.mcorvera.socialapp.service.CustomUserDetailsService;

@Component
public class JwtTokenProvider {
	
	private static final Logger logger=LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;
	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	public String generateJwtToken(AuthenticatedUser authenticatedUser ) {
		
		Date now= new Date();
		Date expiryDate= new Date(now.getTime()+jwtExpirationInMs);
		
		return Jwts.builder()
				.setSubject(authenticatedUser.getId().toString())
				.setIssuedAt(new Date())
				.setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
		
	}
	
	public AuthenticatedUser getUserFromJWT(String jwtToken) {
		Claims claims; 
		Long id;
		
		claims= Jwts.parser()
						.setSigningKey(jwtSecret)
						.parseClaimsJws(jwtToken)
						.getBody();
		id =Long.parseLong(claims.getSubject());
		
		return (AuthenticatedUser) customUserDetailsService.loadUserByUsername(id);
	}
	
	
	public boolean validateJwtToken(String jwtToken) {
		boolean valid=false;
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
			valid=true;
		}catch(SignatureException ex) {
			logger.error("Invalid JWT signature: "+ex.getMessage());
		}catch(MalformedJwtException ex) {
			logger.error("Invalid JWT token: "+ex.getMessage());
		}catch(ExpiredJwtException ex) {
			logger.error("Invalid JWT token, token is Expired: "+ex.getMessage());
		}catch(UnsupportedJwtException ex) {
			logger.error("Unsupported token: "+ex.getMessage());
		}catch(IllegalArgumentException ex) {
			logger.error("Illegal Argument of token: "+ex.getMessage());
		}
		catch(Exception ex) {
			logger.error("Error in token: "+ex.getMessage());
			throw ex;
		}
		
		return valid;
	}
	
}
