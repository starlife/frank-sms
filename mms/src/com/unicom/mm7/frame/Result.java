package com.unicom.mm7.frame;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.util.ObjectUtils;

/**
 * 该类可以看做是一个缓冲类，先通知消息给对方，如果通知失败，写文件 该类是一个单实例类
 * 改为用uuid  2012-12-22
 * @author Administrator
 */
public class Result
{
	public static final String MO_DIR = "MO";// MO彩信保存目录

	//private static int sendid = 100000; // 生成sendid用的
	private static String TIMESTAMP = ""; // 保存时间戳用的

	private static final Log log = LogFactory.getLog(Result.class);

	private static final Log resultLog = LogFactory.getLog("result");
	private static final Result result = new Result();

	private Result()
	{

	}

	/**
	 * @return
	 */
	public static Result getInstance()
	{
		return result;
	}

	/**
	 * 处理提交失败的消息
	 * 
	 * @param sendid
	 * @param to
	 * @param resultCode
	 */
	public void notifyResult(String sendid, List<String> to, int resultCode)
	{
		for (int i = 0; i < to.size(); i++)
		{
			notifyResult(sendid, to.get(i), resultCode);
		}
	}

	/**
	 * 接收发送结果
	 * 
	 * @param sendid
	 * @param mobile
	 * @param resultCode
	 */
	public void notifyResult(String sendid, String mobile, int resultCode)
	{
		resultLog.info("sendid:" + sendid + "|mobile:" + mobile
				+ "|resultCode:" + resultCode);
	}

	/**
	 * 接收状态报告
	 * 
	 * @param sendid
	 * @param mobile
	 * @param mmStatus
	 * @param mmStatusText
	 * @param reportTime
	 */
	public void notifyResult(String sendid, String mobile, int mmStatus,
			String mmStatusText, String reportTime)
	{
		resultLog.info("sendid:" + sendid + "|mobile:" + mobile + "|mmStatus:"
				+ mmStatus + "|mmStatusText:" + mmStatusText + "|reportTime:"
				+ reportTime);
	}

	/**
	 * 接收MO彩信
	 * 
	 * @param mms
	 */
	public void notifyResult(UMms mms)
	{
		// 先生成sendid，然后保存文件，然后发送通知消息给ws
		// 如果发送通知消息失败，那么写离线日志
		//genSendid(mms);
		mms.setSendID(java.util.UUID.randomUUID().toString());
		if (writeObject(mms))
		{
			;// 通知ws
		}
	}

	private boolean writeObject(UMms mms)
	{
		try
		{
			File dir = getMmsDir();
			ObjectUtils.writeObject(new File(dir.getName() + "/"
					+ mms.getSendID()), mms);
			return true;
		}
		catch (Exception ex)
		{
			log.error(null, ex);
			return false;
		}

	}

	public static File getMmsDir()
	{
		File dir = new File(MO_DIR);
		// 如果是监测删除时间，那么执行删除操作
		String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
		if (!TIMESTAMP.equals(timestamp))
		{
			if (dir.isFile() && dir.exists())
			{
				// 彩信文件保留5天
				long now = System.currentTimeMillis();
				long during = 5 * 24 * 3600 * 1000;// 5天

				File[] files = dir.listFiles();
				for (File f : files)
				{

					if (f.lastModified() < now - during)
					{
						f.delete();
					}
				}
			}
			TIMESTAMP = timestamp;

		}
		if (!dir.exists())
		{
			dir.mkdirs();

		}
		return dir;
	}

	/**
	 * 生成每一个MO彩信的sendid，需要保证唯一性 生成算法为：yyyyMMddHHmmss+6位递增整数 总20位
	 * 
	 * @param mms
	 * @return
	 */
	/*public void genSendid(UMms mms)
	{
		String timestamp = mms.getSendtime();
		String sendid = timestamp + allocID();
		mms.setSendID(sendid);

	}*/

	/*public synchronized String allocID()
	{
		sendid = (sendid + 1);
		if (sendid >= 200000)
		{
			sendid = 100000;
		}
		return String.valueOf(sendid);
	}*/

}
