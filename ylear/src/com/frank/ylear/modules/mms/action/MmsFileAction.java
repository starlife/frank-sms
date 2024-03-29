package com.frank.ylear.modules.mms.action;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;

import org.apache.struts2.ServletActionContext;

import com.frank.ylear.common.constant.Constants;
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

	private MmsFile queryBean = null;

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

	/* ������� */
	private String mmsName;

	/**/
	private String id;

	private String callbackMsg;//

	private String mType;
	private String title;

	/** ***================================================================** */
	/**/
	public String mmsEditor() throws Exception
	{
		if (this.getId() != null)
		{
			//�գ���ô�Ƕ�ԭ�в��ŵı༭
			MmsFile mmsFile = mmsFileService.getMmsFile(Long.parseLong(this
					.getId()));
			//���
			MMSFileHelper.makeMMS(mmsFile, this.getRealPath());
			//��ǰ֡
			if (mmsFile.getFrames() > 0)
			{
				MMSFileHelper.changeCurrentFrame(mmsFile, 1);
			}
			// ���ò������
			this.setMmsName(mmsFile.getMmsName());
			this.getSession().put("mmsfile", mmsFile);
		}
		else
		{
			//
			MmsFile mmsFile = this.getMmsFileFromSession();
			if (mmsFile.getId() != null)
			{
				this.delMmsFileFromSession();
			}
		}

		return SUCCESS;
	}

	
	public String mmsFrom3g() throws Exception
	{
		boolean flag = false;
		//String tip="";
		if (this.getMType() != null && this.getTitle() != null)
		{
			this.delMmsFileFromSession();
			MmsFile mmsFile = this.getMmsFileFromSession();
			// ��ʼ������session
			String urlFormat = "http://interface.tourzj.gov.cn/MobilePaper/default.aspx?Mtype=%s&Title=%s";
			String url = String.format(urlFormat, URLEncoder.encode(getMType(),
					"UTF-8"), URLEncoder.encode(getTitle(), "UTF-8"));
			flag = MMSFileHelper
					.makeMMSFrom3G(mmsFile,getRealPath(), url);
			if(flag)
			{
				//���»��ǲ���
				MmsFile temp=mmsFileService.getMmsFile(mmsFile.getMmsName());
				if(temp!=null)
				{
					mmsFile.setId(temp.getId());
				}
				Long id = MMSFileHelper.saveMmsFile(mmsFileService, mmsFile,getRealPath());
				if (id != null||(id==null&&temp!=null))
				{
					// ����ɾ��session
					delMmsFileFromSession();
					// ��������ļ�
					String dir = getRealPath()+File.separator+Constants.UPLOAD_FILE_DIR;
					MMSFileHelper.cleanAllUploadFile(dir);
				}else
				{
					//����ʧ��
					//tip="�������ʧ�ܣ����Ժ�����";
					flag = false;
				}
			}else
			{
				//tip="��ȡ����ʧ�ܣ������Ƿ���";
				;
			}

		}
		PrintWriter out = null;
		out = this.getServletResponse().getWriter();
		out.write(flag + "");
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * ����
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toAdd3g() throws Exception
	{

		return SUCCESS;
	}

	/**
	 * ����ui����
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
	 * �б���ʾ
	 * 
	 * @return
	 * @throws Exception
	 */
	public String list() throws Exception
	{
		mmsFileService.getMmsFileList(this.getQueryBean(), this.getPage());
		return SUCCESS;
	}

	public String customList() throws Exception
	{
		mmsFileService.getMmsFileList(this.getQueryBean(), this.getPage());
		return SUCCESS;
	}

	public String del() throws Exception
	{
		if (this.getId() != null)
		{
			mmsFileService.delMmsFile(Long.parseLong(this.getId()));
		}
		return SUCCESS;
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
			mmsFile.setMmsName(getMmsName());
			Long id = MMSFileHelper.saveMmsFile(mmsFileService, mmsFile,this.getRealPath());
			if (id == null)
			{
				id = mmsFile.getId();
			}
			if (id != null)
			{
				// ����ɾ��session
				delMmsFileFromSession();
				// ��������ļ�
				String dir = getRealPath()+File.separator+Constants.UPLOAD_FILE_DIR;
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

	
	public String close() throws Exception
	{
		//关闭session
		this.delMmsFileFromSession();
		PrintWriter out = this.getServletResponse().getWriter();
		out.write("true");
		out.flush();
		out.close();
		return null;
	}

	/**
	 * ���Ų鿴��
	 */

	public String mmsViewer() throws Exception
	{
		// �ȴ���ݿ��жs�������ݣ�Ȼ�����ļ�����ʽд��tempĿ¼��
		if (this.getId() == null)
		{
			return SUCCESS;
		}
		MmsFile mmsFile = mmsFileService.getMmsFile(Long
				.parseLong(this.getId()));
		if (mmsFile == null)
		{
			return SUCCESS;
		}
		
		//����
		MMSFileHelper.makeMMS(mmsFile,this.getRealPath());
		this.setMmsName(mmsFile.getMmsName());
		this.getSession().put("showmms", mmsFile); //
		return SUCCESS;
	}

	public void validateChoiceFrame()
	{
		//
		this.checkChangeFrameId();
	}

	public void validateUploadImage()
	{
		// ��֤�ļ��Ƿ�Ϊ�գ��ļ������Ƿ���ȷ���ļ���С�Ƿ񳬳��ܴ�С�Ƿ񳬳�
		File file = this.getImage();
		String fileType = this.getImageContentType();
		if (!checkUploadFileNotEmpty(file))
		{
			return;
		}
		//֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
		// ����ļ������Ƿ���
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
		//֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateUploadAudio()
	{
		//�գ��ļ������Ƿ���ȷ���ļ���С�Ƿ񳬳��ܴ�С�Ƿ񳬳�
		File file = this.getAudio();
		String fileType = this.getAudioContentType();
		if (!checkUploadFileNotEmpty(file))
		{
			return;
		}
		//֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
		// ����ļ������Ƿ���
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
		//֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateUploadText()
	{
		//֡����
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
		//֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
	}

	public void validateChangeDuringTime()
	{
		//֡����
		if (!checkCurrentFrameId())
		{
			return;
		}
		//���ʱ���Ƿ���ȷ
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
		MmsFile mmsFile = getMmsFileFromSession();

		MMSFileHelper.addFrame(mmsFile);
		//��
		this.setCallbackMsg("choiceFrame_callback()");
		return SHOWUI;
	}

	/**
	 * ѡ��֡
	 * 
	 * @return
	 * @throws Exception
	 */
	public String choiceFrame() throws Exception
	{
		MmsFile mmsFile = this.getMmsFileFromSession();

		MMSFileHelper.changeCurrentFrame(mmsFile, this.getFrameId());
		//
		//
		this.setCallbackMsg("choiceFrame_callback()");
		return SHOWUI;
	}

	/**
	 *֡
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delFrame() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		Integer frameid = this.getFrameId();

		//֡
		MMSFileHelper.delFrame(mmsFile, frameid);
		
		//
		this.setCallbackMsg("choiceFrame_callback()");
		return SHOWUI;

	}

	/**
	 *
	 * 
	 * @return
	 * @throws Exception
	 */
	public String uploadImage() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();
		MMSFileHelper.uploadImage(mmsFile, getRealPath(), getImage(),
				getImageFileName(), getImageContentType());

		//
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
		MMSFileHelper.uploadAudio(mmsFile, getRealPath(), getAudio(),
				getAudioFileName(), getAudioContentType());

		//
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

		MMSFileHelper.uploadText(mmsFile, getFrameText());
		//
		this.setCallbackMsg("upload_callback()");
		return SHOWUI;

	}

	/**
	 *
	 * 
	 * @return
	 * @throws Exception
	 */
	public String clearImage() throws Exception
	{

		MmsFile mmsFile = this.getMmsFileFromSession();

		MMSFileHelper.clearImage(mmsFile);
		//
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

		MMSFileHelper.clearAudio(mmsFile);
		//
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

		MMSFileHelper.clearText(mmsFile);
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

		MmsFile mmsFile = getMmsFileFromSession();

		MMSFileHelper.changeDuringTime(mmsFile, this.getDuringTime());

		return SHOWUI;

	}

	/** ******************������帮助函数******************* */

	private MmsFile getMmsFileFromSession()
	{
		MmsFile mmsFile = null;
		Object obj = this.getSession().get("mmsfile");
		if (obj == null || !(obj instanceof MmsFile))
		{
			mmsFile = new MmsFile();
			this.getSession().put("mmsfile", mmsFile);
		}
		else
		{
			mmsFile = (MmsFile) obj;
		}
		return mmsFile;
	}

	private void delMmsFileFromSession()
	{
		this.getSession().remove("mmsfile");
	}

	
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

	/* ���ı��֡�Ǹ���ȷ��֡ */
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
		//�գ��ļ������Ƿ���ȷ���ļ���С�Ƿ񳬳��ܴ�С�Ƿ񳬳�
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
	 *      
	 * @param fileType
	 * @return ��֤ͨ��true
	 */
	private boolean checkUploadFileType(String checkType, String fileType)
	{
		if (checkType.equals("image"))
		{
			// ����ļ������Ƿ���
			if (this.getText("file.type.image").indexOf(fileType) == -1)
			{
				String tip = getText("file.type.image.error");
				setErrorTip(tip);
				return false;
			}
		}
		else
		{
			// ����ļ������Ƿ���
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
		if (fileSize >= 200 * 1024)
		{
			String tip = getText("file.size.error");
			setErrorTip(tip);
			return false;
		}
		return true;
	}

	/**
	 * �����Ŵ�С�Ƿ񳬳�
	 * 
	 * @param checkType
	 *          
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
			if (mmsSize + (fileSize - oldSize) >= 300 * 1024)
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

	public String getRealPath()
	{
		return ServletActionContext.getServletContext().getRealPath("");
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

	public MmsFile getQueryBean()
	{
		return queryBean;
	}

	public void setQueryBean(MmsFile queryBean)
	{
		this.queryBean = queryBean;
	}

	public String getMType()
	{
		return mType;
	}

	public void setMType(String type)
	{
		mType = type;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

}
