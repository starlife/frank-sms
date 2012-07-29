package com.frank.ylear.modules.mms.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.util.DateUtils;
import com.frank.ylear.common.util.FileUtil;
import com.frank.ylear.common.util.Tools;
import com.frank.ylear.modules.base.dao.impl.BaseDaoImpl;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.entity.UploadFile;
import com.frank.ylear.modules.mms.service.MmsFileService;
import com.frank.ylear.modules.mms.service.impl.MmsFileServiceImpl;

public class MMSFileHelper
{
	private static final Log log = LogFactory.getLog(MMSFileHelper.class);

	/**
	 * �����и��������ֶ���װ��һ��MmsFile����
	 * 
	 * @param mmsFile
	 * @param mmsName
	 * @param absolutePath
	 * @throws IOException
	 * @throws SQLException
	 */
	public static Long saveMmsFile(MmsFileService service, MmsFile mmsFile,
			String mmsName, String absolutePath)
	{
		// boolean flag = true;
		Long id = null;
		int frames = 0;
		List<InputStream> files = new ArrayList<InputStream>();// ������Ҫ�رյĶ���
		try
		{
			// �����ǰ��ļ�����Ϣд��uploadFile������ begin
			UploadFile uploadFile;
			MmsFrame fr;
			mmsFile.getUploadFiles().clear();
			Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
			Iterator<Integer> it = map.keySet().iterator();
			// ѭ������ÿһ֡
			while (it.hasNext())
			{
				Integer key = it.next();
				fr = map.get(key);
				if (StringUtils.isNotEmpty(fr.getImage()))
				{
					uploadFile = new UploadFile();
					File file = new File(absolutePath + File.separator
							+ fr.getImage());
					FileInputStream input = new FileInputStream(file);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// �����ļ�����
					uploadFile.setFilename(file.getName());// �����ļ���
					uploadFile.setFilesize(blob.length());// �����ļ���С
					uploadFile.setFiletype(fr.getImageFileType());// �����ļ�����
					uploadFile.setFrameid(key);// ����֡��
					uploadFile.setUploadtime(DateUtils.getTimestamp14());
					mmsFile.getUploadFiles().add(uploadFile);

				}
				if (StringUtils.isNotEmpty(fr.getAudio()))
				{
					uploadFile = new UploadFile();
					// File file = getAbsoluteFile(fr.getAudioFileName());
					File file = new File(absolutePath + File.separator
							+ fr.getAudio());
					FileInputStream input = new FileInputStream(file);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// �����ļ�����
					uploadFile.setFilename(file.getName());// �����ļ���
					uploadFile.setFilesize(blob.length());// �����ļ���С
					uploadFile.setFiletype(fr.getAudioFileType());// �����ļ�����
					uploadFile.setFrameid(key);// ����֡��
					uploadFile.setUploadtime(DateUtils.getTimestamp14());
					mmsFile.getUploadFiles().add(uploadFile);

				}
				if (StringUtils.isNotEmpty(fr.getText()))
				{
					uploadFile = new UploadFile();
					byte[] data = fr.getText().getBytes("UTF-8");
					ByteArrayInputStream input = new ByteArrayInputStream(data);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// �����ļ�����
					uploadFile.setFilesize(blob.length());// �����ļ���С
					uploadFile.setFilename(fr.getTextFileName());// �����ļ���
					uploadFile.setFrameid(key);// ����֡��
					uploadFile.setUploadtime(DateUtils.getTimestamp14());
					uploadFile.setFiletype(fr.getTextFileType());
					mmsFile.getUploadFiles().add(uploadFile);

				}
			}
			// end �����ǰ��ļ�����Ϣд��uploadFile������

			String smil = MMSFileHelper.createSmil(mmsFile);// ����smil
			long smilSize = smil.getBytes().length;
			frames = mmsFile.getFrameMap().size();
			long mmsSize = smilSize + mmsFile.getMmsSize();// �����ܴ�С
			mmsFile.setMmsName(mmsName);
			mmsFile.setFrames(frames);
			mmsFile.setSmildata(smil);
			mmsFile.setSmilname(Constants.SMIL_NAME);
			mmsFile.setSmilsize(smilSize);
			mmsFile.setMmsSize(mmsSize);
			mmsFile.setCreatetime(DateUtils.getTimestamp14());
			//���ﱣ��
			id = (Long)service.saveMmsFile(mmsFile);
			return id;
			

		}
		catch (Exception ex)
		{
			log.error(null,ex);
			// flag = false;
			return null;
		}
		finally
		{
			// �ر��ļ�ָ��
			for (int i = 0; i < files.size(); i++)
			{
				try
				{
					files.get(i).close();
				}
				catch (IOException e)
				{ // TODO Auto-generated catch block
					log.error(null,e);
				}
			}
		}

	}
	
	

