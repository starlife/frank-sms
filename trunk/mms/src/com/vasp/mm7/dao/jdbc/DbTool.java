package com.vasp.mm7.dao.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DbTool
{
	private static final Log log = LogFactory
	.getLog(DbTool.class);
	public static void closeResultSet(ResultSet rs)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
				rs=null;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
	}
	
	public static void closeStatement(Statement stmt)
	{
		if (stmt != null)
		{
			try
			{
				stmt.close();
				stmt=null;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
	}
	public static void closeConnection(Connection conn)
	{
		if (conn != null)
		{
			try
			{
				conn.close();
				conn=null;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				log.error(null, e);
			}
		}
	}
	public static void main(String[] args)
	{
		DbTool db=new DbTool();
		LinkedBlockingQueue<DbTool> que=new LinkedBlockingQueue<DbTool>();
		for(int i=0;i<10;i++)
		{
			if(!que.contains(db))
			{
				que.add(db);
			}
		}
		
		System.out.println(que.size());
	}
}
