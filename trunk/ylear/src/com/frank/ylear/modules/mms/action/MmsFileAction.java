package com.frank.ylear.modules.mms.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Hibernate;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.util.FileUtil;
import com.frank.ylear.common.util.MakeSmil;
import com.frank.ylear.common.util.ParseSmil;
import com.frank.ylear.common.util.Tools;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.entity.MmsFrame;
import com.frank.ylear.modules.mms.entity.UploadFile;
import com.frank.ylear.modules.mms.service.MmsFileService;

public class MmsFileAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SHOWUI = "showui";

	private MmsFileService mmsFileService;

	private File image;
	private String imageFileName;
	private String imageContentType;

	private File audio;
	private String audioFileName;
	private String audioContentType;

	private String frameText;

	/* ��ǰ֡ */
	// private Integer currentFrameId;
	private Integer frameId;

	/* ��ǰ֡����ʱ�� */
	private Integer duringTime;

	/* �������� */
	private String mmsName;

	/* �ṩ���鿴����ҳ���ѯ�� */
	private String id;

	private String callbackMsg;// �ص���Ϣ

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getCallbackMsg()
	{
		return callbackMsg;
	}

	public void setCallbackMsg(String callbackMsg)
	{
		this.callbackMsg = callbackMsg;
	}

	public String getMmsName()
	{
		return mmsName;
	}

	public void setMmsName(String mmsName)
	{
		this.mmsName = mmsName;
	}

	public Integer getDuringTime()
	{
		return duringTime;
	}

	public void setDuringTime(Integer durTime)
	{
		this.duringTime = durTime;
	}

	public MmsFileService getMmsFileService()
	{
		return mmsFileService;
	}

	public void setMmsFileService(MmsFileService mmsFileService)
	{
		this.mmsFileService = mmsFileService;
	}

	public File getImage()
	{
		return image;
	}

	public void setImage(File image)
	{
		this.image = image;
	}

	public String getImageFileName()
	{
		return imageFileName;
	}

	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}

	public String getImageContentType()
	{
		return imageContentType;
	}

	public void setImageContentType(String imageContentType)
	{
		this.imageContentType = imageContentType;
	}

	public File getAudio()
	{
		return audio;
	}

	public void setAudio(File audio)
	{
		this.audio = audio;
	}

	public String getAudioFileName()
	{
		return audioFileName;
	}

	public void setAudioFileName(String audioFileName)
	{
		this.audioFileName = audioFileName;
	}

	public String getAudioContentType()
	{
		return audioContentType;
	}

	public void setAudioContentType(String audioContentType)
	{
		this.audioContentType = audioContentType;
	}

	public String getFrameText()
	{
		return frameText;
	}

	public void setFrameText(String frameText)
	{
		this.frameText = frameText;
	}

	public Integer getFrameId()
	{
		return frameId;
	}

	public void setFrameId(Integer frameId)
	{
		this.frameId = frameId;
	}

	/** ***================================================================** */
	/* ���ű༭����ҳ */
	public String execute() throws Exception
	{
		return SUCCESS;
	}

	/**
	 * ����ui����
	 */
	public String show() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		if (mmsFile.getCurrentFrameId() != null)
		{
			this.setCallbackMsg("chooseFrame_callback()");
		}
		return SHOWUI;
	}

	public void validateSave()
	{
		checkMmsNameNotEmpty();
	}

	public void validateClose()
	{

	}

	public void validateAddFrame()
	{

	}

	public void validateChooseFrame()
	{
		// ���֡id�Ƿ���ȷ
		this.checkChangeFrameId();
	}

	public void validateDelFrame()
	{
	}

	public void validateUploadImage()
	{
		// ��֤�ļ��Ƿ�Ϊ�գ��ļ������Ƿ���ȷ���ļ���С�Ƿ񳬳����ܴ�С�Ƿ񳬳�
		File file = this.getImage();
		String fileType = this.getImageContentType();
		if (!checkUploadFileNotEmpty(file))
		{
			return;
		}
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
		// ����ļ������Ƿ����
		if (!checkUploadFileType("image", fileType))
		{
			return;
		}
		// ����ļ���С
		if (!checkUploadFileSize(file.length()))
		{
			return;
		}
		// �������ܴ�С
		if (!checkMmsSizeBeyond("image", file.length()))
		{
			return;
		}

	}

	public void validateClearImage()
	{
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateUploadAudio()
	{
		// ��֤�ļ��Ƿ�Ϊ�գ��ļ������Ƿ���ȷ���ļ���С�Ƿ񳬳����ܴ�С�Ƿ񳬳�
		File file = this.getAudio();
		String fileType = this.getAudioContentType();
		if (!checkUploadFileNotEmpty(file))
		{
			return;
		}
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
		// ����ļ������Ƿ����
		if (!checkUploadFileType("audio", fileType))
		{
			return;
		}
		// ����ļ���С
		if (!checkUploadFileSize(file.length()))
		{
			return;
		}
		// �������ܴ�С
		if (!checkMmsSizeBeyond("audio", file.length()))
		{
			return;
		}

	}

	public void validateClearAudio()
	{
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateUploadText()
	{
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}

		// �������ܴ�С
		String text = this.getFrameText();
		if (text != null)
		{
			long textSize = text.getBytes().length;
			if (!checkMmsSizeBeyond("text", textSize))
			{
				return;
			}
		}

	}

	public void validateClearText()
	{
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateChangeDuringTime()
	{
		// ��鵱ǰ�Ƿ���֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
		// ��鵱ǰ֡���ʱ���Ƿ���ȷ
		if (!checkDuringTime())
		{
			return;
		}
	}

	/**
	 * ����֡
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addFrame() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid;
		if (mmsFile.getCurrentFrameId() == null)
		{
			// ��ǰ����Ϊ��
			frameid = 1;
			// ������Ŵ�СΪ�գ���ô��Ϊ0
			if (mmsFile.getMmsSize() == null)
			{
				mmsFile.setMmsSize(0);
			}
		}
		else
		{
			frameid = mmsFile.getCurrentFrameId() + 1;
		}
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
		// ��ǰ֡���ı���
		this.changeCurrentFrame(mmsFile, frameid);

		// �ص�����
		this.setCallbackMsg("chooseFrame_callback()");
		return SHOWUI;
	}

	/**
	 * ѡ��֡
	 * 
	 * @return
	 * @throws Exception
	 */
	public String chooseFrame() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		int frameid = this.getFrameId();
		this.changeCurrentFrame(mmsFile, frameid);
		// �ص�����
		this.setCallbackMsg("chooseFrame_callback()");
		return SHOWUI;
	}

	/**
	 * ɾ��֡
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delFrame() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = this.getFrameId();
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
		MmsFrame mf = map.get(frameid);
		if (mf == null)
		{
			currentFrameNumber = frameid - 1;
			mf = map.get(currentFrameNumber);
		}
		if (mf == null)
		{
			// ��ǰ����Ϊ��
			currentFrameNumber = null;
			this.clearCurrentFrame(mmsFile);
		}
		else
		{
			// ���õ�ǰ֡
			this.changeCurrentFrame(mmsFile, currentFrameNumber);
		}
		// �ص�����
		this.setCallbackMsg("chooseFrame_callback()");
		return SHOWUI;

	}

	/**
	 * �ϴ�ͼƬ�ļ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadImage() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ����image
		long fileSize = this.getImage().length();
		// �������һ���ļ�
		File newFile = this.generateFile(this.getImageFileName(), frameid);
		// copy File
		Tools.copyFile(this.getImage(), newFile);
		fr.setImage(getRelativeFile(newFile));
		fr.setImageFileName(newFile.getName());
		fr.setImageFileSize(fileSize);
		fr.setImageFileType(this.getImageContentType());

		// ���¼���֡��С�Ͳ��Ŵ�С
		reSizeFrameAndMms(fr, mmsFile);
		// ���ûص�����
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 * �ϴ������ļ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadAudio() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ����audio
		long fileSize = this.getAudio().length();
		// �������һ���ļ���
		File newFile = this.generateFile(this.getAudioFileName(), frameid);
		// copy File
		Tools.copyFile(this.getAudio(), newFile);
		fr.setAudio(getRelativeFile(newFile));
		fr.setAudioFileName(newFile.getName());
		fr.setAudioFileSize(fileSize);
		fr.setAudioFileType(this.getAudioContentType());

		// ���¼���֡��С�Ͳ��Ŵ�С
		reSizeFrameAndMms(fr, mmsFile);
		// ���ûص�����
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 * �ϴ��ı�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadText() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ����text
		String text = this.getFrameText();
		if (text == null)
		{
			text = "";
		}
		long fileSize = text.length();
		// �������һ���ļ���
		File newFile = this.generateFile("temp.txt", frameid);
		fr.setText(text);
		fr.setTextFileName(newFile.getName());
		fr.setTextFileSize(fileSize);

		// ���¼���֡��С�Ͳ��Ŵ�С
		reSizeFrameAndMms(fr, mmsFile);
		// ���ûص�����
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 * ���ͼƬ�ļ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearImage() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ���image
		fr.setImage(null);
		fr.setImageFileName(null);
		fr.setImageFileSize(0);
		fr.setImageFileType(null);
		// ���¼���֡��С�Ͳ��Ŵ�С
		reSizeFrameAndMms(fr, mmsFile);
		// ���ûص�����
		this.setCallbackMsg("clearImage_callback()");
		return SHOWUI;

	}

	/**
	 * ��������ļ�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearAudio() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ���audio
		fr.setAudio(null);
		fr.setAudioFileName(null);
		fr.setAudioFileSize(0);
		fr.setAudioFileType(null);
		// ���¼���֡��С�Ͳ��Ŵ�С
		reSizeFrameAndMms(fr, mmsFile);
		// ���ûص�����
		this.setCallbackMsg("clearAudio_callback()");
		return SHOWUI;

	}

	/**
	 * ����ı�
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearText() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// ���text
		fr.setText(null);
		fr.setTextFileName(null);
		fr.setTextFileSize(0);
		// ���¼���֡��С�Ͳ��Ŵ�С
		reSizeFrameAndMms(fr, mmsFile);
		// ���ûص�����
		this.setCallbackMsg("clearText_callback()");
		return SHOWUI;

	}

	/**
	 * ���֡�Ĳ���ʱ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public String changeDuringTime() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);

		fr.setDuringTime(this.getDuringTime());
		mmsFile.setCurrentDuringTime(this.getDuringTime());
		return SHOWUI;

	}

	/**
	 * �������
	 * 
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		boolean flag = true;
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
			while (it.hasNext())
			{
				Integer key = it.next();
				fr = map.get(key);
				if (Tools.isNotEmpty(fr.getImage()))
				{
					uploadFile = new UploadFile();
					File file = getAbsoluteFile(fr.getImageFileName());
					FileInputStream input = new FileInputStream(file);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// �����ļ�����
					uploadFile.setFilename(file.getName());// �����ļ���
					uploadFile.setFilesize(blob.length());// �����ļ���С
					uploadFile.setFiletype(fr.getImageFileType());// �����ļ�����
					uploadFile.setFramenumber(key);// ����֡��
					uploadFile.setUploadtime(new Date());
					mmsFile.getUploadFiles().add(uploadFile);

				}
				if (Tools.isNotEmpty(fr.getAudio()))
				{
					uploadFile = new UploadFile();
					File file = getAbsoluteFile(fr.getAudioFileName());
					FileInputStream input = new FileInputStream(file);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// �����ļ�����
					uploadFile.setFilename(file.getName());// �����ļ���
					uploadFile.setFilesize(blob.length());// �����ļ���С
					uploadFile.setFiletype(this.getAudioContentType());// �����ļ�����
					uploadFile.setFramenumber(key);// ����֡��
					uploadFile.setUploadtime(new Date());
					mmsFile.getUploadFiles().add(uploadFile);

				}
				if (Tools.isNotEmpty(fr.getText()))
				{
					uploadFile = new UploadFile();
					byte[] data = fr.getText().getBytes();
					ByteArrayInputStream input = new ByteArrayInputStream(data);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// �����ļ�����
					uploadFile.setFilesize(blob.length());// �����ļ���С
					uploadFile.setFilename(fr.getTextFileName());// �����ļ���
					uploadFile.setFramenumber(key);// ����֡��
					uploadFile.setUploadtime(new Date());
					uploadFile.setFiletype(Constants.FILE_TYPE_TEXT);
					mmsFile.getUploadFiles().add(uploadFile);

				}
			}
			// end �����ǰ��ļ�����Ϣд��uploadFile������

			String smil = this.createSmil(mmsFile);// ����smil
			int smilSize = smil.getBytes().length;
			// int mmsSize = smilSize + mmsFile.getMmsSize();// �����ܴ�С
			mmsFile.setMmsName(mmsName);
			mmsFile.setContentSize(smilSize);
			mmsFile.setContentSmil(smil);
			frames = mmsFile.getFrameMap().size();
			mmsFile.setFrames(frames);
			mmsFile.setSmilname("1.smil");
			mmsFile.setCreatetime(new Date());
			id = (Long) mmsFileService.add(mmsFile);
			// ����ɾ��session
			delMmsFileFromSession();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			flag = false;
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
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		String result = null;
		if (flag)
		{
			// ��������ļ�
			cleanAllUploadFile();
			result = "true," + String.valueOf(id) + ","
					+ String.valueOf(frames);

		}
		else
		{
			result = "false";
		}
		PrintWriter out = null;
		out = this.getServletResponse().getWriter();
		out.write(result);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * �ر�
	 */
	public String close() throws Exception
	{
		// ɾ��session
		this.delMmsFileFromSession();
		PrintWriter out = this.getServletResponse().getWriter();
		out.write("true");
		out.flush();
		out.close();
		return null;
	}

	/**
	 * �鿴����
	 */

	public String showMms() throws Exception
	{
		// �ȴ����ݿ��ж����������ݣ�Ȼ�����ļ�����ʽд��tempĿ¼��
		if (this.getId() == null)
		{
			return SUCCESS;
		}
		MmsFile mmsFile = mmsFileService.getMms(Long.parseLong(this.getId()));
		// ������Ҫ������֡
		for (int i = 0; i < mmsFile.getFrames(); i++)
		{
			MmsFrame fr = new MmsFrame();
			mmsFile.getFrameMap().put(i + 1, fr);
		}

		// ��������upload�ļ�����tempԤ���ļ�������MmsFrame�е��ֶ�
		Iterator<UploadFile> it = mmsFile.getUploadFiles().iterator();
		while (it.hasNext())
		{
			UploadFile upload = it.next();
			int key = upload.getFramenumber();
			MmsFrame fr = mmsFile.getFrameMap().get(key);
			if (Constants.FILE_TYPE_TEXT.equals(upload.getFiletype()))
			{
				Blob blob = upload.getFiledata();
				fr.setText(FileUtil.getData(blob.getBinaryStream(), "gb2312"));
				fr.setTextFileName(upload.getFilename());
				fr.setTextFileSize(upload.getFilesize());
				fr.setTextFileType(upload.getFiletype());
			}
			else if (upload.getFiletype().indexOf("image") != -1)
			{
				Blob blob = upload.getFiledata();
				String filename = upload.getFilename();
				File file = this.getAbsoluteFile(filename);
				String relativefile = this.getRelativeFile(file);
				FileUtil.saveData(blob.getBinaryStream(), file);
				fr.setImage(relativefile);
				fr.setImageFileName(filename);
				fr.setImageFileSize(upload.getFilesize());
				fr.setImageFileType(upload.getFiletype());
			}
			else if (upload.getFiletype().indexOf("audio") != -1)
			{
				Blob blob = upload.getFiledata();
				String filename = upload.getFilename();
				File file = this.getAbsoluteFile(filename);
				String relativefile = this.getRelativeFile(file);
				FileUtil.saveData(blob.getBinaryStream(), file);
				fr.setAudio(relativefile);
				fr.setAudioFileName(filename);
				fr.setAudioFileSize(upload.getFilesize());
				fr.setAudioFileType(upload.getFiletype());
			}
		}

		// ͨ��smil�����ݸ����õ�����֡��duringTime
		ParseSmil parse = new ParseSmil(mmsFile.getContentSmil());
		parse.parse();
		for (int i = 0; i < parse.getFramecount(); i++)
		{
			ParseSmil.Frame f = parse.getFrames().get(i);
			MmsFrame fr = mmsFile.getFrameMap().get(f.framenumber);
			fr.setDuringTime(f.dur);
		}
		// ���¼����֡��С
		Iterator<Integer> keys = mmsFile.getFrameMap().keySet().iterator();
		while (keys.hasNext())
		{
			MmsFrame fr = mmsFile.getFrameMap().get(keys.next());
			this.reSizeFrame(fr);
		}
		this.getSession().put("showmms", mmsFile); // ������ʾ��ǰҳ�� //
		return SUCCESS;
	}

	/** ******************������һЩ��������******************* */

	private MmsFile getMmsFileFromSession()
	{
		MmsFile mmsFile = (MmsFile) this.getSession().get("mmsfile");
		if (mmsFile == null)
		{
			mmsFile = new MmsFile();
			this.getSession().put("mmsfile", mmsFile);
		}
		return mmsFile;
	}

	private void delMmsFileFromSession()
	{
		this.getSession().remove("mmsfile");
	}

	/* ��鵱ǰ֡id�Ƿ���ȷ */
	private boolean checkCurrentFrameId()
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		if (mmsFile.getCurrentFrameId() == null
				|| mmsFile.getCurrentFrameId() == 0)
		{
			this.setCallbackMsg("window.parent.alert('"
					+ getText("frame.count.null") + "')");
			this.addActionError(getText("frame.count.null"));
			return false;
		}
		return true;
	}

	/* ���ı��֡�Ǹ���ȷ��֡ */
	private boolean checkChangeFrameId()
	{
		if (this.getFrameId() == null)
		{
			this.setCallbackMsg("window.parent.alert('"
					+ getText("frameid.null") + "')");
			this.addActionError(getText("frameid.null"));
			return false;
		}
		MmsFile mmsFile = this.getMmsFileFromSession();
		int frameCount = mmsFile.getFrameMap().size();
		if (this.getFrameId() < 1 || this.getFrameId() > frameCount)
		{
			this.setCallbackMsg("window.parent.alert('"
					+ getText("frameid.error") + "')");
			this.addActionError(getText("frameid.error"));
			return false;
		}
		return true;
	}

	private boolean checkUploadFileNotEmpty(File file)
	{
		// ��֤�ļ��Ƿ�Ϊ�գ��ļ������Ƿ���ȷ���ļ���С�Ƿ񳬳����ܴ�С�Ƿ񳬳�
		if (file == null)
		{
			this.setCallbackMsg("window.parent.alert('" + getText("file.null")
					+ "')");
			this.addActionError(getText("file.null"));
			return false;
		}
		return true;
	}

	/**
	 * @param checkType
	 *            ֵimage��audio
	 * @param fileType
	 * @return ��֤ͨ��true
	 */
	private boolean checkUploadFileType(String checkType, String fileType)
	{
		if (checkType.equals("image"))
		{
			// ����ļ������Ƿ����
			if (this.getText("file.type.image").indexOf(fileType) == -1)
			{
				this.setCallbackMsg("window.parent.alert('"
						+ getText("file.type.image.error") + "')");
				this.addActionError(getText("file.type.image.error"));
				return false;
			}
		}
		else
		{
			// ����ļ������Ƿ����
			if (this.getText("file.audio").indexOf(fileType) == -1)
			{
				this.setCallbackMsg("window.parent.alert('"
						+ getText("file.type.audio.error") + "')");
				this.addActionError(getText("file.type.audio.error"));
				return false;
			}
		}
		return true;
	}

	private boolean checkUploadFileSize(long fileSize)
	{
		if (fileSize >= 50 * 1024)
		{
			this.setCallbackMsg("window.parent.alert('"
					+ getText("file.size.error") + "')");
			this.addActionError(getText("file.size.error"));
			return false;
		}
		return true;
	}

	/**
	 * �����Ŵ�С�Ƿ񳬳�
	 * 
	 * @param checkType
	 *            ֵΪimage��audio,text
	 * @param fileSize
	 * @return
	 */
	private boolean checkMmsSizeBeyond(String checkType, long fileSize)
	{
		// �������ܴ�С
		MmsFile mmsFile = this.getMmsFileFromSession();
		int frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);

		long oldSize = 0;
		if (checkType.equals("image"))
		{
			oldSize = fr.getImageFileSize();
		}
		else if (checkType.equals("audio"))
		{
			oldSize = fr.getAudioFileSize();
		}
		else
		{
			oldSize = fr.getTextFileSize();
		}

		if (fileSize > oldSize)
		{
			long mmsSize = 0;
			if (mmsFile.getMmsSize() != null)
			{
				mmsSize = mmsFile.getMmsSize();
			}
			if (mmsSize + (fileSize - oldSize) >= 100 * 1024)
			{
				this.setCallbackMsg("window.parent.alert('"
						+ getText("mmsSize.error") + "')");
				this.addActionError(getText("mmsSize.error"));
				return false;
			}
		}
		return true;

	}

	private boolean checkDuringTime()
	{
		if (this.getDuringTime() != null)
		{
			if (this.getDuringTime() <= 0)
			{
				this.setCallbackMsg("window.parent.alert('"
						+ getText("frame.duringTime.error") + "')");
				this.addActionError(this.getText("frame.duringTime.error"));
				return false;
			}
		}
		return true;
	}

	public boolean checkMmsNameNotEmpty()
	{
		if (mmsName == null)
		{
			this.setCallbackMsg("window.parent.alert('"
					+ getText("mmsName.null") + "')");
			this.addActionError(getText("mmsName.null"));
			return false;
		}
		return true;
	}

	/**
	 * ȡ���������Ŀ��·��
	 */
	private String getRelativeFile(File file)
	{
		return Constants.UPLOAD_FILE_DIR.substring(1) + File.separator
				+ file.getName();
	}

	/**
	 * ����ԭ�ļ�����frameid����һ���µ��ļ�
	 * 
	 * @param originalfileName
	 * @param frameid
	 * @return
	 */
	private File generateFile(String originalfileName, int frameid)
	{
		// �������ļ���
		String generateFilename = Tools.getRandomFileName(frameid,
				originalfileName);
		// String relativeFilename = getRelativeFile(generateFilename);
		String absolutePath = ServletActionContext.getServletContext()
				.getRealPath(Constants.UPLOAD_FILE_DIR);

		File path = new File(absolutePath);
		if (!path.exists())
		{
			path.mkdirs();
		}
		File file = new File(path, generateFilename);

		return file;
	}

	private File getAbsoluteFile(String filname)
	{

		String absolutePath = ServletActionContext.getServletContext()
				.getRealPath(Constants.UPLOAD_FILE_DIR);

		File path = new File(absolutePath);
		if (!path.exists())
		{
			path.mkdirs();
		}
		File file = new File(path, filname);

		return file;
	}

	private void cleanAllUploadFile()
	{
		String absolutePath = ServletActionContext.getServletContext()
				.getRealPath(Constants.UPLOAD_FILE_DIR);

		File path = new File(absolutePath);
		if (!path.exists())
		{
			path.mkdirs();
		}
		else
		{
			if (path.isDirectory())
			{
				path.delete();
			}
		}

	}

	private void reSizeFrame(MmsFrame fr)
	{
		long frameSize = fr.getImageFileSize() + fr.getAudioFileSize()
				+ fr.getTextFileSize();
		fr.setFrameSize(frameSize);
	}

	/**
	 * @param mms
	 */
	private void reSizeMms(MmsFile mms)
	{
		int size = 0;
		Map<Integer, MmsFrame> map = mms.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			size += map.get(it.next()).getFrameSize();
		}
		mms.setMmsSize(size);
	}

	private void reSizeFrameAndMms(MmsFrame fr, MmsFile mms)
	{
		this.reSizeFrame(fr);
		mms.setCurrentFrameSize(fr.getFrameSize());
		this.reSizeMms(mms);
	}

	/**
	 * ��������uploadFile����
	 * 
	 * @param mmsFile
	 * @throws IOException
	 * @throws SQLException
	 */
	private void createAllUploadFile(MmsFile mmsFile) throws IOException,
			SQLException
	{
		UploadFile uploadFile;
		MmsFrame fr;
		mmsFile.getUploadFiles().clear();
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			Integer key = it.next();
			fr = map.get(key);
			if (Tools.isNotEmpty(fr.getImage()))
			{
				uploadFile = new UploadFile();
				File file = getAbsoluteFile(fr.getImageFileName());
				FileInputStream input = new FileInputStream(file);
				Blob blob = Hibernate.createBlob(input);
				uploadFile.setFiledata(blob);// �����ļ�����
				uploadFile.setFilename(file.getName());// �����ļ���
				uploadFile.setFilesize(blob.length());// �����ļ���С
				uploadFile.setFiletype(fr.getImageFileType());// �����ļ�����
				uploadFile.setFramenumber(key);// ����֡��
				uploadFile.setUploadtime(new Date());
				mmsFile.getUploadFiles().add(uploadFile);

			}
			if (Tools.isNotEmpty(fr.getAudio()))
			{
				uploadFile = new UploadFile();
				File file = getAbsoluteFile(fr.getAudioFileName());
				FileInputStream input = new FileInputStream(file);
				Blob blob = Hibernate.createBlob(input);
				uploadFile.setFiledata(blob);// �����ļ�����
				uploadFile.setFilename(file.getName());// �����ļ���
				uploadFile.setFilesize(blob.length());// �����ļ���С
				uploadFile.setFiletype(this.getAudioContentType());// �����ļ�����
				uploadFile.setFramenumber(key);// ����֡��
				uploadFile.setUploadtime(new Date());
				mmsFile.getUploadFiles().add(uploadFile);

			}
			if (Tools.isNotEmpty(fr.getText()))
			{
				uploadFile = new UploadFile();
				byte[] data = fr.getText().getBytes();
				ByteArrayInputStream input = new ByteArrayInputStream(data);
				Blob blob = Hibernate.createBlob(input);
				uploadFile.setFiledata(blob);// �����ļ�����
				uploadFile.setFilesize(blob.length());// �����ļ���С
				uploadFile.setFilename(fr.getTextFileName());// �����ļ���
				uploadFile.setFramenumber(key);// ����֡��
				uploadFile.setUploadtime(new Date());
				uploadFile.setFiletype(Constants.FILE_TYPE_TEXT);
				mmsFile.getUploadFiles().add(uploadFile);

			}
		}

	}

	private String createSmil(MmsFile mmsFile)
	{
		MakeSmil smil = new MakeSmil();
		// String path="D:/";
		// String name="test1";
		// ��������
		// �����ļ����·��
		// sml.setSmilPath(path);
		// �����ļ���
		// sml.setSmilName(name);
		// �����ļ�����
		// sml.resetSmil();//�����ļ�����
		smil.smilAddHead();// ������ļ�ͷ����Ϣ��
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			Integer key = it.next();
			MmsFrame fr = map.get(key);
			smil.setSmilParStart(fr.getDuringTime());// ���ſ�ʼ���
			if (Tools.isNotEmpty(fr.getImage()))
			{
				smil.smilAddImg(fr.getImageFileName());
			}
			if (Tools.isNotEmpty(fr.getText()))
			{
				smil.smilAddTxt(fr.getTextFileName());
			}
			if (Tools.isNotEmpty(fr.getAudio()))
			{
				smil.smilAddAudio(fr.getAudioFileName());
			}
			smil.setSmilParEnd();

		}
		smil.smilAddFoot();

		return smil.getFileContent();
	}

	private void changeCurrentFrame(MmsFile mmsFile, int frameid)
	{
		MmsFrame frame = mmsFile.getFrameMap().get(frameid);
		mmsFile.setCurrentFrameId(frameid);
		mmsFile.setCurrentFrameSize(frame.getFrameSize());
		mmsFile.setCurrentDuringTime(frame.getDuringTime());
		mmsFile.setCurrentFrameText(frame.getText());
	}

	private void clearCurrentFrame(MmsFile mmsFile)
	{
		mmsFile.setCurrentFrameId(null);
		mmsFile.setCurrentFrameSize(null);
		mmsFile.setCurrentDuringTime(null);
		mmsFile.setCurrentFrameText(null);
	}

}
