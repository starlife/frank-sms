package com.unicom.mm7.rmi;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.frame.Result;
import com.unicom.mm7.util.ObjectUtils;

public class MmsServiceImpl extends UnicastRemoteObject implements MmsService
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(MmsServiceImpl.class);

	/**
	 * 因为UnicastRemoteObject的构造方法抛出了RemoteException异常，因此这里默认的构造方法必须写，必须声明抛出RemoteException异常
	 * 
	 * @throws RemoteException
	 */
	public MmsServiceImpl() throws RemoteException
	{
	}

	@Override
	public boolean sendMms(UMms mms) throws RemoteException
	{
		// TODO Auto-generated method stub
		log.info(mms);
		return com.unicom.mm7.frame.Sender.mmsQue.offer(mms);
	}

	@Override
	public UMms getMms(String sendid) throws RemoteException
	{
		// TODO Auto-generated method stub
		UMms mms = null;
		try
		{
			File dir = Result.getMmsDir();
			File f = new File(dir.getName() + "/" + sendid);
			Object obj = ObjectUtils.readObject(f);
			if (obj != null && (obj instanceof UMms))
			{
				mms = (UMms) obj;
			}
		}
		catch (Exception ex)
		{
			log.error(null, ex);
		}
		return mms;
	}

}
