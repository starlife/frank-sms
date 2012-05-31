package com.vasp.mm7.database.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlUpdate
{
	private String tableName;
	private final List<String> set = new ArrayList<String>();
	private final List<String> where = new ArrayList<String>();

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public void addSetField(String name, Object value)
	{
		set.add(SqlHelper.field(name, value));
	}

	public void addWhereField(String name, Object value)
	{
		where.add(SqlHelper.field(name, value));
	}

	public String toString()
	{
		// update lyear.dbo.u_mms set status=1 where status=0 and sendtime<?
		StringBuffer sb = new StringBuffer();
		sb.append("update ").append(tableName);
		if (set.size() > 0)
		{
			sb.append(" set ");
			for (int i = 0; i < set.size(); i++)
			{
				sb.append(set.get(i));
				if (i < set.size() - 1)
				{
					sb.append(",");
				}
			}
		}

		if (where.size() > 0)
		{
			sb.append(" where ");
			for (int i = 0; i < where.size(); i++)
			{
				sb.append(where.get(i));
				if (i < where.size() - 1)
				{
					sb.append(" and ");
				}
			}
		}
		return sb.toString();
	}

}
