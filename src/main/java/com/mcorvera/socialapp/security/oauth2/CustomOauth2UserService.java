package com.mcorvera.socialapp.security.oauth2;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.mcorvera.socialapp.model.ProviderNameEnum;
import com.mcorvera.socialapp.model.Role;
import com.mcorvera.socialapp.model.RoleName;
import com.mcorvera.socialapp.model.User;
import com.mcorvera.socialapp.model.security.AuthenticatedUser;
import com.mcorvera.socialapp.repository.RoleRepository;
import com.mcorvera.socialapp.repository.UserRepository;
import com.mcorvera.socialapp.util.CookieAdmin;
import com.mcorvera.socialapp.util.CookieAdminI;



@Component
public class CustomOauth2UserService extends DefaultOAuth2UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// TODO Auto-generated method stub
		OAuth2User oAuth2User= super.loadUser(userRequest);
		
		User user;
		Map<String, Object> attributes=oAuth2User.getAttributes();
		String email= (String) attributes.get("email");
		Optional<User> UserRegistered= this.userRepository.findByEmailOrUsername(email, null);
	
		if(UserRegistered.isPresent()) {
			user = UserRegistered.get();
			if (!user.getProvider().equals(ProviderNameEnum.valueOf(userRequest.getClientRegistration().getRegistrationId()) )) {
				throw new OAuth2AuthenticationException(new OAuth2Error("Account registered"), "Looks like you has a account with "+user.getProvider().name());
			}
			user =  updateUser( userRequest, user, attributes);
		} else {
			user = createNewUser(oAuth2User, userRequest, attributes);
		}
		
	
		return AuthenticatedUser.getUserAuthenticated(user,attributes);
	}
	
	
	private User updateUser(OAuth2UserRequest userRequest,User user, Map<String, Object> attributes) {
		user.setProvider(ProviderNameEnum.valueOf(userRequest.getClientRegistration().getRegistrationId()));
        if(attributes.containsKey("id"))
        	user.setProviderId((String)attributes.get("id"));
        else
        	user.setProviderId((String)attributes.get("sub"));
        if(attributes.containsKey("given_name"))
        	user.setName((String)attributes.get("given_name"));
        else if (attributes.containsKey("first_name"))
        user.setName((String)attributes.get("first_name"));
        
        
        if(attributes.containsKey("family_name"))
        	user.setLastname((String)attributes.get("family_name"));
        else if (attributes.containsKey("last_name"))
        	user.setLastname((String)attributes.get("last_name"));
       
        user.setImageUrl(this.getPictureFromAttributes(attributes));
        user=userRepository.save(user);
        return user;
	}
	
	private User createNewUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest, Map<String, Object> attributes ) {
		User user = new User();
		if(attributes.containsKey("id"))
        	user.setProviderId((String)attributes.get("id"));
        else
        	user.setProviderId((String)attributes.get("sub"));
		
		user.setProvider(ProviderNameEnum.valueOf(userRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(userRequest.getClientRegistration().getClientId());
        if(attributes.containsKey("given_name"))
        	user.setName((String)attributes.get("given_name"));
        else if (attributes.containsKey("first_name"))
        user.setName((String)attributes.get("first_name"));
        
        
        if(attributes.containsKey("family_name"))
        	user.setLastname((String)attributes.get("family_name"));
        else if (attributes.containsKey("last_name"))
        	user.setLastname((String)attributes.get("last_name"));
        user.setUsername((user.getName().trim()+"."+user.getLastname().trim()).toLowerCase().replaceAll(" ", ""));
		if(userRepository.existsByUsername(user.getUsername())) {
			user.setUsername((user.getName().trim()+"."+user.getLastname().trim()).toLowerCase().replaceAll(" ", "")+String.valueOf(Math.random()));
		}
        user.setEmail((String) attributes.get("email"));
        user.setImageUrl(this.getPictureFromAttributes(attributes));
        Role role=roleRepository.findByName(RoleName.ROL_USER);
		List<Role> roles = new ArrayList<>();
		roles.add(role);
		user.setRoles(roles);
        return userRepository.save(user);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private String getPictureFromAttributes(Map<String, Object> attributes) {
		if(attributes.containsKey("picture") && attributes.containsKey("sub") )
			return (String)attributes.get("picture");
		else if(attributes.containsKey("picture")) {
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
			}
		}  else if(attributes.containsKey("avatar_url"))
					return (String)attributes.get("avatar_url");
		return "";
	}
	
}
