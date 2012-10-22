package com.frank.ylear.modules.base.action;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
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

	
	/**
	 *得到request
	 * 
	 * @return
	 */
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 得到response
	 * @return
	 */
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	/**
	 * 得到session
	 * 
	 * @param name
	 * @return
	 */
	protected HttpSession getSession2() {
		return getRequest().getSession();
	}
	
	
	
	/**
	 * 从session中获取属性值
	 * @param name
	 * @return
	 */
	protected Object getSession(String name){
		return getServletRequest().getSession().getAttribute(name);
	}

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

	
	/**
	 * Ajax后台向前台传输数据
	 * @param content	chuandi9内容
	 * @throws IOException
	 */
	protected void sendAjaxMsg(Object content) throws IOException{
		HttpServletResponse response = ServletActionContext.getResponse();      
        response.setCharacterEncoding("UTF-8");      
        response.getWriter().print(content.toString());
	}
	

	

}
