package com.cmcc.mm7.vasp.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cmcc.mm7.vasp.service.Hex;

public class Thunk
{
	private static final Log log = LogFactory.getLog(Http.class);
	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public boolean recvData(InputStream input)
	{
		// �ȶ�httpͷ��Ϣ
		if (input == null)
		{
			log.warn("input is null");
			return false;
		}
		// ��ʾһ��thunked����
		ByteArrayOutputStream thunked = new ByteArrayOutputStream();
		try
		{
			while (true)
			{
				thunked.reset();// ���³�ʼ��
				while (true)
				{
					int i = input.read();
					if (i == -1)
					{
						log.warn("��ȡ��ʱδ��������-1��ʧ��");
						return false;
					}
					thunked.write(i);
					// �ж��Ƿ�������thunkͷ�Ľ���������ǣ���ô��thunk������
					if (isThunkedHeaderComplete(thunked.toString()))
					{
						break;
					}
				}
				int thunkedLen = getThunkedLength(thunked.toString());
				if (thunkedLen == 0)
				{
					// thunked������Ϊ0���п����ǽ�β��
					byte[] temp = new byte[2];
					input.read(temp);// ��ȡ��\r\n��
					thunked.write(temp);
					if (isLastChunked(thunked.toString()))
					{
						return true;
					}
					else
					{
						return false;
					}

				}
				// ��ȡthunked������
				this.readThunkedData(input, thunkedLen);
				// �����ȡ��\r\n��,����
				byte[] temp = new byte[2];
				input.read(temp);
			}

		}
		catch (Exception ex)
		{
			log.error("��ȡ��ͷʱ����", ex);
			return false;
		}

	}

	private boolean readThunkedData(InputStream input, int length)
	{
		try
		{
			byte tbytebuf[] = new byte[length];
			for (int temp = input.read(tbytebuf); temp != length; temp = input
					.read(tbytebuf, temp, length - temp)
					+ temp)
				;
			baos.write(tbytebuf);
		}
		catch (Exception ex)
		{
			log.error(null, ex);
			return false;
		}
		return true;
	}

	public byte[] getData()
	{
		return baos.toByteArray();
	}

	/**
	 * �Ƿ����thunkedͷ
	 * 
	 * @param thunkedHeader
	 * @return
	 */
	public static boolean isThunkedHeaderComplete(String thunkedHeader)
	{
		return thunkedHeader.indexOf("\r\n") != -1;
	}

	/**
	 * �Ƿ������һ��thunked
	 * 
	 * @param thunked
	 * @return
	 */
	public static boolean isLastChunked(String thunked)
	{
		return thunked.indexOf("\r\n\r\n") != -1;
	}

	/**
	 * �õ�thunked�������ݳ���
	 * 
	 * @param thunkedHeader
	 * @return
	 */
	public static int getThunkedLength(String thunkedHeader)
	{
		int length = 0;
		int index = thunkedHeader.indexOf("\r\n");
		if (index <= 0)
		{
			return 0;
		}
		String hex = thunkedHeader.substring(0, index).trim();
		if (hex.length() > 8)
		{
			//
			return 0;
		}
		byte[] bytes = Hex.rstr(hex);
		for (int i = 0; i < bytes.length; i++)
		{
			length += (bytes[i] + 256 * (i - 0));
		}
		return length;
	}

}
