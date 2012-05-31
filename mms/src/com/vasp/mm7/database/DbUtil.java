package com.vasp.mm7.database;

import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;

import com.vasp.mm7.database.jdbc.DataSource;
import com.vasp.mm7.database.jdbc.JdbcTemplate;

public class DbUtil
{
	private static final Log log = LogFactory.getLog(DbUtil.class);
	private static JdbcTemplate template = null;

	private static void init() throws Exception
	{
		DataSource ds = new DataSource();
		Configuration config=HibernateSessionFactory.getConfiguration();
		ds.setDriverClass(config.getProperty("connection.driver_class"));
		ds.setUrl(config.getProperty("connection.url"));
		ds.setUsername(config.getProperty("connection.username"));
		ds.setPassword(config.getProperty("connection.password"));
		template = new JdbcTemplate(ds);
	}

	private static JdbcTemplate getJdbcTemplate() throws Exception
	{
		if (template == null)
		{
			init();
		}
		return template;
	}

	public static int execute(String sql)
	{

		try
		{
			return getJdbcTemplate().update(sql);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			log.error(null,e);
			return 0;
		}
	}

	public static void main(String[] args) throws FileNotFoundException
	{

		String msgContent = "qqqqqq";
		String to = "13777802386";
		String timestamp = "20110512000000";
		String sql = "insert into lyear.dbo.u_sms(msg_content,recipient,sendtime,status) VALUES ('"
				+ msgContent + "','" + to + "','" + timestamp + "',0)";
		int row = execute(sql);
		//System.out.println(row > 0);
		//String path="hibernate.cfg.xml";
		//FileInputStream input=new FileInputStream(path);
		//System.out.println(input);
		//System.out.println(.getPath());
		System.out.println(row);

		

	}

}
