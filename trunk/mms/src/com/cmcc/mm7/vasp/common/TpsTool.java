package com.cmcc.mm7.vasp.common;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 流量控制工具
 * @author Administrator
 *
 */

public class TpsTool
{
	
	private static String curTime = "";// 保存当前时间yyyMMddHHmmss
	private static int tps = 0;// tps标记

	private static final Log log = LogFactory.getLog(TpsTool.class);// 日志

	public synchronized static void limitTPS()
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
			
			tps = 1;
			curTime = now;
		}
		log.debug("tps:" + tps);

	}
	
	public static void main(String[] args)
	{
		Constants.MAX_SPEED=2;
		
		while (true)
		{
			TpsTool.limitTPS();
			log.info("tps:" + tps);
		}
	}
}
