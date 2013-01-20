package com.ylear.sp.sgip.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SmsService extends Remote
{
	public boolean sendSms(String sendid, String msgContent, String recipient)
			throws RemoteException;

}
