package com.unicom.mm7.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.unicom.mm7.bean.UMms;

public class Test
{
	public static void main(String[] args)
	{
		try { 
            //��RMI����ע����в�������ΪRHello�Ķ��󣬲��������ϵķ��� 
			MmsService mmsSrv =(MmsService) Naming.lookup("rmi://localhost:8888/MmsService"); 
            System.out.println(mmsSrv.sendMms(new UMms("1","2","3"))); 
        } catch (NotBoundException e) { 
            e.printStackTrace(); 
        } catch (MalformedURLException e) { 
            e.printStackTrace(); 
        } catch (RemoteException e) { 
            e.printStackTrace();   
        } 
	}
}
