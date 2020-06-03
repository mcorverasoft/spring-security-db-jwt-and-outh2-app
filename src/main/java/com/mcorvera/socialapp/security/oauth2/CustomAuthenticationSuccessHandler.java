package com.mcorvera.socialapp.security.oauth2;

import static com.mcorvera.socialapp.security.oauth2.CustomAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mcorvera.socialapp.model.security.AuthenticatedUser;
import com.mcorvera.socialapp.security.JwtTokenProvider;
import com.mcorvera.socialapp.util.CookieAdminI;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private CookieAdminI cookieAdmin;
	@Autowired
	private CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	

	
	@Value("${app.jwtType}")
	private String jwtType;
	
	@Value("#{'${app.oauth2.authorizedRedirectUris}'.split(',')}")
	private List<String> authorizedRedirectUris=new ArrayList<>();
	
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		String forwardSuccesUrl=cookieAdmin.getCookie(REDIRECT_URI_PARAM_COOKIE_NAME, request)
				.map(cookie->cookie.getValue()).orElse("/");
		
		//Generate the json web token to user:
		String jwtToken=jwtTokenProvider.generateJwtToken((AuthenticatedUser) authentication.getPrincipal());	
		
		URIBuilder url;
		boolean isAuthorizedRedirectUri;

		try {
			url = new URIBuilder(forwardSuccesUrl);
			url.addParameter("token",jwtToken);
			url.addParameter("tokenType",jwtType);
			forwardSuccesUrl=url.toString();
			
			isAuthorizedRedirectUri =	authorizedRedirectUris.stream()
					.anyMatch(authorizedRedirectUri->{
						URIBuilder authorizedURI;
						try {
							authorizedURI = new URIBuilder(authorizedRedirectUri);
							if(authorizedURI.getPort()==url.getPort() && authorizedURI.getHost().equals(url.getHost()))
								return true;
							else
								return false;
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return false;
						});
		
			if(isAuthorizedRedirectUri) {
				customAuthorizationRequestRepository.removeAuthorizationRequest(request);
			}else
				response.setStatus(Integer.valueOf(HttpStatus.BAD_REQUEST.toString()));
			this.customAuthorizationRequestRepository.removeAuthorizationRequestCookie(request, response);
			this.getRedirectStrategy().sendRedirect(request, response, forwardSuccesUrl);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	

}
