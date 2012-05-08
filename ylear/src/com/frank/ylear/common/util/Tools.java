package com.frank.ylear.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Tools
{
	private static Random rand = new Random(1000);

	public static final int BUFSIZE = 1024;

	
	
	
	
	
	
	/**
	 * 得到一个随机的文件名，名字类似 number+rand.ext
	 * 
	 * @param number
	 * @param postfix
	 * @return
	 */
	public static String getRandomFileName(String frameid, String ext)
	{
		String r = String.valueOf(rand.nextInt());
		if (r.length() > 4)
		{
			r = r.substring(1, r.length());
		}

		return frameid + "_" + r + "." + ext;
	}

	public static String getRandomFileName(String frameid, File originalfileName)
	{
		String ext = Tools.getFileExt(originalfileName);
		String r = String.valueOf(rand.nextInt());
		if (r.length() > 4)
		{
			r = r.substring(1, r.length());
		}

		return frameid + "_" + r + "." + ext;
	}

	public static String getRandomFileName(int frameid, String originalfileName)
	{
		String ext = Tools.getFileExt(originalfileName);
		String r = String.valueOf(rand.nextInt());
		if (r.length() > 4)
		{
			r = r.substring(1, r.length());
		}

		return frameid + "_" + r + "." + ext;
	}

	public static boolean isNotEmpty(String str)
	{
		return (str != null) && (!str.trim().equals(""));
	}

	public static String getFileExt(File f)
	{
		String ext = "";
		String filename = f.getName();
		int index = filename.lastIndexOf(".");
		if (index > 0)
		{
			ext = filename.substring(index + 1);
		}
		return ext;
	}

	public static String getFileExt(String file)
	{
		return getFileExt(new File(file));
	}

	public static boolean copyFile(File src, File dest)
	{
		boolean flag = true;
		FileInputStream input = null;
		FileOutputStream output = null;
		try
		{
			input = new FileInputStream(src);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[BUFSIZE];
			int len = 0;
			while ((len = input.read(buf)) != -1)
			{
				output.write(buf, 0, len);
			}
			buf = null;

		}
		catch (IOException ex)
		{
			flag = false;
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if (input != null)
				{
					input.close();
				}
				if (output != null)
				{
					output.close();
				}
			}
			catch (IOException ex)
			{
				flag = false;
				ex.printStackTrace();
			}
		}
		return flag;

	}

}
