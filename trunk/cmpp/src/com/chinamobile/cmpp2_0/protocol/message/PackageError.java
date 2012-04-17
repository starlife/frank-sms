package com.chinamobile.cmpp2_0.protocol.message;

/**
 * @author Administrator
 */
public class PackageError extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PackageError()
	{
	}

	public PackageError(String msg)
	{
		super(msg);
	}

}
