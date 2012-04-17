package com.chinamobile.cmpp2_0.protocol.message;

/**
 * 
 * @author Administrator
 */
public class CommandID {
	public static final int CMPP_CONNECT = 0x00000001;// 请求连接
	public static final int CMPP_CONNECT_RESP = 0x80000001;// 请求连接应答
	public static final int CMPP_TERMINATE = 0x00000002;// 终止连接
	public static final int CMPP_TERMINATE_RESP = 0x80000002;// 终止连接应答
	public static final int CMPP_SUBMIT = 0x00000004;// 提交短信
	public static final int CMPP_SUBMIT_RESP = 0x80000004;// 提交短信应答
	public static final int CMPP_DELIVER = 0x00000005;// 短信下发
	public static final int CMPP_DELIVER_RESP = 0x80000005;// 下发短信应答
	public static final int CMPP_ACTIVE_TEST = 0x00000008;// 激活测试
	public static final int CMPP_ACTIVE_TEST_RESP = 0x80000008;// 激活测试应答

	public static String getCommandString(int commandId) {
		switch (commandId) {
		case CMPP_CONNECT:
			return "CMPP_CONNECT";
		case CMPP_CONNECT_RESP:
			return "CMPP_CONNECT_RESP";
		case CMPP_TERMINATE:
			return "CMPP_TERMINATE";
		case CMPP_TERMINATE_RESP:
			return "CMPP_TERMINATE_RESP";
		case CMPP_SUBMIT:
			return "CMPP_SUBMIT";
		case CMPP_SUBMIT_RESP:
			return "CMPP_SUBMIT_RESP";
		case CMPP_DELIVER:
			return "CMPP_DELIVER";
		case CMPP_DELIVER_RESP:
			return "CMPP_DELIVER_RESP";
		case CMPP_ACTIVE_TEST:
			return "CMPP_ACTIVE_TEST";
		case CMPP_ACTIVE_TEST_RESP:
			return "CMPP_ACTIVE_TEST_RESP";
		default:
			return "" + commandId;

		}
	}

}
