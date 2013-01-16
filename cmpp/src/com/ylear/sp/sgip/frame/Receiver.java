package com.ylear.sp.sgip.frame;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamobile.cmpp2_0.protocol.PReceiver;
import com.chinamobile.cmpp2_0.protocol.message.DeliverMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitMessage;
import com.chinamobile.cmpp2_0.protocol.message.SubmitRespMessage;
import com.chinamobile.cmpp2_0.protocol.message.bean.Report;
import com.chinamobile.cmpp2_0.protocol.util.Constants;
import com.ylear.sp.cmpp.database.DeliverDaoImpl;
import com.ylear.sp.cmpp.database.SubmitDaoImpl;
import com.ylear.sp.cmpp.database.pojo.DeliverBean;
import com.ylear.sp.cmpp.database.pojo.SubmitBean;
import com.ylear.sp.cmpp.util.DateUtils;

/**
 * 短信接收类，接收到短信，并且入库
 * 
 * @version 1.0
 * @version 1.1 frank 支持群发
 * @author Administrator
 */
public class Receiver extends PReceiver
{
	private static final Log log = LogFactory.getLog(Receiver.class);

	private static final Log speed = LogFactory.getLog("speed");// 流量控制错

	private SubmitDaoImpl submitDao = SubmitDaoImpl.getInstance();
	private DeliverDaoImpl deliverDao = DeliverDaoImpl.getInstance();

	// private static final Log db = LogFactory.getLog("db");

	public void doDeliver(DeliverMessage dm)
	{
		DeliverBean bean = new DeliverBean();
		bean.setMsgid(dm.getDeliver().getMsgID());
		bean.setDestId(dm.getDeliver().getDestTermID());
		bean.setSrcId(dm.getDeliver().getSrcTermID());
		bean.setTpPid(dm.getDeliver().getTP_pid());
		bean.setTpUdhi(dm.getDeliver().getTP_udhi());
		bean.setServiceId(dm.getDeliver().getServiceID());
		bean.setMsgFmt(dm.getDeliver().getMsgFmt());
		bean.setMsgContent(dm.getDeliver().getMsgContent());
		bean.setMsgLength(dm.getDeliver().getMsgLength());
		bean.setRecvtime(DateUtils.getTimestamp14(dm.getTimeStamp()));
		deliverDao.save(bean);
		// db.info(bean);
		log.info("doDeliver ok");

	}

	public void doReport(DeliverMessage dm)
	{
		Report report = dm.getDeliver().getReport();
		String msgid = report.getMsg_Id();
		String stat = report.getStat();
		String submitTime = report.getSubmit_time();
		String doneTime = report.getDone_time();
		SubmitBean bean = submitDao.getSubmitBeanByMsgid(msgid);
		if (bean != null)
		{
			bean.setStat(stat);
			bean.setSubmitTime(submitTime);
			bean.setDoneTime(doneTime);
			submitDao.save(bean);
		}
		// db.info("Report:" + dm);
		log.info("doReport ok");
	}

	/**
	 * 把Submit消息入库，这里支持群发的处理
	 */
	public void doSubmitResp(SubmitMessage sm, SubmitRespMessage srm)
	{
		// 写Sumit消息到数据库中
		if (srm.getMsgId().equals("0000000000000000"))
		{
			// 流量控制错,需要重新发送这个SubmitMessage消息，并且记录下出现了这个错误
			Sender.que.offer(sm);
			speed.info("当前速率" + Constants.MAX_SPEED + "过大");
			return;
		}
		SubmitBean bean = new SubmitBean();
		bean.setMsgid(srm.getMsgId());
		bean.setPkTotal(sm.getSubmit().getPkTotal());
		bean.setPkNumber(sm.getSubmit().getPkNumber());
		bean.setMsgSrc(sm.getSubmit().getMsgSrc());
		bean.setSrcId(sm.getSubmit().getSrcTermID());
		bean.setDestId(StringUtils.join(sm.getSubmit().getDestTermID()));
		bean.setMsgFmt(sm.getSubmit().getMsgFmt());
		bean.setMsgLength(sm.getSubmit().getMsgLength());
		bean.setMsgContent(sm.getSubmit().getMsg());
		bean.setFeetype(sm.getSubmit().getFeeType());
		bean.setFeecode(sm.getSubmit().getFeeCode());
		bean.setServiceId(sm.getSubmit().getServiceID());
		bean.setSendtime(DateUtils.getTimestamp14(sm.getTimeStamp()));
		bean.setResultCode(srm.getStatus());
		bean.setResultStr(srm.getStatusStr());
		Long sessionid = null;
		synchronized (Sender.sessionMap)
		{
			sessionid = Sender.sessionMap.remove(sm.getHead().getSequenceId());
		}
		bean.setSessionid(sessionid);
		// db.info(bean);
		submitDao.save(bean);
		log.info("doSubmitResp ok");

	}

}
