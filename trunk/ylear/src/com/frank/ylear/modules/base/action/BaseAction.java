package com.frank.ylear.modules.base.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import com.frank.ylear.common.model.PageBean;
import com.opensymphony.xwork2.ActionSupport;
public class BaseAction extends ActionSupport implements ServletRequestAware,ServletResponseAware,SessionAware,ParameterAware
{
	private PageBean page = new PageBean();
	
	private HttpServletRequest servletRequest;
	private HttpServletResponse servletResponse;
	private Map<String, Object> session;
	private Map<String, String[]> parameters;

	public PageBean getPage()
	{
		return page;
	}

	public void setPage(PageBean page)
	{
		this.page = page;
	}

	

	public Map<String, Object> getSession()
	{
		return session;
	}

	public void setSession(Map<String, Object> session)
	{
		this.session = session;
	}

	public Map<String, String[]> getParameters()
	{
		return parameters;
	}

	public void setParameters(Map<String, String[]> parameters)
	{
		this.parameters = parameters;
	}

	public HttpServletRequest getServletRequest()
	{
		return servletRequest;
	}

	public void setServletRequest(HttpServletRequest servletRequest)
	{
		this.servletRequest = servletRequest;
	}

	public HttpServletResponse getServletResponse()
	{
		return servletResponse;
	}

	public void setServletResponse(HttpServletResponse servletResponse)
	{
		this.servletResponse = servletResponse;
	}

	
	
	

	

}
