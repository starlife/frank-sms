package com.frank.ylear.modules.mms.action;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.frank.ylear.common.constant.Constants;
import com.frank.ylear.common.util.Tools;
import com.frank.ylear.modules.base.action.BaseAction;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.service.MmsFileService;
import com.frank.ylear.modules.mms.util.MMSFileHelper;
import com.frank.ylear.modules.mms.util.MmsFrame;

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

	/** ***================================================================** */
	/* 彩信编辑器主页 */
	public String mmsEditor() throws Exception
	{
		return SUCCESS;
	}

	/**
	 * 彩信ui界面
	 */
	public String showui() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();
		if (mmsFile.getCurrentFrameId() != null)
		{
			this.setCallbackMsg("choiceFrame_callback()");
		}
		return SHOWUI;
	}

	/**
	 * 列表显示
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		// mmsService.getMmsList(this.getQueryBean(), this.getPage());
		//List<MmsFile> list=mmsFileService.getRecentMms(10);
		//this.getPage().setList(list);
		mmsFileService.getMmsFileList(this.getPage());
		return SUCCESS;
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
		String result = "";
		if (mmsName == null)
		{
			flag = false;
			result = "mmsName.null";
		}
		if (flag)
		{
			if (mmsFile.getMmsSize() == null || mmsFile.getMmsSize() == 0)
			{
				flag = false;
				result = "mmsSize.null";
			}
		}
		if (flag)
		{
			int frames = mmsFile.getFrameMap().size();
			String absolutePath = ServletActionContext.getServletContext()
					.getRealPath("");
			Long id = MMSFileHelper.saveMmsFile(mmsFileService, mmsFile, this
					.getMmsName(), absolutePath);

			if (id != null)
			{
				// 这里删除session
				delMmsFileFromSession();
				// 清空所有文件
				String dir = ServletActionContext.getServletContext()
						.getRealPath(Constants.UPLOAD_FILE_DIR);
				MMSFileHelper.cleanAllUploadFile(dir);
				result = String.valueOf(id) + "," + String.valueOf(frames);
			}
			else
			{
				flag = false;
				result = "";
			}
		}

		PrintWriter out = null;
		out = this.getServletResponse().getWriter();
		out.write(flag + "," + result);
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
	 * 彩信查看器
	 */

	public String mmsViewer() throws Exception
	{
		// 先从数据库中读出所有数据，然后以文件的形式写到temp目录中
		if (this.getId() == null)
		{
			return SUCCESS;
		}
		MmsFile mmsFile = mmsFileService.getMms(Long.parseLong(this.getId()));

		String absolutePath = ServletActionContext.getServletContext()
				.getRealPath("");
		// 组装彩信
		MMSFileHelper.makeMMS(mmsFile, absolutePath);

		this.getSession().put("showmms", mmsFile); // 重新显示当前页面 //
		return SUCCESS;
	}

	public void validateChoiceFrame()
	{
		// 检查帧id是否正确
		this.checkChangeFrameId();
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
		MmsFile mmsFile = getMmsFileFromSession();

		MMSFileHelper.addFrame(mmsFile);

		// 回调函数
		this.setCallbackMsg("choiceFrame_callback()");
		return SHOWUI;
	}

	/**
	 * 选择帧
	 * 
	 * @return
	 * @throws Exception
	 */
	public String choiceFrame() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();

		MMSFileHelper.changeCurrentFrame(mmsFile, this.getFrameId());
		// 回调函数
		this.setCallbackMsg("choiceFrame_callback()");
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

		// 删除帧
		MMSFileHelper.delFrame(mmsFile, frameid);

		// 回调函数
		this.setCallbackMsg("choiceFrame_callback()");
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
		// 上传文件
		this.uploadFile(this.getImage(), this.getImageFileName());
		MMSFileHelper.uploadImage(mmsFile, getImage(), getImageFileName(),
				getImageContentType());

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
		// 上传文件
		this.uploadFile(getAudio(), getAudioFileName());

		MMSFileHelper.uploadAudio(mmsFile, getAudio(), getAudioFileName(),
				getAudioContentType());

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

		MMSFileHelper.uploadText(mmsFile, getFrameText());
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

		MMSFileHelper.clearImage(mmsFile);
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

		MMSFileHelper.clearAudio(mmsFile);
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

		MMSFileHelper.clearText(mmsFile);
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

		MmsFile mmsFile = getMmsFileFromSession();

		MMSFileHelper.changeDuringTime(mmsFile, this.getDuringTime());

		return SHOWUI;

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
			String tip = getText("frame.count.null");
			setErrorTip(tip);
			return false;
		}
		return true;
	}

	/* 检查改变的帧是个正确的帧 */
	private boolean checkChangeFrameId()
	{
		if (this.getFrameId() == null)
		{
			String tip = getText("frameid.null");
			setErrorTip(tip);
			return false;
		}
		MmsFile mmsFile = this.getMmsFileFromSession();
		int frameCount = mmsFile.getFrameMap().size();
		if (this.getFrameId() < 1 || this.getFrameId() > frameCount)
		{
			String tip = getText("frameid.error");
			setErrorTip(tip);
			return false;
		}
		return true;
	}

	private boolean checkUploadFileNotEmpty(File file)
	{
		// 验证文件是否为空，文件类型是否正确，文件大小是否超出，总大小是否超出
		if (file == null)
		{
			String tip = getText("file.null");
			setErrorTip(tip);
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
				String tip = getText("file.type.image.error");
				setErrorTip(tip);
				return false;
			}
		}
		else
		{
			// 检查文件类型是否符合
			if (this.getText("file.audio").indexOf(fileType) == -1)
			{
				String tip = getText("file.type.audio.error");
				setErrorTip(tip);
				return false;
			}
		}
		return true;
	}

	private boolean checkUploadFileSize(long fileSize)
	{
		if (fileSize >= 50 * 1024)
		{
			String tip = getText("file.size.error");
			setErrorTip(tip);
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
				String tip = getText("mmsSize.error");
				setErrorTip(tip);
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
				String tip = getText("frame.duringTime.error");
				setErrorTip(tip);
				return false;
			}
		}
		return true;
	}

	/*
	 * private boolean checkMmsNameNotEmpty() { if (mmsName == null) { String
	 * tip = getText("mmsName.null"); setErrorTip(tip); return false; } return
	 * true; } private boolean checkMmsNotEmpty() { MmsFile mmsFile =
	 * this.getMmsFileFromSession(); if (mmsFile.getMmsSize() == null ||
	 * mmsFile.getMmsSize() == 0) { String tip = getText("mmsSize.null");
	 * setErrorTip(tip); return false; } return true; }
	 */

	private void setErrorTip(String tip)
	{
		this.setCallbackMsg("window.parent.alert('" + tip + "')");
		this.addActionError(tip);
	}

	private boolean uploadFile(File file, String newFileName)
	{
		String absolutePath = ServletActionContext.getServletContext()
				.getRealPath(Constants.UPLOAD_FILE_DIR);
		File path = new File(absolutePath);
		if (!path.exists())
		{
			path.mkdirs();
		}
		File dest = new File(path + File.separator + newFileName);
		return Tools.copyFile(file, dest);
	}

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

}
