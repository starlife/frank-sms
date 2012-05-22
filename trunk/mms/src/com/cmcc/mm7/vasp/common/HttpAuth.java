package com.cmcc.mm7.vasp.common;


public class HttpAuth
{
	//private static final Log log = LogFactory.getLog(HttpAuth.class);
	
	public static String getBasicReq()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("HTTP/1.1 401 Authorization Required\r\n");
		sb.append("WWW-Authenticate: Basic ");
		sb.append("realm=\"" + MMConstants.REALM + "\"\r\n\r\n");
		
		return sb.toString();	
	}

	

}