	/**
	 * �����ݿ��в�������������һ������
	 * 
	 * @param mmsFile
	 * @param absolutePath
	 * @return
	 */
	public static boolean makeMMS(MmsFile mmsFile, String absolutePath)
	{
		// ������Ҫ������֡
		for (int i = 0; i < mmsFile.getFrames(); i++)
		{
			MmsFrame fr = new MmsFrame();
			mmsFile.getFrameMap().put(i + 1, fr);
		}

		// ��������upload�ļ�����tempԤ���ļ�������MmsFrame�е��ֶ�
		Iterator<UploadFile> it = mmsFile.getUploadFiles().iterator();
		try
		{
			while (it.hasNext())
			{
				UploadFile upload = it.next();
				int key = upload.getFrameid();
				MmsFrame fr = mmsFile.getFrameMap().get(key);
				String fileType = FileTypeHelper.decodeFileType(upload
						.getFiletype());
				if (Constants.FILE_TYPE_TEXT.equals(fileType))
				{
					Blob blob = upload.getFiledata();
					fr.setText(FileUtil.getData(blob.getBinaryStream(),
							"UTF-8"));
					fr.setTextFileName(upload.getFilename());
					fr.setTextFileSize(upload.getFilesize());
					fr.setTextFileType(upload.getFiletype());
				}
				else if (Constants.FILE_TYPE_IMAGE.equals(fileType))
				{
					Blob blob = upload.getFiledata();
					String filename = upload.getFilename();
					File file = new File(absolutePath + File.separator
							+ Constants.UPLOAD_FILE_DIR + File.separator
							+ filename);
					FileUtil.saveData(blob.getBinaryStream(), file);
					fr.setImage(Constants.UPLOAD_FILE_DIR + File.separator
							+ filename);
					fr.setImageFileName(filename);
					fr.setImageFileSize(upload.getFilesize());
					fr.setImageFileType(upload.getFiletype());
				}
				else if (Constants.FILE_TYPE_AUDIO.equals(fileType))
				{
					Blob blob = upload.getFiledata();
					String filename = upload.getFilename();
					File file = new File(absolutePath + File.separator
							+ Constants.UPLOAD_FILE_DIR + File.separator
							+ filename);
					// String relativefile = this.getRelativeFile(file);
					FileUtil.saveData(blob.getBinaryStream(), file);
					fr.setAudio(Constants.UPLOAD_FILE_DIR + File.separator
							+ filename);
					fr.setAudioFileName(filename);
					fr.setAudioFileSize(upload.getFilesize());
					fr.setAudioFileType(upload.getFiletype());
				}
			}
		}
		catch (SQLException ex)
		{
			log.error(null, ex);
			return false;

		}
		finally
		{

		}

		// ͨ��smil�����ݸ����õ�����֡��duringTime
		SmilParser parse = new SmilParser(mmsFile.getSmildata());
		parse.parse();
		for (int i = 0; i < parse.getFramecount(); i++)
		{
			SmilParser.Frame f = parse.getFrames().get(i);
			MmsFrame fr = mmsFile.getFrameMap().get(f.framenumber);
			fr.setDuringTime(f.dur);
		}
		// ���¼����֡��С
		Iterator<Integer> keys = mmsFile.getFrameMap().keySet().iterator();
		while (keys.hasNext())
		{
			MmsFrame fr = mmsFile.getFrameMap().get(keys.next());
			MMSFileHelper.reSizeFrame(fr);
		}
		return true;
	}

	/**
	 * ����һ֡��֡��Ϊframeid
	 */
	public static void addFrame(MmsFile mmsFile)
	{
		// 1.�õ������֡�ţ������ǰû��֡����ô֡��Ϊ1������֡��Ϊ��ǰ֡��1
		Integer frameid;
		if (mmsFile.getCurrentFrameId() == null)
		{
			frameid = 1;
			// ������Ŵ�СΪ�գ���ô��Ϊ0
			/*
			 * if (mmsFile.getMmsSize() == null) { mmsFile.setMmsSize(0L); }
			 */
		}
		else
		{
			frameid = mmsFile.getCurrentFrameId() + 1;
		}

		// 2.����֡����Ҫ������
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
		// ������Ҫ�ƶ��Ľڵ�
		Set<Integer> keySet = map.keySet();
		int keyLen = keySet.size();
		Integer[] keys = new Integer[keyLen];
		keySet.toArray(keys);
		// ����һ��temp
		Map<Integer, MmsFrame> temp = new LinkedHashMap<Integer, MmsFrame>();

		for (int i = 0; i < keys.length; i++)
		{
			if (frameid <= keys[i])
			{
				// ����Ҫ�ƶ���֡ת�Ƶ���һ��map������ͳһ�ƻ�ȥ
				MmsFrame fr = map.remove(keys[i]);
				temp.put(keys[i] + 1, fr);
			}
		}

		// Ϊ�˴ﵽ����Ч�����Ȳ���
		MmsFrame frame = new MmsFrame();
		map.put(frameid, frame);
		// ת�ƻ�ȥ
		if (temp.size() > 0)
		{
			map.putAll(temp);
			temp.clear();
		}

		// 3. ��ǰ֡���ı���
		MMSFileHelper.changeCurrentFrame(mmsFile, frameid);
	}

