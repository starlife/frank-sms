/**
 * File Name:MMContentType.java Company: �й��ƶ����Ź�˾ Date : 2004-1-31
 */

package com.cmcc.mm7.vasp.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MMContentType implements Serializable, Cloneable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String strPrimaryType;
	private String strSubType;
	private boolean bLock;
	private Map<String, String> ParmeterList;

	/**
	 * ���췽��
	 */
	public MMContentType()
	{
		strPrimaryType = "";
		strSubType = "";
		bLock = false;
		ParmeterList = new HashMap<String, String>();
	}

	/**
	 * ���췽��
	 */
	public MMContentType(String type)
	{
		strPrimaryType = "";
		strSubType = "";
		bLock = false;
		ParmeterList = new HashMap<String, String>();

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
	 * ���ò���
	 */
	public void setParameter(String name, String value)
	{
		ParmeterList.put(name, value);
	}

	/**
	 * ��ò���
	 */
	public String getParameter(String name)
	{
		return ((String) ParmeterList.get(name));
	}

	/**
	 * �������в������б�
	 */
	public Map<String, String> etParameterList()
	{
		return (ParmeterList);
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

	/**
	 * �Ƚ����������������Ƿ�һ��
	 */
	public boolean match(MMContentType type)
	{
		String strPrimaryType = type.getPrimaryType().trim();
		String strSubType = type.getSubType().trim();
		if (strPrimaryType.equals(strSubType))
			return true;
		else
			return false;
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
		sb.append("strPrimaryType=" + strPrimaryType + "\n");
		sb.append("strSubType=" + strSubType + "\n");
		sb.append("bLock=" + bLock + "\n");
		System.out.println("ParmeterList=" + ParmeterList);
		if (ParmeterList != null)
		{
			Iterator<String> it = ParmeterList.keySet().iterator();
			int i = 0;
			while (it.hasNext())
			{
				sb.append("ParmeterList[" + i + "]="
						+ ParmeterList.get(it.next()) + "\n");
			}
		}
		return sb.toString();
	}
}
