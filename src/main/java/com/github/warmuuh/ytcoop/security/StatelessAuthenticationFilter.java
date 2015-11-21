package com.github.warmuuh.ytcoop.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

public class StatelessAuthenticationFilter extends GenericFilterBean {

	private final TokenAuthenticationService authenticationService;
	private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
	private SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler("/login");

	private RequestMatcher matcher = new NegatedRequestMatcher(new OrRequestMatcher(new AntPathRequestMatcher("/webjars/**"),
			new AntPathRequestMatcher("/auth/**"),
			new AntPathRequestMatcher("/login"),
			new AntPathRequestMatcher("/style/**"),
			new AntPathRequestMatcher("/img/**")));
	private JwtAuthenticationSuccessHandler jwtSuccHandler;
	
	public StatelessAuthenticationFilter(TokenAuthenticationService authenticationService, JwtAuthenticationSuccessHandler jwtSuccHandler) {
		this.authenticationService = authenticationService;
		this.jwtSuccHandler = jwtSuccHandler;
		failureHandler.setAllowSessionCreation(false);
		failureHandler.setRedirectStrategy(new RedirectParamStrategy());
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		Authentication authentication = null;
		
		if (!matcher.matches((HttpServletRequest) request)){
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			authentication = authenticationService.getAuthentication(httpRequest);
			if (authentication == null)
				throw new AuthenticationCredentialsNotFoundException("User not found");
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (AuthenticationException ex) {
			jwtSuccHandler.getRequestCache().saveRequest((HttpServletRequest) request, (HttpServletResponse) response);
			failureHandler.onAuthenticationFailure((HttpServletRequest) request, (HttpServletResponse) response, ex);
		}

		SecurityContextHolder.getContext().setAuthentication(null);
	}
}