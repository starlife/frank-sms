package com.frank.sp.sgip.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.frank.sp.sgip.bean.USms;

public class SmsServiceImpl extends UnicastRemoteObject implements SmsService
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(SmsServiceImpl.class);

	/**
	 * 因为UnicastRemoteObject的构造方法抛出了RemoteException异常，因此这里默认的构造方法必须写，必须声明抛出RemoteException异常
	 * 
	 * @throws RemoteException
	 */
	public SmsServiceImpl() throws RemoteException
	{
	}

	@Override
	public boolean sendSms(String sendid, String msgContent, String recipient)
			throws RemoteException
	{
		// TODO Auto-generated method stub
		USms bean = new USms();
		bean.setSendid(sendid);
		bean.setMsgContent(msgContent);
		bean.setRecipient(recipient);
		log.debug("rmi接口收到短信：" + bean);
		return com.frank.sp.sgip.frame.Sender.smsQue.offer(bean);
	}

}
