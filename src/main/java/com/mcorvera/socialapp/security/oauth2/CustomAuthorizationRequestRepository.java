package com.mcorvera.socialapp.security.oauth2;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.mcorvera.socialapp.util.CookieAdminI;
import com.nimbusds.oauth2.sdk.util.StringUtils;

@Component
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	
	 public static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
	 public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
	 public static final String OAUTH2_ACTION_COOKIE_NAME = "action";
	 private int cookieExpirySecs = 1000;
	
	@Autowired
	private CookieAdminI cookieAdmin;
	
	
	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
		
		OAuth2AuthorizationRequest oAuth2AuthorizationRequest=cookieAdmin.getCookie(AUTHORIZATION_REQUEST_COOKIE_NAME, request)
				.map(cookie->
				this.deserialize(cookie.getValue(), OAuth2AuthorizationRequest.class )).orElse(null);
		return oAuth2AuthorizationRequest;
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
			HttpServletResponse response) {
		//If authorizationRequest==null delete previous cookies with authorizations
		if(authorizationRequest==null) {
			cookieAdmin.removeCookie(AUTHORIZATION_REQUEST_COOKIE_NAME, request, response);
		}
		cookieAdmin.save(AUTHORIZATION_REQUEST_COOKIE_NAME, this.serialize(authorizationRequest) , 
				cookieExpirySecs, response);
		String redirectUri = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUri)) {
        	cookieAdmin.save( REDIRECT_URI_PARAM_COOKIE_NAME, redirectUri, cookieExpirySecs, response);
        }
        String action = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(action)) {
        	cookieAdmin.save(OAUTH2_ACTION_COOKIE_NAME,request.getParameter(OAUTH2_ACTION_COOKIE_NAME)  , 
				cookieExpirySecs, response);
        }
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return this.loadAuthorizationRequest(request);
	}
	
	public void removeAuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response) {
		cookieAdmin.removeCookie(REDIRECT_URI_PARAM_COOKIE_NAME, request, response);
		cookieAdmin.removeCookie(AUTHORIZATION_REQUEST_COOKIE_NAME, request, response);
		cookieAdmin.removeCookie(OAUTH2_ACTION_COOKIE_NAME, request, response);
	}
	
	//Serialize Object		
	private String serialize(Object object) {
		System.out.println(Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object)));
		return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
	}


	private <T> T deserialize(String serializedObj, Class<T> cls) {
		System.out.println(cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(serializedObj))));
		return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(serializedObj)));
	}

}
