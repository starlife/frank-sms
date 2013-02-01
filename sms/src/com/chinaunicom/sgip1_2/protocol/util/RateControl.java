package com.chinaunicom.sgip1_2.protocol.util;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 浏览控制工具类
 * 
 * @author Administrator
 */
public class RateControl
{
	// 流量控制
	private static String curTime = "";// 格式为yyyyMMddHHmmss
	private static int tps = 0;// 当前第几条发送消息

	private static final Log log = LogFactory.getLog(RateControl.class);// 记录日志

	public static void controlRate()
	{
		String now = DateUtil.getTimeStamp();
		if (curTime.equals(now))
		{
			tps++;
			if (tps >= Constants.MAX_SPEED)
			{
				while (curTime.equals(now))
				{
					try
					{
						TimeUnit.MICROSECONDS.sleep(5);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						log.error(null, e);
					}
					now = DateUtil.getTimeStamp();
				}
			}
		}
		else
		{
			// 新的1s开始，重新初始化tps
			tps = 1;
			curTime = now;
		}

	}

	public static void main(String[] args)
	{
		while (true)
		{
			controlRate();
			log.info("tps:" + tps);
		}
	}
}
