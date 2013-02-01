package com.frank.sp.sgip.frame;

import com.frank.sp.sgip.bean.USms;
import com.frank.sp.sgip.util.DateUtils;



/**
 * 该类可以看做是一个缓冲类，先通知消息给对方，如果通知失败，写文件 该类是一个单实例类 改为用uuid 2012-12-22
 * 
 * @author Administrator
 */
public class Result
{

	//private static final Log log = LogFactory.getLog(Result.class);

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
	public void notifySubmitResp(String sendid,String[] to, int resultCode)
	{
		for (String number:to)
		{
			notifySubmitResp(sendid,number, resultCode);
		}
	}

	/**
	 * 接收发送结果
	 * 
	 * @param sendid
	 * @param mobile
	 * @param resultCode
	 */
	private void notifySubmitResp(String sendid, String mobile, int resultCode)
	{
		// 提交失败 result=-1
		int result = -1;
		String resultMessage = String.valueOf(resultCode);
		String reportTime = DateUtils.getTimestamp14();
		NotifyThread.notifyReport(sendid, mobile, result, resultMessage,
				reportTime);

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
	public void notifyReport(String sendid, String mobile, int errorCode)
	{
		// 提交成功 result=0
		int result = 0;
		String resultMessage =String.valueOf(errorCode);
		String reportTime = DateUtils.getTimestamp14();
		// 如果通知失败，那么写日志
		NotifyThread.notifyReport(sendid, mobile, result, resultMessage,
				reportTime);

	}

	/**
	 * 接收MO短信
	 * 
	 * @param mms
	 */
	public void notifyDeliver(USms sms)
	{
		
		NotifyThread.notifyDeliver(sms.getSendid(),sms.getMsgContent(),sms.getRecipient());
		
	}

	

}
