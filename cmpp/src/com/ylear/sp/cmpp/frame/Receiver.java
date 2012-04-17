package com.ylear.sp.cmpp.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.PReceiver;
import com.chinamobile.cmpp2_0.protocol.message.DeliverMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitRespMessage;
import com.ylear.sp.cmpp.database.pojo.SLogsmssubmit;
import com.ylear.sp.cmpp.util.LogTools;

public class Receiver extends PReceiver
{
	private static final Log log = LogFactory.getLog(Receiver.class);
	
	public void doDeliver(DeliverMessage dm)
	{
		log.debug("method doDeliver() begin");
		//写上行消息到数据库中
		
		//记录日志
		if(LogTools.getPLog().isInfoEnabled())
		{
			LogTools.getPLog().info(dm);
		}
		
		log.debug("method doDeliver() end");
		
	}
	
	public void doReport(DeliverMessage dm) 
	{
		/*
		 * if (!DbLog.getLog().updateMT(deliver.report.id,
		 * deliver.report.getStatString(), deliver.report.getErrString())) {
		 * System.out.println("更新状态报告失败"); }
		 */
	}
	
	public void doSubmitResp(SubmitMessage sm,SubmitRespMessage srm)
	{
		//写上行消息到数据库中
		SLogsmssubmit bean=new SLogsmssubmit();
		bean.setMsgid(srm.getMsgId());
		//bean.setM
		//srm.getStatus();
		//记录日志
		if(LogTools.getPLog().isInfoEnabled())
		{
			LogTools.getPLog().info(sm);
		}
		/*if (!DbLog.getLog().insertMT(p, srm))
		{
			Print.getInstance().printout("写入日志数据库失败");
		}*/
	}
	
	
}
