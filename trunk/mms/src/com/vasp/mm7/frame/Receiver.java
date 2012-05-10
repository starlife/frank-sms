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
	 * ���ݿ���ʶ���
	 */
	// private SubmitDaoImpl dao = SubmitDaoImpl.getInstance();

	public Receiver(MM7Config config)
	{
		super(config);
	}

	/**
	 * ��дdeliver��Ϣ
	 */
	@Override
	public MM7VASPRes doDeliver(MM7DeliverReq request)
	{
		log.info("�յ��ֻ�" + request.getSender() + "�ύ����Ϣ������Ϊ��"
				+ request.getSubject());
		log.info("MMSC�ı�ʶ��Ϊ��" + request.getMMSRelayServerID());

		dealDeliver(request);

		// �ظ���Ϣ
		MM7DeliverRes mm7DeliverRes = new MM7DeliverRes();
		mm7DeliverRes.setTransactionID(request.getTransactionID());
		mm7DeliverRes.setServiceCode("��Ʒ����"); // ����ServiceCode����ѡ
		mm7DeliverRes.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		mm7DeliverRes.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return mm7DeliverRes;
	}

	/**
	 * ��дdeliver_report��Ϣ
	 */
	@Override
	public MM7VASPRes doDeliveryReport(MM7DeliveryReportReq mm7DeliveryReportReq)
	{
		// ����д����״̬�����Ĵ������
		dealDeliveryReport(mm7DeliveryReportReq);

		// �ظ���Ϣ
		MM7DeliveryReportRes res = new MM7DeliveryReportRes();
		res.setTransactionID(mm7DeliveryReportReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	/**
	 * ��дReadReply��Ϣ
	 */
	@Override
	public MM7VASPRes doReadReply(MM7ReadReplyReq mm7ReadReplyReq)
	{
		// ����д���ն������Ĵ������
		dealReadReply(mm7ReadReplyReq);

		// �ظ���Ϣ
		MM7ReadReplyRes res = new MM7ReadReplyRes();
		res.setTransactionID(mm7ReadReplyReq.getTransactionID());
		res.setStatusCode(MMConstants.RequestStatus.SUCCESS);
		res.setStatusText(MMConstants.RequestStatus.TEXT_SUCCESS);
		return res;
	}

	public void dealDeliver(MM7DeliverReq deliverReq)
	{
		// ���
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
		// �������s_log_mmssubmit��
		/*
		 * String messageid=deliverReportReq.getMessageID();
		 * if(!Tools.isBlank(messageid)) { SubmitBean
		 * bean=dao.getSubmitBeanByMessageid(messageid); if(bean!=null) {
		 * bean.setDeliverreportTime(deliverReportReq.getTimeStamp());
		 * bean.setMmstatus((int)deliverReportReq.getMMStatus());
		 * bean.setMmstatustext(deliverReportReq.getStatusText());
		 * dao.update(bean); } }
		 */
		// ��������¼��s_log_mmsreport
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
		// �������s_log_mmssubmit��
		/*
		 * String messageid=mm7ReadReplyReq.getMessageID();
		 * if(!Tools.isBlank(messageid)) { SubmitBean
		 * bean=dao.getSubmitBeanByMessageid(messageid); if(bean!=null) {
		 * bean.setReadyreplyTime(mm7ReadReplyReq.getTimeStamp());
		 * bean.setReadstatus((int)mm7ReadReplyReq.getMMStatus());
		 * bean.setReadstatustext(mm7ReadReplyReq.getStatusText());
		 * dao.update(bean); } }
		 */
		// ��������¼��s_log_mmsreadreply
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

	// Main����
	public static void main(String[] args) throws Exception
	{
		// ��ʼ��VASP
		MM7Config mm7Config = new MM7Config("./config/mm7Config.xml");
		// ����ConnConfig.xml�ļ���·��
		mm7Config.setConnConfigName("./config/ConnConfig.xml"); // �ر�
		// ����MyReceiver
		Receiver receiver = new Receiver(mm7Config);
		// ����������
		receiver.start();
		for (;;)
			;
	}

}
