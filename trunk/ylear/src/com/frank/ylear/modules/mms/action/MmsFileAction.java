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

	/* 当前帧 */
	// private Integer currentFrameId;
	private Integer frameId;

	/* 当前帧播放时间 */
	private Integer duringTime;

	/* 彩信名称 */
	private String mmsName;

	/* 提供给查看彩信页面查询用 */
	private String id;

	private String callbackMsg;// 回调消息

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
	/* 彩信编辑器主页 */
	public String execute() throws Exception
	{
		return SUCCESS;
	}

	/**
	 * 彩信ui界面
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
		// 检查帧id是否正确
		this.checkChangeFrameId();
	}

	public void validateDelFrame()
	{
	}

	public void validateUploadImage()
	{
		// 验证文件是否为空，文件类型是否正确，文件大小是否超出，总大小是否超出
		File file = this.getImage();
		String fileType = this.getImageContentType();
		if (!checkUploadFileNotEmpty(file))
		{
			return;
		}
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}
		// 检查文件类型是否符合
		if (!checkUploadFileType("image", fileType))
		{
			return;
		}
		// 检查文件大小
		if (!checkUploadFileSize(file.length()))
		{
			return;
		}
		// 检查彩信总大小
		if (!checkMmsSizeBeyond("image", file.length()))
		{
			return;
		}

	}

	public void validateClearImage()
	{
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateUploadAudio()
	{
		// 验证文件是否为空，文件类型是否正确，文件大小是否超出，总大小是否超出
		File file = this.getAudio();
		String fileType = this.getAudioContentType();
		if (!checkUploadFileNotEmpty(file))
		{
			return;
		}
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}
		// 检查文件类型是否符合
		if (!checkUploadFileType("audio", fileType))
		{
			return;
		}
		// 检查文件大小
		if (!checkUploadFileSize(file.length()))
		{
			return;
		}
		// 检查彩信总大小
		if (!checkMmsSizeBeyond("audio", file.length()))
		{
			return;
		}

	}

	public void validateClearAudio()
	{
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateUploadText()
	{
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}

		// 检查彩信总大小
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
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateChangeDuringTime()
	{
		// 检查当前是否有帧存在
		if (!checkCurrentFrameId())
		{
			return;
		}
		// 检查当前帧间隔时间是否正确
		if (!checkDuringTime())
		{
			return;
		}
	}

	/**
	 * 插入帧
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
			// 当前彩信为空
			frameid = 1;
			// 如果彩信大小为空，那么置为0
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
		// 备份需要移动的节点
		Set<Integer> keySet = map.keySet();
		int keyLen = keySet.size();
		Integer[] keys = new Integer[keyLen];
		keySet.toArray(keys);
		// 定义一个temp
		Map<Integer, MmsFrame> temp = new LinkedHashMap<Integer, MmsFrame>();

		for (int i = 0; i < keys.length; i++)
		{
			if (frameid <= keys[i])
			{
				// 把需要移动的帧转移到另一个map里，最好在统一移回去
				MmsFrame fr = map.remove(keys[i]);
				temp.put(keys[i] + 1, fr);
			}
		}

		// 为了达到排序效果，先插入
		MmsFrame frame = new MmsFrame();
		map.put(frameid, frame);
		// 转移回去
		if (temp.size() > 0)
		{
			map.putAll(temp);
			temp.clear();
		}
		// 当前帧被改变了
		this.changeCurrentFrame(mmsFile, frameid);

		// 回调函数
		this.setCallbackMsg("chooseFrame_callback()");
		return SHOWUI;
	}

	/**
	 * 选择帧
	 * 
	 * @return
	 * @throws Exception
	 */
	public String chooseFrame() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		int frameid = this.getFrameId();
		this.changeCurrentFrame(mmsFile, frameid);
		// 回调函数
		this.setCallbackMsg("chooseFrame_callback()");
		return SHOWUI;
	}

	/**
	 * 删除帧
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delFrame() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = this.getFrameId();
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();

		// 先删除，后移动
		map.remove(frameid);

		Set<Integer> keySet = map.keySet();
		int keyLen = keySet.size();
		Integer[] keys = new Integer[keyLen];
		keySet.toArray(keys);
		// 定义一个temp
		Map<Integer, MmsFrame> temp = new LinkedHashMap<Integer, MmsFrame>();

		for (int i = 0; i < keys.length; i++)
		{
			if (frameid < keys[i])
			{
				// 把需要移动的帧转移到另一个map里，最好在统一移回去
				MmsFrame fr = map.remove(keys[i]);
				temp.put(keys[i] - 1, fr);
			}
		}
		// 转移回去
		if (temp.size() > 0)
		{
			map.putAll(temp);
			temp.clear();
		}

		// 设置当前帧
		Integer currentFrameNumber = frameid;
		MmsFrame mf = map.get(frameid);
		if (mf == null)
		{
			currentFrameNumber = frameid - 1;
			mf = map.get(currentFrameNumber);
		}
		if (mf == null)
		{
			// 当前彩信为空
			currentFrameNumber = null;
			this.clearCurrentFrame(mmsFile);
		}
		else
		{
			// 设置当前帧
			this.changeCurrentFrame(mmsFile, currentFrameNumber);
		}
		// 回调函数
		this.setCallbackMsg("chooseFrame_callback()");
		return SHOWUI;

	}

	/**
	 * 上传图片文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadImage() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 设置image
		long fileSize = this.getImage().length();
		// 随机生成一个文件
		File newFile = this.generateFile(this.getImageFileName(), frameid);
		// copy File
		Tools.copyFile(this.getImage(), newFile);
		fr.setImage(getRelativeFile(newFile));
		fr.setImageFileName(newFile.getName());
		fr.setImageFileSize(fileSize);
		fr.setImageFileType(this.getImageContentType());

		// 重新计算帧大小和彩信大小
		reSizeFrameAndMms(fr, mmsFile);
		// 设置回调函数
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 * 上传铃声文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadAudio() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 设置audio
		long fileSize = this.getAudio().length();
		// 随机生成一个文件名
		File newFile = this.generateFile(this.getAudioFileName(), frameid);
		// copy File
		Tools.copyFile(this.getAudio(), newFile);
		fr.setAudio(getRelativeFile(newFile));
		fr.setAudioFileName(newFile.getName());
		fr.setAudioFileSize(fileSize);
		fr.setAudioFileType(this.getAudioContentType());

		// 重新计算帧大小和彩信大小
		reSizeFrameAndMms(fr, mmsFile);
		// 设置回调函数
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 * 上传文本
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadText() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 设置text
		String text = this.getFrameText();
		if (text == null)
		{
			text = "";
		}
		long fileSize = text.length();
		// 随机生成一个文件名
		File newFile = this.generateFile("temp.txt", frameid);
		fr.setText(text);
		fr.setTextFileName(newFile.getName());
		fr.setTextFileSize(fileSize);

		// 重新计算帧大小和彩信大小
		reSizeFrameAndMms(fr, mmsFile);
		// 设置回调函数
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 * 清除图片文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearImage() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 清空image
		fr.setImage(null);
		fr.setImageFileName(null);
		fr.setImageFileSize(0);
		fr.setImageFileType(null);
		// 重新计算帧大小和彩信大小
		reSizeFrameAndMms(fr, mmsFile);
		// 设置回调函数
		this.setCallbackMsg("clearImage_callback()");
		return SHOWUI;

	}

	/**
	 * 清除铃声文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearAudio() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 清空audio
		fr.setAudio(null);
		fr.setAudioFileName(null);
		fr.setAudioFileSize(0);
		fr.setAudioFileType(null);
		// 重新计算帧大小和彩信大小
		reSizeFrameAndMms(fr, mmsFile);
		// 设置回调函数
		this.setCallbackMsg("clearAudio_callback()");
		return SHOWUI;

	}

	/**
	 * 清除文本
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearText() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 清空text
		fr.setText(null);
		fr.setTextFileName(null);
		fr.setTextFileSize(0);
		// 重新计算帧大小和彩信大小
		reSizeFrameAndMms(fr, mmsFile);
		// 设置回调函数
		this.setCallbackMsg("clearText_callback()");
		return SHOWUI;

	}

	/**
	 * 变更帧的播放时间
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
	 * 保存彩信
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
		List<InputStream> files = new ArrayList<InputStream>();// 保存需要关闭的对象
		try
		{
			// 下面是把文件的信息写到uploadFile对象中 begin
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
					uploadFile.setFiledata(blob);// 设置文件内容
					uploadFile.setFilename(file.getName());// 设置文件名
					uploadFile.setFilesize(blob.length());// 设置文件大小
					uploadFile.setFiletype(fr.getImageFileType());// 设置文件类型
					uploadFile.setFramenumber(key);// 设置帧号
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
					uploadFile.setFiledata(blob);// 设置文件内容
					uploadFile.setFilename(file.getName());// 设置文件名
					uploadFile.setFilesize(blob.length());// 设置文件大小
					uploadFile.setFiletype(this.getAudioContentType());// 设置文件类型
					uploadFile.setFramenumber(key);// 设置帧号
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
					uploadFile.setFiledata(blob);// 设置文件内容
					uploadFile.setFilesize(blob.length());// 设置文件大小
					uploadFile.setFilename(fr.getTextFileName());// 设置文件名
					uploadFile.setFramenumber(key);// 设置帧号
					uploadFile.setUploadtime(new Date());
					uploadFile.setFiletype(Constants.FILE_TYPE_TEXT);
					mmsFile.getUploadFiles().add(uploadFile);

				}
			}
			// end 下面是把文件的信息写到uploadFile对象中

			String smil = this.createSmil(mmsFile);// 创建smil
			int smilSize = smil.getBytes().length;
			// int mmsSize = smilSize + mmsFile.getMmsSize();// 彩信总大小
			mmsFile.setMmsName(mmsName);
			mmsFile.setContentSize(smilSize);
			mmsFile.setContentSmil(smil);
			frames = mmsFile.getFrameMap().size();
			mmsFile.setFrames(frames);
			mmsFile.setSmilname("1.smil");
			mmsFile.setCreatetime(new Date());
			id = (Long) mmsFileService.add(mmsFile);
			// 这里删除session
			delMmsFileFromSession();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			flag = false;
		}
		finally
		{
			// 关闭文件指针
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
			// 清空所有文件
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
	 * 关闭
	 */
	public String close() throws Exception
	{
		// 删除session
		this.delMmsFileFromSession();
		PrintWriter out = this.getServletResponse().getWriter();
		out.write("true");
		out.flush();
		out.close();
		return null;
	}

	/**
	 * 查看彩信
	 */

	public String showMms() throws Exception
	{
		// 先从数据库中读出所有数据，然后以文件的形式写到temp目录中
		if (this.getId() == null)
		{
			return SUCCESS;
		}
		MmsFile mmsFile = mmsFileService.getMms(Long.parseLong(this.getId()));
		// 创建需要的所有帧
		for (int i = 0; i < mmsFile.getFrames(); i++)
		{
			MmsFrame fr = new MmsFrame();
			mmsFile.getFrameMap().put(i + 1, fr);
		}

		// 根据所有upload文件创建temp预览文件并设置MmsFrame中的字段
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

		// 通过smil的内容辅助得到各个帧的duringTime
		ParseSmil parse = new ParseSmil(mmsFile.getContentSmil());
		parse.parse();
		for (int i = 0; i < parse.getFramecount(); i++)
		{
			ParseSmil.Frame f = parse.getFrames().get(i);
			MmsFrame fr = mmsFile.getFrameMap().get(f.framenumber);
			fr.setDuringTime(f.dur);
		}
		// 重新计算各帧大小
		Iterator<Integer> keys = mmsFile.getFrameMap().keySet().iterator();
		while (keys.hasNext())
		{
			MmsFrame fr = mmsFile.getFrameMap().get(keys.next());
			this.reSizeFrame(fr);
		}
		this.getSession().put("showmms", mmsFile); // 重新显示当前页面 //
		return SUCCESS;
	}

	/** ******************下面是一些辅助方法******************* */

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

	/* 检查当前帧id是否正确 */
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

	/* 检查改变的帧是个正确的帧 */
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
		// 验证文件是否为空，文件类型是否正确，文件大小是否超出，总大小是否超出
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
	 *            值image或audio
	 * @param fileType
	 * @return 验证通过true
	 */
	private boolean checkUploadFileType(String checkType, String fileType)
	{
		if (checkType.equals("image"))
		{
			// 检查文件类型是否符合
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
			// 检查文件类型是否符合
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
	 * 检查彩信大小是否超出
	 * 
	 * @param checkType
	 *            值为image，audio,text
	 * @param fileSize
	 * @return
	 */
	private boolean checkMmsSizeBeyond(String checkType, long fileSize)
	{
		// 检查彩信总大小
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
	 * 取得相对于项目的路径
	 */
	private String getRelativeFile(File file)
	{
		return Constants.UPLOAD_FILE_DIR.substring(1) + File.separator
				+ file.getName();
	}

	/**
	 * 根据原文件名和frameid生成一个新的文件
	 * 
	 * @param originalfileName
	 * @param frameid
	 * @return
	 */
	private File generateFile(String originalfileName, int frameid)
	{
		// 生成新文件名
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
	 * 创建所有uploadFile对象
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
				uploadFile.setFiledata(blob);// 设置文件内容
				uploadFile.setFilename(file.getName());// 设置文件名
				uploadFile.setFilesize(blob.length());// 设置文件大小
				uploadFile.setFiletype(fr.getImageFileType());// 设置文件类型
				uploadFile.setFramenumber(key);// 设置帧号
				uploadFile.setUploadtime(new Date());
				mmsFile.getUploadFiles().add(uploadFile);

			}
			if (Tools.isNotEmpty(fr.getAudio()))
			{
				uploadFile = new UploadFile();
				File file = getAbsoluteFile(fr.getAudioFileName());
				FileInputStream input = new FileInputStream(file);
				Blob blob = Hibernate.createBlob(input);
				uploadFile.setFiledata(blob);// 设置文件内容
				uploadFile.setFilename(file.getName());// 设置文件名
				uploadFile.setFilesize(blob.length());// 设置文件大小
				uploadFile.setFiletype(this.getAudioContentType());// 设置文件类型
				uploadFile.setFramenumber(key);// 设置帧号
				uploadFile.setUploadtime(new Date());
				mmsFile.getUploadFiles().add(uploadFile);

			}
			if (Tools.isNotEmpty(fr.getText()))
			{
				uploadFile = new UploadFile();
				byte[] data = fr.getText().getBytes();
				ByteArrayInputStream input = new ByteArrayInputStream(data);
				Blob blob = Hibernate.createBlob(input);
				uploadFile.setFiledata(blob);// 设置文件内容
				uploadFile.setFilesize(blob.length());// 设置文件大小
				uploadFile.setFilename(fr.getTextFileName());// 设置文件名
				uploadFile.setFramenumber(key);// 设置帧号
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
		// 创建对象
		// 设置文件存放路径
		// sml.setSmilPath(path);
		// 设置文件名
		// sml.setSmilName(name);
		// 设置文件内容
		// sml.resetSmil();//重置文件内容
		smil.smilAddHead();// 并添加文件头部信息。
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			Integer key = it.next();
			MmsFrame fr = map.get(key);
			smil.setSmilParStart(fr.getDuringTime());// 彩信开始标记
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
