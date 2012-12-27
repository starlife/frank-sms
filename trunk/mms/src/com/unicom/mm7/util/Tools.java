package com.unicom.mm7.util;

public class Tools
{
	/**
	 * @param b
	 * @return
	 */
	public static int bool2int(boolean b)
	{
		if (b)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public static String stringPlus(String number1, int number2)
	{
		String ret = null;
		try
		{
			ret = String.valueOf(Integer.valueOf(number1) + number2);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return ret;
	}

	public static boolean isBlank(String str)
	{

		return (null == str) || "".equals(str.trim());
	}
	public static void main(String[] args)
	{
		System.out.println(java.util.UUID.randomUUID());
	}
}
