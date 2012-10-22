package com.frank.ylear.modules.util;
/**
 * 全局消息控制bean
 * @author Memory
 * @createDate 2011-11-07
 */
public class GlobalMessage {
	//正常码
	public final static Integer SUCC_CODE = 1;
	//失败码
	public final static Integer FAIL_CODE = 0;
	//异常码
	public final static Integer WARN_CODE = -1;
	
	
	//接口交互结果失败
	public final static String REST_FAIL_CODE = "0000";
	//接口交互结果成功
	public final static String REST_SUCC_CODE = "1111";
	//接口交互已冻结
	public final static String REST_FREEZE_CODE = "2222";
	//接口交互未绑定
	public final static String REST_BINDING_CODE = "3333";
	
	
	//密码重置
	public final static String RESET_PASSWORD = "111111";
	
	/**
	 * session中登录用户键
	 */
	public final static String LOGINED_USER = "loginedUser";
	
	public final static String RESETPASSWORD = "111111";
	
	/**
	 * 用户系统登录次数，初始化
	 */
	public final static Integer INIT_LOGINCOUNT = 0;
	
	/**
	 * 每页显示用户数
	 */
	public final static int DISPLAY_USER_PER_PAGE = 15;
	
	/**
	 * 排行或统计每页默认显示数
	 */
	public final static int RANK_OR_STATIS_PER_PAGE = 20;

	/**
	 * 设定每页显示数
	 */
	public final static int DISPLAY_PER_PAGE = 10;
	

	public final static String USER_ENTITY_NM = "TUser";

	public final static String USER_LOGIN_INFO_ENTITY_NM = "TUserLoginInfo";


	/**
	 * memo长度
	 */
	public final static int MEMO_LENGTH = 20;
	
	public final static int PWD_AVALIBLE_MONTHS = 1;
	
	// 参数(会话有效时间，单位：秒)ID
	public final static Integer PARAM_SESSION_TIMEOUT_ID = 3;
}
