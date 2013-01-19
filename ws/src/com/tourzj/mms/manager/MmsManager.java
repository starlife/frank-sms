package com.tourzj.mms.manager;

import java.rmi.Naming;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.tourzj.common.Env;
import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.rmi.MmsService;

public class MmsManager {
	private static final Log log = LogFactory.getLog(MmsManager.class);

	public static boolean submit(UMms mms) {
		boolean ret = false;
		try {
			// 在RMI服务注册表中查找名称为RHello的对象，并调用其上的方法
			String address = Env.getEnv().getString("rmi_address");
			if (address == null) {
				log.error("取rmi地址rmi_address值为空");
				address = "rmi://localhost:8888/MmsService";
			}
			MmsService mmsSrv = (MmsService) Naming.lookup(address);
			ret = mmsSrv.sendMms(mms);
		} catch (Exception e) {
			log.error(null, e);
		}
		return ret;
	}

	public static UMms getMms(String sendId) {
		UMms mms = null;
		try {
			// 在RMI服务注册表中查找名称为RHello的对象，并调用其上的方法
			String address = Env.getEnv().getString("rmi_address");
			if (address == null) {
				log.error("取rmi地址rmi_address值为空");
				address = "rmi://localhost:8888/MmsService";
			}
			MmsService mmsSrv = (MmsService) Naming.lookup(address);
			mms = mmsSrv.getMms(sendId);
		} catch (Exception e) {
			log.error(null, e);
		}
		return mms;
	}
}
