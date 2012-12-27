package com.unicom.mm7.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtil
{
	private static final Log log = LogFactory.getLog(FileUtil.class);

	public static String getData(File f, String charset)
	{
		String str = null;
		try
		{
			str = getData(new FileInputStream(f), charset);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			log.error(null, e);

		}
		return str;
	}

	/**
	 * 读取inputSteam内容
	 * 
	 * @param in
	 * @return
	 */
	public static String getData(InputStream in, String charset)
	{
		String data = null;
		try
		{
			StringBuffer sb = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, charset));
			String line;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + Constants.NEWLINE);
			}
			data = sb.toString();

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(null, e);
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
		return data;
	}

	/**
	 * 保存文件
	 * 
	 * @param in
	 * @param f
	 */
	public static void saveData(File f, byte[] bytes)
	{
		FileOutputStream output = null;
		try
		{
			output = new FileOutputStream(f);
			output.write(bytes);
		}
		catch (IOException ex)
		{
			log.error(null, ex);
		}
		finally
		{
			if (output != null)
			{

				try
				{

					output.close();

				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					log.error(null, e);
				}
			}
		}

	}

	/**
	 * 保存文件
	 * 
	 * @param in
	 * @param f
	 */
	public static void saveData(InputStream in, File f)
	{
		FileOutputStream output = null;
		try
		{
			output = new FileOutputStream(f);
			byte[] buf = new byte[102400];
			int len = 0;
			while ((len = in.read(buf)) != -1)
			{
				output.write(buf, 0, len);
			}
			buf = null;
		}
		catch (IOException ex)
		{
			log.error(null, ex);
		}
		finally
		{
			try
			{
				in.close();
				if (output != null)
				{
					output.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}

	}

	/**
	 * 保存文件
	 * 
	 * @param in
	 * @param f
	 */
	public static byte[] readData(InputStream in)
	{
		// byte[] bytes=null;
		ByteArrayOutputStream output = null;
		try
		{
			output = new ByteArrayOutputStream();
			byte[] buf = new byte[102400];
			int len = 0;
			while ((len = in.read(buf)) != -1)
			{
				output.write(buf, 0, len);
			}
			buf = null;
			return output.toByteArray();
		}
		catch (IOException ex)
		{
			log.error(null, ex);
			return null;
		}
		finally
		{
			try
			{
				in.close();
				if (output != null)
				{
					output.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}

	}

}
