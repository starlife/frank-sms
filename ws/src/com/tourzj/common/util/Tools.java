package com.tourzj.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

public class Tools
{
	// private static Random rand = new Random(1000);

	public static final int BUFSIZE = 1024;

	
	public static String parse(String numbers)
	{
		String[] nums = numbers.split("[;]");
		StringBuffer sb = new StringBuffer();
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < nums.length; i++)
		{
			String number = nums[i].trim();
			if (isValidPhoneNum(number))
			{
				set.add(number);
				// sb.append(number).append(",");
			}
		}
		Iterator<String> it = set.iterator();
		while (it.hasNext())
		{
			sb.append(it.next()).append(",");
		}
		if (sb.length() > 1)
		{
			
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
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
	
	public static String getRandomFileName(String prefix,String fileName)
	{
		Random rand=new Random();
		String temp=String.valueOf(rand.nextInt(1000));
		String ext=Tools.getFileExt(fileName);
		return prefix+temp +"."+ext;
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

	public static boolean isValidPhoneNum(String phone)
	{
		String regex = "^((\\+86)|(86))?(1)\\d{10}$";
		return Pattern.matches(regex, phone);
	}

	public static void main(String[] args)
	{
		Set<String> set=new HashSet<String>();
		for(int i=0;i<100;i++)
		{
			String file=getRandomFileName("IMG_","1.jpeg");
			System.out.println(file);
			set.add(file);
		}
		System.out.println(set.size());
	}

}
