/**
 * File Name:MMContentType.java Company: �й��ƶ����Ź�˾ Date : 2004-1-31
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
	 * ���췽��
	 */
	public MMContentType()
	{
		strPrimaryType = "";
		strSubType = "";
		bLock = false;
		// ParmeterList = new HashMap<String, String>();
	}

	/**
	 * ���췽��
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
			System.err.println("�����Ͳ��Ǳ�׼���ͣ�type=" + type);
	}

	/**
	 * ���������
	 */
	public String getPrimaryType()
	{
		return (this.strPrimaryType);
	}

	/**
	 * ����������
	 */
	public void setPrimaryType(String primaryType)
	{
		this.strPrimaryType = primaryType;
	}

	/**
	 * ������������
	 */
	public MMContentType lock()
	{
		bLock = true;
		return this;
	}

	/**
	 * ���������Ƿ�����
	 */
	public boolean isLock()
	{
		return (bLock);
	}

	/**
	 * ����������
	 */
	public void setSubType(String subType)
	{
		this.strSubType = subType;
	}

	/**
	 * ���������
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
	 * �Ƚ����������������Ƿ�һ��
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
			System.err.println("�����Ͳ��Ǳ�׼���ͣ�type=" + type);
			return (false);
		}
	}

	/**
	 * ���ض�����ı���ʾ
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getPrimaryType()).append("/").append(getSubType());
		return sb.toString();
	}
}
