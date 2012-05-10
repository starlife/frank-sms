package com.vasp.mm7.frame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.common.MMConstants;
import com.cmcc.mm7.vasp.conf.MM7Config;
import com.cmcc.mm7.vasp.message.MM7DeliverReq;
import com.cmcc.mm7.vasp.message.MM7DeliverRes;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportReq;
import com.cmcc.mm7.vasp.message.MM7DeliveryReportRes;
import com.cmcc.mm7.vasp.message.MM7ReadReplyReq;
import com.cmcc.mm7.vasp.message.MM7ReadReplyRes;
import com.cmcc.mm7.vasp.message.MM7VASPRes;
import com.cmcc.mm7.vasp.service.MM7Receiver;
import com.vasp.mm7.database.pojo.DeliverBean;
import com.vasp.mm7.database.pojo.SLogMmsreadreply;
import com.vasp.mm7.database.pojo.SLogMmsreport;
import com.vasp.mm7.util.DateUtils;

public class Receiver extends MM7Receiver
{

	private static final Log log = LogFactory.getLog(Receiver.class);

	private static final Log db = LogFactory.getLog("db");

	/**
	 * 数据库访问对象
	 */
	// private SubmitDaoImpl dao = SubmitDaoImpl.getInstance();

	public Receiver(MM7Config config)
	{
		super(config);
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

	public void dealDeliver(MM7DeliverReq deliverReq)
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

	public void dealDeliveryReport(MM7DeliveryReportReq deliverReportReq)
	{
		// 这里更新s_log_mmssubmit表
		/*
		 * String messageid=deliverReportReq.getMessageID();
		 * if(!Tools.isBlank(messageid)) { SubmitBean
		 * bean=dao.getSubmitBeanByMessageid(messageid); if(bean!=null) {
		 * bean.setDeliverreportTime(deliverReportReq.getTimeStamp());
		 * bean.setMmstatus((int)deliverReportReq.getMMStatus());
		 * bean.setMmstatustext(deliverReportReq.getStatusText());
		 * dao.update(bean); } }
		 */
		// 这里插入记录到s_log_mmsreport
		SLogMmsreport reportBean = new SLogMmsreport();
		reportBean.setMessageid(deliverReportReq.getMessageID());
		reportBean.setTrasactionid(deliverReportReq.getTransactionID());
		reportBean.setDateTime(deliverReportReq.getTimeStamp());
		reportBean.setMm7version(deliverReportReq.getMM7Version());
		reportBean.setMmstatus((int) deliverReportReq.getMMStatus());
		reportBean.setMmstatustext(deliverReportReq.getStatusText());
		reportBean.setSenderAddress(deliverReportReq.getSender());
		reportBean.setRecipientAddress(deliverReportReq.getRecipient());
		// dao.save(reportBean);
		log.info(deliverReportReq);
	}

	public void dealReadReply(MM7ReadReplyReq mm7ReadReplyReq)
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
		SLogMmsreadreply readreplyBean = new SLogMmsreadreply();
		readreplyBean.setMessageid(mm7ReadReplyReq.getMessageID());
		readreplyBean.setTrasactionid(mm7ReadReplyReq.getTransactionID());
		readreplyBean.setDateTime(mm7ReadReplyReq.getTimeStamp());
		readreplyBean.setMm7version(mm7ReadReplyReq.getMM7Version());
		readreplyBean.setReadstatus((int) mm7ReadReplyReq.getMMStatus());
		readreplyBean.setReadstatustext(mm7ReadReplyReq.getStatusText());
		readreplyBean.setSenderAddress(mm7ReadReplyReq.getSender());
		readreplyBean.setRecipientAddress(mm7ReadReplyReq.getRecipient());
		// dao.save(readreplyBean);
		db.info(readreplyBean);
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
