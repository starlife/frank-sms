package com.cmcc.mm7.vasp.util;

/**
 * MD5加密工具�?
 * @author Administrator
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{
	public final static String[] hexDigits={
		"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"
	};

	public static String byte2HexString(byte b){
		return hexDigits[(b>>4)&0xf]+hexDigits[b&0xf];
	}

	public static String byteArray2HexString(byte[] input){
		StringBuffer sb=new StringBuffer();
		for(byte b:input)
			sb.append(byte2HexString(b));
		return sb.toString();
	}

	public static String compile(String input){
        String resultString=null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString=byteArray2HexString(md.digest(input.getBytes()));
		}
		catch(NoSuchAlgorithmException e){

		}
		return resultString;

	}

	public static byte[] compile(byte[] buf){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(buf);
		}
		catch(NoSuchAlgorithmException e){
			return null;

		}

	}



}

