package com.vasp.mm7.database;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;

import com.vasp.mm7.database.jdbc.DataSource;
import com.vasp.mm7.database.jdbc.JdbcTemplate;
import com.vasp.mm7.database.pojo.SubmitBean;

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

	public static JdbcTemplate getJdbcTemplate() throws Exception
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

	public static void main(String[] args) throws Exception
	{

		SubmitDaoImpl submitDao = SubmitDaoImpl.getInstance();
		int total=10000;
		List<SubmitBean> list=new ArrayList<SubmitBean>(total);
		long begin=System.currentTimeMillis();
		for(int i=0;i<total;i++)
		{
			//Thread.sleep(1000);
			SubmitBean submitBean = new SubmitBean();
			submitBean.setMessageid("053101435691006401333");
			submitBean.setTransactionid("1");
			submitBean.setMm7version("6.3.0");
			submitBean.setToAddress("13777802301");
			submitBean.setSubject("zzzz");
			submitBean.setSendtime("20120531014355");
			submitBean.setVaspid("895192");
			submitBean.setVasid("106573061704");
			submitBean.setServiceCode("1113329901");
			submitBean.setLinkid(null);
			submitBean.setStatus(1000);
			submitBean.setStatusText("·¢ËÍ³É¹¦");
			submitBean.setSessionid(144L);		
			list.add(submitBean);
			if(list.size()%10==0)
			{
				submitDao.save(list);
				list.clear();
			}
			//submitDao.update("053101435691006401333","13777802301","0330000000411120531015318510"
			//		,"20120531015321",2,"6400");
			
		}
		
		long end=System.currentTimeMillis();
		log.info("jdbc insert "+total+" records takes "+(end-begin)+"ms");
		//log.info(row+" rows success");

		

	}

}
