package com.chinamobile.cmpp2_0.protocol.util;

public class Constants
{
	/**
	 * 定义通道超时时间 单位（ms）
	 */
	public static final int TIMEOUT = 60 * 1000;
	/**
	 * 定义应答包超时时间按 单位（ms）， 超过了重发
	 */
	public static final long RESP_TIME = 60 * 1000;// 
	/**
	 * 定义短信重发次数
	 */
	public static final int RESEND_TIME = 3;// 重发次数

	/**
	 * 发送心跳包频率（ms）
	 */
	public static final long HEARTBEAT = 5000;
	/**
	 * 定义NeedRespQue队列的长度
	 */
	public static final int NEEDRESPQUE_SIZE = 10000;

	/**
	 * 每秒最多提交条数
	 */
	public static int MAX_SPEED = 20;

}
