/**
 * File Name:MMContentType.java Company: 中国移动集团公司 Date : 2004-1-31
 */

package com.cmcc.mm7.vasp.common;

import java.io.Serializable;

public class MMContentType implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String strPrimaryType;
	private String strSubType;
	private boolean bLock;

	// private Map<String, String> ParmeterList;

	/**
	 * 构造方法
	 */
	public MMContentType()
	{
		strPrimaryType = "";
		strSubType = "";
		bLock = false;
		// ParmeterList = new HashMap<String, String>();
	}

	/**
	 * 构造方法
	 */
	public MMContentType(String type)
	{
		strPrimaryType = "";
		strSubType = "";
		bLock = false;
		// ParmeterList = new HashMap<String, String>();

		int index = type.indexOf("/");
		if (index > 0)
		{
			String strPraType = type.substring(0, index);
			String strSubType = type.substring(index + 1);
			setPrimaryType(strPraType);
			if (strSubType.indexOf(";") > 0)
				strSubType = strSubType.substring(0, strSubType.indexOf(";"));
			setSubType(strSubType);
		}
		else
			System.err.println("该类型不是标准类型！type=" + type);
	}

	/**
	 * 获得主类型
	 */
	public String getPrimaryType()
	{
		return (this.strPrimaryType);
	}

	/**
	 * 设置主类型
	 */
	public void setPrimaryType(String primaryType)
	{
		this.strPrimaryType = primaryType;
	}

	/**
	 * 锁定对象类型
	 */
	public MMContentType lock()
	{
		bLock = true;
		return this;
	}

	/**
	 * 对象类型是否锁定
	 */
	public boolean isLock()
	{
		return (bLock);
	}

	/**
	 * 设置子类型
	 */
	public void setSubType(String subType)
	{
		this.strSubType = subType;
	}

	/**
	 * 获得子类型
	 */
	public String getSubType()
	{
		return (this.strSubType);
	}

	public boolean equals(MMContentType type)
	{
		return type.getPrimaryType().equals(this.getPrimaryType())
				&& type.getSubType().equals(this.getSubType());
	}

	/**
	 * 比较主类型与子类型是否一致
	 */
	public boolean match(String type)
	{
		int index = type.indexOf("/");
		if (index > 0)
		{
			String strParType = type.substring(0, index);
			String strSubType = type.substring(index + 1);
			if (strParType.trim().equals(strPrimaryType))
			{
				if (strSubType.trim().equals(strSubType))
					return true;
				else
					return false;
			}
			else
				return false;
		}
		else
		{
			System.err.println("该类型不是标准类型！type=" + type);
			return (false);
		}
	}

	/**
	 * 返回对象的文本表示
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getPrimaryType()).append("/").append(getSubType());
		return sb.toString();
	}
}
