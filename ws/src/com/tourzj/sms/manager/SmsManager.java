package com.tourzj.sms.manager;

import java.rmi.Naming;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.frank.sp.sgip.rmi.SmsService;
import com.tourzj.common.Env;

public class SmsManager {
	private static final Log log = LogFactory.getLog(SmsManager.class);

	public static boolean submit(String sendid,String msgContent,String recipient) {
		boolean ret = false;
		try {
			// 在RMI服务注册表中查找名称为RHello的对象，并调用其上的方法
			String address = Env.getEnv().getString("rmi_smsaddress");
			if (address == null) {
				log.error("取rmi地址rmi_address值为空");
				address = "rmi://localhost:7777/SmsService";
			}
			SmsService smsSrv = (SmsService) Naming.lookup(address);
			ret = smsSrv.sendSms(sendid,msgContent,recipient);
			ret=true;
		} catch (Exception e) {
			log.error(null, e);
		}
		return ret;
	}
	public static void main(String[] args) {
		System.out.println(submit("sendid","msg","13777802386"));
	}

	
}
