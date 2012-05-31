package com.vasp.mm7.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.MM7Receiver;
import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.protocol.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.protocol.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.protocol.message.MM7VASPRes;
import com.vasp.mm7.conf.MM7Config;
import com.vasp.mm7.database.SubmitDaoImpl;
import com.vasp.mm7.database.pojo.DeliverBean;
import com.vasp.mm7.util.DateUtils;

public class Receiver extends MM7Receiver
{

	private static final Log log = LogFactory.getLog(Receiver.class);

	// private static final Log db = LogFactory.getLog("db");

	private SubmitDaoImpl submitDao = SubmitDaoImpl.getInstance();

	/**
	 * 数据库访问对象
	 */
	//private SubmitDaoImpl1 dao = SubmitDaoImpl1.getInstance();
	public Receiver(MM7Config config)
	{
		super(config.getListenIP(), config.getListenPort(),
				config.getBackLog(), config.isKeepAlive(), config.getTimeOut(),
				config.getCharSet());
	}

	/**
	 * 重写deliver消息
	 */
	@Override
	public MM7VASPRes doDeliver(MM7DeliverReq request)
	{
		log.info("收到手机" + request.getSender() + "提交的消息，标题为："
				+ request.getSubject());
		log.info("MMSC的标识符为：" + request.getMMSRelayServerID());

		dealDeliver(request);

		// 回复消息
		MM7DeliverRes mm7DeliverRes = new MM7DeliverRes();
		mm7DeliverRes.setTransactionID(request.getTransactionID());
		mm7DeliverRes.setServiceCode("产品代码"); // 设置ServiceCode，可选
		mm7DeliverRes.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		mm7DeliverRes.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return mm7DeliverRes;
	}

	/**
	 * 重写deliver_report消息
	 */
	@Override
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
	{
		// 这里写接收状态报告后的处理过程
		dealDeliveryReport(mm7DeliveryReportReq);

		// 回复消息
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	/**
	 * 重写ReadReply消息
	 */
	@Override
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// 这里写接收读报告后的处理过程
		dealReadReply(mm7ReadReplyReq);

		// 回复消息
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	private void dealDeliver(MM7DeliverReq deliverReq)
	{
		// 入库
		DeliverBean deliver = new DeliverBean();
		deliver.setTransactionid(deliverReq.getTransactionID());
		deliver.setLinkId(deliverReq.getLinkedID());
		deliver.setMm7version(deliverReq.getMM7Version());
		deliver.setSubject(deliverReq.getSubject());
		// deliver.setRecvTime(deliverReq.getTimeStamp());
		deliver.setRecvTime(DateUtils.getTimestamp14());
		// dao.save(deliver);
		log.info(deliverReq);
	}

	private void dealDeliveryReport(MM7DeliveryReportReq deliverReportReq)
	{
		// 这里更新s_log_mmssubmit表
		String messageid = deliverReportReq.getMessageID();
		String to = deliverReportReq.getRecipient();
		if (to.startsWith("+86"))
		{
			to = to.substring(3, to.length());
		}
		else if (to.startsWith("86"))
		{
			to = to.substring(2, to.length());
		}
		String transcationid=deliverReportReq.getTransactionID();
		String reportTime=DateUtils.getTimestamp14(deliverReportReq
				.getTimeStamp());
		Integer mmStatus=(int)deliverReportReq.getMMStatus();
		String mmStatusText=deliverReportReq.getStatusText();
		log.debug("submitDao.getSubmitBean 之前:"+System.currentTimeMillis());
		submitDao.update(messageid, to, transcationid, reportTime, mmStatus, mmStatusText);
		log.debug("submitDao.getSubmitBean 之后:"+System.currentTimeMillis());

	}

	private void dealReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// 这里更新s_log_mmssubmit表
		/*
		 * String messageid=mm7ReadReplyReq.getMessageID();
		 * if(!Tools.isBlank(messageid)) { SubmitBean
		 * bean=dao.getSubmitBeanByMessageid(messageid); if(bean!=null) {
		 * bean.setReadyreplyTime(mm7ReadReplyReq.getTimeStamp());
		 * bean.setReadstatus((int)mm7ReadReplyReq.getMMStatus());
		 * bean.setReadstatustext(mm7ReadReplyReq.getStatusText());
		 * dao.update(bean); } }
		 */
		// 这里插入记录到s_log_mmsreadreply
	}

	// Main方法
	public static void main(String[] args) throws Exception
	{
		// 初始化VASP
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		// 设置ConnConfig.xml文件的路径
		mm7Config.setConnConfigName("./config/ConnConfig.xml"); // 必备
		// 构造MyReceiver
		Receiver receiver = new Receiver(mm7Config);
		// 启动接收器
		receiver.start();
		for (;;)
			;
	}

}
