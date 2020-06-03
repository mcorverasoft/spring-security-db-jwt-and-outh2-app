package com.mcorvera.socialapp;


import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mcorvera.socialapp.model.security.AuthenticatedUser;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {

	@Bean
	public AuditorAware<Long> auditorAware(){
		return new EntityAuditorAware();
	}

}

class EntityAuditorAware implements AuditorAware<Long>{
	@Override
	public Optional<Long> getCurrentAuditor() {
	
		AuthenticatedUser user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication !=null) {
        		if(authentication.isAuthenticated() ) {
        				user=(AuthenticatedUser) authentication.getPrincipal();
        				return Optional.of(user.getId());
        		}else {
        			return Optional.of(0L);//0 user self;
        		}
        }
        else 
        	return Optional.of(0L);//0 user self;
		
	}
}
