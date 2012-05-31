package com.vasp.mm7.database;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.vasp.mm7.database.jdbc.DataSource;
import com.vasp.mm7.database.jdbc.JdbcTemplate;

public class DbUtil
{
	private static final Log log = LogFactory.getLog(DbUtil.class);
	private static JdbcTemplate template = null;

	private static void init() throws Exception
	{
		DataSource ds = new DataSource();
		URL url = DbUtil.class.getClassLoader().getResource("hibernate.cfg.xml");
		readJDBCConfig(url, ds);
		template = new JdbcTemplate(ds);
	}

	private static void readJDBCConfig(URL url, DataSource ds) throws Exception
	{

		SAXBuilder sax = new SAXBuilder();

		Document doc = sax.build(url);
		Element root = doc.getRootElement();
		Element sessionFactory = root.getChild("session-factory");
		List list = sessionFactory.getChildren("property");
		for (int i = 0; i < list.size(); i++)
		{
			Element property = (Element) list.get(i);
			if ("connection.driver_class".equals(property.getAttribute("name")
					.getValue()))
			{
				ds.setDriverClass(property.getTextTrim());
			}
			else if ("connection.url".equals(property.getAttribute("name")
					.getValue()))
			{
				ds.setUrl(property.getTextTrim());
			}
			else if ("connection.username".equals(property.getAttribute("name")
					.getValue()))
			{
				ds.setUsername(property.getTextTrim());
			}

			else if ("connection.password".equals(property.getAttribute("name")
					.getValue()))
			{
				ds.setPassword(property.getTextTrim());
			}

		}

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
		//int row = execute(sql);
		//System.out.println(row > 0);
		//String path="hibernate.cfg.xml";
		//FileInputStream input=new FileInputStream(path);
		//System.out.println(input);
		//System.out.println(.getPath());

		

	}

}
