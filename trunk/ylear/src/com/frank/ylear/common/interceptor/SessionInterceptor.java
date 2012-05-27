package com.frank.ylear.common.interceptor;

import com.frank.ylear.common.constant.Constants;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class SessionInterceptor  extends AbstractInterceptor
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation actioninvocation) throws Exception
	{
		// TODO Auto-generated method stub
		 ActionContext ctx = ActionContext.getContext();
		  Object user = ctx.getSession().get(Constants.SESSION_USER);
		  if (user == null) {
		   return Action.LOGIN;
		  } else {
		   return actioninvocation.invoke();
		  }
		 

	}

}