	public static void delFrame(MmsFile mmsFile, Integer frameid)
	{
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();

		// ��ɾ�������ƶ�
		map.remove(frameid);

		Set<Integer> keySet = map.keySet();
		int keyLen = keySet.size();
		Integer[] keys = new Integer[keyLen];
		keySet.toArray(keys);
		// ����һ��temp
		Map<Integer, MmsFrame> temp = new LinkedHashMap<Integer, MmsFrame>();

		for (int i = 0; i < keys.length; i++)
		{
			if (frameid < keys[i])
			{
				// ����Ҫ�ƶ���֡ת�Ƶ���һ��map������ͳһ�ƻ�ȥ
				MmsFrame fr = map.remove(keys[i]);
				temp.put(keys[i] - 1, fr);
			}
		}
		// ת�ƻ�ȥ
		if (temp.size() > 0)
		{
			map.putAll(temp);
			temp.clear();
		}

		// ���õ�ǰ֡
		Integer currentFrameNumber = frameid;
		MmsFrame mf = mmsFile.getFrameMap().get(frameid);
		if (mf == null)
		{
			currentFrameNumber = frameid - 1;
			mf = mmsFile.getFrameMap().get(currentFrameNumber);
		}
		if (mf == null)
		{
			// ��ǰ����Ϊ��
			currentFrameNumber = null;
			MMSFileHelper.clearCurrentFrame(mmsFile);
		}
		else
		{
			// ���õ�ǰ֡
			MMSFileHelper.changeCurrentFrame(mmsFile, currentFrameNumber);
		}

	}

	public static void uploadImage(MmsFile mmsFile, File image,
			String fileName, String fileType)
	{
		MmsFrame fr = mmsFile.getFrameMap().get(mmsFile.getCurrentFrameId());
		// ����image
		long fileSize = image.length();

		fr.setImage(Constants.UPLOAD_FILE_DIR + File.separator + fileName);
		fr.setImageFileName(fileName);
		fr.setImageFileSize(fileSize);
		fr.setImageFileType(fileType);
		// ���¼���֡��С�Ͳ��Ŵ�С
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void uploadAudio(MmsFile mmsFile, File audio,
			String fileName, String fileType)
	{
		MmsFrame fr = mmsFile.getFrameMap().get(mmsFile.getCurrentFrameId());
		// ����image
		long fileSize = audio.length();

		fr.setAudio(Constants.UPLOAD_FILE_DIR + File.separator + fileName);
		fr.setAudioFileName(fileName);
		fr.setAudioFileSize(fileSize);
		fr.setAudioFileType(fileType);
		// ���¼���֡��С�Ͳ��Ŵ�С
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void uploadText(MmsFile mmsFile, String text)
	{
		MmsFrame fr = mmsFile.getFrameMap().get(mmsFile.getCurrentFrameId());
		// ����image
		long fileSize = text.getBytes().length;
		String fileName = mmsFile.getCurrentFrameId() + ".txt";

		fr.setText(text);
		fr.setTextFileName(fileName);
		fr.setTextFileSize(fileSize);
		// fr.setTextFileType(fileType);
		// ���¼���֡��С�Ͳ��Ŵ�С
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void clearImage(MmsFile mmsFile)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ���image
		fr.setImage(null);
		fr.setImageFileName(null);
		fr.setImageFileSize(0);
		fr.setImageFileType(null);
		// ���¼���֡��С�Ͳ��Ŵ�С
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void clearAudio(MmsFile mmsFile)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ���audio
		fr.setAudio(null);
		fr.setAudioFileName(null);
		fr.setAudioFileSize(0);
		fr.setAudioFileType(null);
		// ���¼���֡��С�Ͳ��Ŵ�С
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void clearText(MmsFile mmsFile)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ���text
		fr.setText(null);
		fr.setTextFileName(null);
		fr.setTextFileSize(0);
		// ���¼���֡��С�Ͳ��Ŵ�С
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void changeDuringTime(MmsFile mmsFile, int duringTime)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);

		fr.setDuringTime(duringTime);
		mmsFile.setCurrentDuringTime(duringTime);
	}

	public static void changeCurrentFrame(MmsFile mmsFile, int frameid)
	{
		MmsFrame frame = mmsFile.getFrameMap().get(frameid);
		mmsFile.setCurrentFrameId(frameid);
		mmsFile.setCurrentFrameSize(frame.getFrameSize());
		mmsFile.setCurrentDuringTime(frame.getDuringTime());
		mmsFile.setCurrentFrameText(frame.getText());
	}

	public static void clearCurrentFrame(MmsFile mmsFile)
	{
		mmsFile.setCurrentFrameId(null);
		mmsFile.setCurrentFrameSize(null);
		mmsFile.setCurrentDuringTime(null);
		mmsFile.setCurrentFrameText(null);
	}

	public static void reSizeFrame(MmsFrame fr)
	{
		long frameSize = fr.getImageFileSize() + fr.getAudioFileSize()
				+ fr.getTextFileSize();
		fr.setFrameSize(frameSize);
	}

	/**
	 * @param mms
	 */
	public static void reSizeMms(MmsFile mms)
	{
		long size = 0;
		Map<Integer, MmsFrame> map = mms.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			size += map.get(it.next()).getFrameSize();
		}
		mms.setMmsSize(size);
	}

