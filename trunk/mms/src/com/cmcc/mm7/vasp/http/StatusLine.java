package com.cmcc.mm7.vasp.http;

public class StatusLine
{
	private String httpVerion=null;
	private int statusCode=0;
	private String reasonPhrase=null;
	
	public StatusLine(String statusLine)
	{
		 init(statusLine);
	}
	
	private void init(String statusLine)
	{
		try
		{
			String[] temp=statusLine.split(" ");
			if(temp.length>=3)
			{
				httpVerion=temp[0];
				statusCode=Integer.parseInt(temp[1]);
				reasonPhrase=temp[2];
			}
		}
		catch(Exception ex)
		{
			//
		
		}
	}

	public String getHttpVerion()
	{
		return httpVerion;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public String getReasonPhrase()
	{
		return reasonPhrase;
	}
}
