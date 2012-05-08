package com.frank.ylear.common.interceptor;

import java.util.Map;

import com.frank.ylear.modules.login.action.LoginAction;
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
		  Map session = ctx.getSession();
		  Action action = (Action) actioninvocation.getAction();
		  if (action instanceof LoginAction) {
		   return actioninvocation.invoke();
		  }
		  String userName = (String) session.get("USER_NAME");
		  if (userName == null) {
		   return Action.LOGIN;
		  } else {
		   return actioninvocation.invoke();
		  }
		 

	}

}
