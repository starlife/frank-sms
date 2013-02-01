package com.vasp.mm7.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.vasp.mm7.dao.buffer.InsertBufLayer;
import com.vasp.mm7.dao.buffer.UpdateBufLayer;
import com.vasp.mm7.database.pojo.SubmitBean;

/**
 * 修改为jdbc实现
 * 
 * @author Administrator
 */
public class SubmitDaoImpl
{
	private static final Log log = LogFactory.getLog(SubmitDaoImpl.class);

	private static SubmitDaoImpl dao = new SubmitDaoImpl();
	
	private InsertBufLayer iBufLayer=new InsertBufLayer();
	
	private UpdateBufLayer uBufLayer=new UpdateBufLayer();
	
	private SubmitDaoImpl()
	{

	}

	public static SubmitDaoImpl getInstance()
	{
		return dao;
	}

	public void save(List<SubmitBean> list) throws Exception
	{
		iBufLayer.add(list.iterator());
		iBufLayer.commit();
		
		
	}

	/**
	 * messageid和to唯一的确定一条记录
	 * 
	 * @param messageid
	 * @param to
	 * @return
	 * @throws Exception
	 */
	public void update(String messageid, String to, String transcationid,
			String reportTime, Integer mmStatus, String mmStatusText)
			throws Exception
	{
		SubmitBean submitBean=new SubmitBean();
		submitBean.setTransactionid(transcationid);
		submitBean.setToAddress(to);
		submitBean.setMessageid(messageid);
		submitBean.setReportTime(reportTime);
		submitBean.setMmStatus(mmStatus);
		submitBean.setMmStatusText(mmStatusText);
		
		uBufLayer.add(submitBean);
	}
	
	
	public void mystop()
	{
		uBufLayer.myStop();
	}
	
	public static void main(String[] args) throws Exception
	{
		SubmitDaoImpl dao =SubmitDaoImpl.getInstance();
		int total=10000;
		
		List<SubmitBean> list=new ArrayList<SubmitBean>();
		long begin = System.currentTimeMillis();
		for(int i=0;i<total;i++)
		{			
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
			submitBean.setStatusText("发送成功");
			submitBean.setSessionid(144L);	
			list.add(submitBean);
			if(list.size()%10==0)
			{
				dao.save(list);
				list.clear();
				//java.util.concurrent.TimeUnit.MILLISECONDS.sleep(2000);
			}
		}
		long end = System.currentTimeMillis();
		log.info("jdbc insert " + total + " records takes "
				+ (end - begin) + "ms");
		
	}

}
