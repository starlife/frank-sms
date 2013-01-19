/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.tourzj.common;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Administrator
 */
public class Env
{

	private static final Log log = LogFactory.getLog(Env.class);

	private static Env cfg = new Env();

	private Properties pps = new Properties();

	private Env()
	{
		load();

	}

	public static Env getEnv()
	{
		return cfg;
	}

	

	private void load()
	{
		try
		{
			InputStream inputStream = getClass().getResourceAsStream(
					"/config.properties");
			pps.load(inputStream);			

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(null, e);
		}
		log.info("∂¡»°≈‰÷√Œƒº˛£∫"+pps.toString());

	}
	
	public String getString(String key)
	{
		return pps.getProperty(key);
	}

	public String toString()
	{

		return pps.toString();

	}

	public static void main(String[] args) {
		System.out.println(Env.getEnv().getString("rmi_address"));
		//System.out.println(Env.getEnv());
	}

	

}
