package com.frank.ylear.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.frank.ylear.common.constant.Constants;

public class FileUtil
{
	
	/**
	 * 得到流的数据
	 * @param in
	 * @return
	 */
	public static  String getData(InputStream in,String charset)
	{
		String data=null;
		try
		{
			StringBuffer sb=new StringBuffer();
			BufferedReader reader=new BufferedReader(
					new InputStreamReader(in,charset));
			String line;
			while((line=reader.readLine())!=null)
			{
				sb.append(line+Constants.NEWLINE);
			}
			data=sb.toString();
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}	
	
	/**
	 *  写流数据到文件中
	 * @param in
	 * @param f
	 */
	public static  void saveData(InputStream in,File f)
	{
		FileOutputStream output=null;
		try
		{
			output=new FileOutputStream(f);
			byte[] buf=new byte[102400];
			int len=0;
			while((len=in.read(buf))!=-1)
			{
				output.write(buf,0,len);
			}	
			buf=null;
		}catch(IOException ex)
		{
			ex.printStackTrace();
		}finally
		{
			try
			{
				in.close();
				if(output!=null)
				{
					output.close();
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	

}
