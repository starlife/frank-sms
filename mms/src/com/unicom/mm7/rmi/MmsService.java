package com.unicom.mm7.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.unicom.mm7.bean.UMms;


public interface MmsService extends Remote { 

    
	/**
	 * 彩信下行
	 * @param mms 彩信
	 * @return
	 * @throws RemoteException
	 */
    public boolean sendMms(UMms mms) throws RemoteException; 
    
    public UMms getMms(String sendid) throws RemoteException; 
}