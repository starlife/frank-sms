package com.frank.ylear.modules.image;

import com.opensymphony.xwork2.ActionInvocation;

import com.opensymphony.xwork2.Result;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

/**
 * image Result
 * @author Administrator
 *
 */
public class ImageResult implements Result
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void execute(ActionInvocation ai) throws Exception
	{

		ImageAction action = (ImageAction) ai.getAction();

		HttpServletResponse response = ServletActionContext.getResponse();

		response.setHeader("Cash", "no cash");

		response.setContentType(action.getContentType());

		response.setContentLength(action.getContentLength());

		response.getOutputStream().write(action.getImageBytes());

		// response.getOutputStream().flush();

		response.getOutputStream().close();

	}

}
