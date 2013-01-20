package com.ylear.sp.sgip.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ObjectUtils
{
	private static final Log log = LogFactory.getLog(ObjectUtils.class);

	public static void writeObject(File file, Object obj) throws IOException
	{
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(new FileOutputStream(file));
			writeObject(out, obj);

		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException ex)
				{
					log.error(null, ex);
				}
			}
		}
	}

	public static Object readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException
	{
		return in.readObject();
	}

	public static Object readObject(File file) throws IOException,
			ClassNotFoundException
	{
		ObjectInputStream in = null;
		try
		{
			in = new ObjectInputStream(new FileInputStream(file));
			return readObject(in);

		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException ex)
				{
					log.error(null, ex);
				}
			}
		}
	}

	public static void writeObject(ObjectOutputStream out, Object obj)
			throws IOException
	{
		out.writeObject(obj);

	}

}
