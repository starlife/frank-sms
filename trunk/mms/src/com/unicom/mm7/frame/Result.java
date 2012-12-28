package com.unicom.mm7.frame;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.unicom.mm7.bean.UMms;
import com.unicom.mm7.util.DateUtils;
import com.unicom.mm7.util.ObjectUtils;

/**
 * 该类可以看做是一个缓冲类，先通知消息给对方，如果通知失败，写文件 该类是一个单实例类 改为用uuid 2012-12-22
 * 
 * @author Administrator
 */
public class Result
{
	public static final String MO_DIR = "MO";// MO彩信保存目录

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
		// 提交失败 result=-1
		int result = -1;
		String resultMessage = String.valueOf(resultCode);
		String reportTime = DateUtils.getTimestamp14();
		// 如果通知失败，那么写日志
		resultLog.info("sendid:" + sendid + "|mobile:" + mobile + "|result:"
				+ result + "|resultMessage:" + resultMessage + "|reportTime:"
				+ reportTime);
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
		// 提交成功 result=0
		int result = 0;
		String resultMessage = mmStatusText;
		// 如果通知失败，那么写日志
		resultLog.info("sendid:" + sendid + "|mobile:" + mobile + "|result:"
				+ result + "|resultMessage:" + resultMessage + "|reportTime:"
				+ reportTime);
	}

	/**
	 * 接收MO彩信
	 * 
	 * @param mms
	 */
	public void notifyResult(UMms mms)
	{
		// 保存文件，然后通知到ws，
		// 如果通知失败，记录通知失败原因
		if (writeObject(mms))
		{
			// 如果通知失败，那么写日志
			resultLog.info("deliver:" + mms.getSendID());// 通知ws
		}
		else
		{
			log.error("MO消息写文件失败" + mms.getSendID());
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

}
