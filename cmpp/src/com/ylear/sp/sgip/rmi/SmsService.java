package com.ylear.sp.sgip.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SmsService extends Remote
{
	public boolean sendSms(String msg,String to) throws RemoteException;

}
