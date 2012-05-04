package com.chinamobile.cmpp2_0.protocol.util;

public class Constants
{
	/**
	 * 定义NeedRespQue队列的长度
	 */
	public static final int NEEDRESPQUE_SIZE = 10000;

	/**
	 * 定义包等待确认时间，单位（ms）， 超过了重发
	 */
	public static final long RESP_TIME = 60 * 1000;// 
	/**
	 * 短信重发次数
	 */
	public static final int RESEND_TIME = 3;// 重发次数

	/**
	 * 发送心跳包频率（ms）
	 */
	public static final long HEARTBEAT = 5000;

	/**
	 * 每秒最多提交条数
	 */
	public static int MAX_SPEED = 20;

}