	public static void reSizeFrameAndMms(MmsFrame fr, MmsFile mms)
	{
		reSizeFrame(fr);
		mms.setCurrentFrameSize(fr.getFrameSize());
		reSizeMms(mms);
	}

	public static String createSmil(MmsFile mmsFile)
	{
		Smil smil = new Smil();

		smil.smilAddHead();// ������ļ�ͷ����Ϣ��
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			MmsFrame fr = map.get(it.next());

			smil.setSmilParStart(fr.getDuringTime());// ���ſ�ʼ���
			if (Tools.isNotEmpty(fr.getImage()))
			{
				smil.smilAddImg(fr.getImageFileName());
			}
			if (Tools.isNotEmpty(fr.getText()))
			{
				smil.smilAddText(fr.getTextFileName());
			}
			if (Tools.isNotEmpty(fr.getAudio()))
			{
				smil.smilAddAudio(fr.getAudioFileName());
			}
			smil.setSmilParEnd();

		}
		smil.smilAddFoot();

		return smil.getSmil();
	}
	
	public static boolean cleanAllUploadFile(String dir)
	{
		boolean b=false;
		File path = new File(dir);
		if (!path.exists())
		{
			path.mkdirs();
		}
		else
		{
			if (path.isDirectory())
			{
				File[] files=path.listFiles();
				for(int i=0;i<files.length;i++)
				{
					b=files[i].delete();
				}
			}
		}
		return b;

	}

	public static void main(String[] args) throws IOException, SQLException
	{
		String path = "E:\\google_svn\\frank-sms\\trunk\\ylear\\src\\"
				+ "applicationContext.xml";
		ApplicationContext ctx = new FileSystemXmlApplicationContext(path);
		BaseDaoImpl dao = (BaseDaoImpl) ctx.getBean("baseDao");

		// List l1=dao.getHibernateTemplate().find("select obj from UMms obj
		// inner join fetch obj.mmsFile where 1=1");
		MmsFileServiceImpl service = (MmsFileServiceImpl) ctx
				.getBean("mmsFileService");
		MmsFile mmsFile = service.getMmsFile(2L);
		System.out.println(mmsFile);
		String absolutePath = "E:\\google_svn\\frank-sms\\trunk\\ylear";
		boolean b = makeMMS(mmsFile, absolutePath);
		Iterator<Integer> it = mmsFile.getFrameMap().keySet().iterator();
		while (it.hasNext())
		{

			System.out.println("FRAME:" + mmsFile.getFrameMap().get(it.next()));
		}
		System.out.println(mmsFile);
		System.out.println("result:=====" + b);
		System.out.println("++++++++++++++++++++++++++++++++++++++");
		MmsFrame fr = mmsFile.getFrameMap().get(1);
		File file = new File(absolutePath + File.separator + fr.getImage());
		FileInputStream input = new FileInputStream(file);
		// files.add(input);
		Blob blob = Hibernate.createBlob(input);
		UploadFile uploadFile = new UploadFile();
		uploadFile.setFiledata(blob);// �����ļ�����
		uploadFile.setFilename(file.getName());// �����ļ���
		uploadFile.setFilesize(blob.length());// �����ļ���С
		uploadFile.setFiletype(fr.getImageFileType());// �����ļ�����
		uploadFile.setFrameid(1);// ����֡��
		uploadFile.setUploadtime(DateUtils.getTimestamp14());
		dao.getHibernateTemplate().save(uploadFile);

	}
}
