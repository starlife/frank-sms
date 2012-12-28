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
 * ������Կ�����һ�������࣬��֪ͨ��Ϣ���Է������֪ͨʧ�ܣ�д�ļ� ������һ����ʵ���� ��Ϊ��uuid 2012-12-22
 * 
 * @author Administrator
 */
public class Result
{
	public static final String MO_DIR = "MO";// MO���ű���Ŀ¼

	private static String TIMESTAMP = ""; // ����ʱ����õ�

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
	 * �����ύʧ�ܵ���Ϣ
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
	 * ���շ��ͽ��
	 * 
	 * @param sendid
	 * @param mobile
	 * @param resultCode
	 */
	public void notifyResult(String sendid, String mobile, int resultCode)
	{
		// �ύʧ�� result=-1
		int result = -1;
		String resultMessage = String.valueOf(resultCode);
		String reportTime = DateUtils.getTimestamp14();
		// ���֪ͨʧ�ܣ���ôд��־
		resultLog.info("sendid:" + sendid + "|mobile:" + mobile + "|result:"
				+ result + "|resultMessage:" + resultMessage + "|reportTime:"
				+ reportTime);
	}

	/**
	 * ����״̬����
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
		// �ύ�ɹ� result=0
		int result = 0;
		String resultMessage = mmStatusText;
		// ���֪ͨʧ�ܣ���ôд��־
		resultLog.info("sendid:" + sendid + "|mobile:" + mobile + "|result:"
				+ result + "|resultMessage:" + resultMessage + "|reportTime:"
				+ reportTime);
	}

	/**
	 * ����MO����
	 * 
	 * @param mms
	 */
	public void notifyResult(UMms mms)
	{
		// �����ļ���Ȼ��֪ͨ��ws��
		// ���֪ͨʧ�ܣ���¼֪ͨʧ��ԭ��
		if (writeObject(mms))
		{
			// ���֪ͨʧ�ܣ���ôд��־
			resultLog.info("deliver:" + mms.getSendID());// ֪ͨws
		}
		else
		{
			log.error("MO��Ϣд�ļ�ʧ��" + mms.getSendID());
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
		// ����Ǽ��ɾ��ʱ�䣬��ôִ��ɾ������
		String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
		if (!TIMESTAMP.equals(timestamp))
		{
			if (dir.isFile() && dir.exists())
			{
				// �����ļ�����5��
				long now = System.currentTimeMillis();
				long during = 5 * 24 * 3600 * 1000;// 5��

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
