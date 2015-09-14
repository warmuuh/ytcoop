package com.github.warmuuh.ytcoop.security;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.DefaultRedirectStrategy;

/**
 * adds original path as query parameter
 * 
 * @author pm
 *
 */
public class RedirectParamStrategy extends DefaultRedirectStrategy {

	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		String qrStr = request.getQueryString();
		if (qrStr == null) 
			qrStr = "";
		
		String query = "?redirect=" + URLEncoder.encode(request.getRequestURI() + "?" + qrStr, "UTF-8");
		super.sendRedirect(request, response, url + query);
	}

}
