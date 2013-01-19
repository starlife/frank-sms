package com.tourzj.common.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetEncodingFilter implements Filter {
	protected String encoding = null;
	protected FilterConfig filterConfig = null;
	protected boolean ignore = true;

	public void destroy() {
		this.encoding = null;
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (!ignore && encodingAvailable()
				&& (request.getCharacterEncoding() == null)) {
				request.setCharacterEncoding(encoding);
				response.setCharacterEncoding(encoding);
		}
		chain.doFilter(request, response);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		this.encoding = filterConfig.getInitParameter("encoding");
		String value = filterConfig.getInitParameter("ignore");
		if (value == null)
			this.ignore = false;
		else if (value.equalsIgnoreCase("true"))
			this.ignore = true;
		else if (value.equalsIgnoreCase("yes"))
			this.ignore = true;
		else
			this.ignore = false;
	}

	public boolean encodingAvailable() {
		if (encoding == null || encoding.trim().equals("")) {
			return false;
		}
		try {
			Charset.forName(encoding);
			return true;
		} catch (UnsupportedCharsetException ex) {
			return false;
		}
	}

}
