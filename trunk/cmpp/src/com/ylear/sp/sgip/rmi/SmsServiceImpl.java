package com.ylear.sp.sgip.rmi;

import java.rmi.RemoteException;

import com.ylear.sp.sgip.bean.USms;

public class SmsServiceImpl implements SmsService
{

	@Override
	public boolean sendSms(String sendid,String msgContent, String recipient) throws RemoteException
	{
		// TODO Auto-generated method stub
		USms bean=new USms();
		bean.setSendid(sendid);
		bean.setMsgContent(msgContent);
		bean.setRecipient(recipient);
		return com.ylear.sp.sgip.frame.Sender.smsQue.offer(bean);
	}

}
