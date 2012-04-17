package com.chinamobile.cmpp2_0.protocol.message;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
public class StatusID
{
	public static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	static
	{
		statusMap.put(0, "系统操作成功");
		statusMap.put(1, "没有匹配路由");
		statusMap.put(2, "源网关代码错误");
		statusMap.put(3, "路由类型错误");
		statusMap.put(4, "本节点不支持更新");
		statusMap.put(5, "路由信息更新失败");
		statusMap.put(6, "汇接网关路由信息时间戳比本地路由信息时间戳旧");
		statusMap.put(9, "系统繁忙");
		statusMap.put(10, "Update_type错误");
		statusMap.put(11, "路由编号错误");
		statusMap.put(12, "目的网关代码错误");
		statusMap.put(13, "目的网关IP错误");
		statusMap.put(14, "目的网关Port错误");
		statusMap.put(15, "MT路由其实号段错误");
		statusMap.put(16, "MT路由截止号段错误");
		statusMap.put(17, "手机所属省代码错误");
		statusMap.put(18, "用户类型错误");
		statusMap.put(19, "SP_Id错误");
		statusMap.put(20, "SP_Code错误");
		statusMap.put(21, "SP_AccessType错误");
		statusMap.put(22, "Service_Id错误");
		statusMap.put(23, "Start_code错误");
		statusMap.put(24, "End_code错误");

	}

	public static String getStatus(int status)
	{

		String statusString = statusMap.get(status);
		if (statusString == null)
		{
			if (status >= 100 && status <= 199)
			{
				statusString = "厂家自定义错误" + status;
			}
			else
			{
				statusString = "错误" + status;
			}
		}

		return statusString;
	}

	public static void main(String[] args)
	{
		System.out.println(StatusID.getStatus(80));
	}

}
