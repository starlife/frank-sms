package com.frank.ylear.modules.mms.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;
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
	 * 把所有附件和文字都组装成一个MmsFile对象
	 * 
	 * @param mmsFile
	 * @param mmsName
	 * @param absolutePath
	 * @throws IOException
	 * @throws SQLException
	 */
	public static Long saveMmsFile(MmsFileService service, MmsFile mmsFile,String realPath)
	{
		// boolean flag = true;
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
			// 循环遍历每一帧
			while (it.hasNext())
			{
				Integer key = it.next();
				fr = map.get(key);
				if (StringUtils.isNotEmpty(fr.getImage()))
				{
					uploadFile = new UploadFile();
					File file = MMSFileHelper.getTempFile(realPath,fr.getImageFileName());
					FileInputStream input = new FileInputStream(file);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// 设置文件内容
					uploadFile.setFilename(file.getName());// 设置文件名
					uploadFile.setFilesize(blob.length());// 设置文件大小
					uploadFile.setFiletype(fr.getImageFileType());// 设置文件类型
					uploadFile.setFrameid(key);// 设置帧号
					uploadFile.setUploadtime(DateUtils.getTimestamp14());
					mmsFile.getUploadFiles().add(uploadFile);

				}
				if (StringUtils.isNotEmpty(fr.getAudio()))
				{
					uploadFile = new UploadFile();
					File file = MMSFileHelper.getTempFile(realPath,fr.getAudioFileName());
					FileInputStream input = new FileInputStream(file);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// 设置文件内容
					uploadFile.setFilename(file.getName());// 设置文件名
					uploadFile.setFilesize(blob.length());// 设置文件大小
					uploadFile.setFiletype(fr.getAudioFileType());// 设置文件类型
					uploadFile.setFrameid(key);// 设置帧号
					uploadFile.setUploadtime(DateUtils.getTimestamp14());
					mmsFile.getUploadFiles().add(uploadFile);

				}
				if (StringUtils.isNotEmpty(fr.getText()))
				{
					uploadFile = new UploadFile();
					byte[] data = fr.getText().getBytes(Constants.CHARSET);
					ByteArrayInputStream input = new ByteArrayInputStream(data);
					files.add(input);
					Blob blob = Hibernate.createBlob(input);
					uploadFile.setFiledata(blob);// 设置文件内容
					uploadFile.setFilesize(blob.length());// 设置文件大小
					uploadFile.setFilename(key+".txt");// 设置文件名
					uploadFile.setFrameid(key);// 设置帧号
					uploadFile.setUploadtime(DateUtils.getTimestamp14());
					uploadFile.setFiletype(fr.getTextFileType());
					mmsFile.getUploadFiles().add(uploadFile);

				}
			}
			// end 下面是把文件的信息写到uploadFile对象中

			String smil = MMSFileHelper.createSmil(mmsFile);// 创建smil
			long smilSize = smil.getBytes(Constants.CHARSET).length;
			frames = mmsFile.getFrameMap().size();
			long mmsSize = smilSize + mmsFile.getMmsSize();// 彩信总大小
			mmsFile.setFrames(frames);
			mmsFile.setSmildata(smil);
			mmsFile.setSmilname(Constants.SMIL_NAME);
			mmsFile.setSmilsize(smilSize);
			mmsFile.setMmsSize(mmsSize);
			mmsFile.setCreatetime(DateUtils.getTimestamp14());
			//这里保存
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
			// 关闭文件指针
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
	
	public static boolean makeMMSFrom3G(MmsFile mmsFile,String realPath,String url)
	{
		// 从给定的url中读取彩信内容
		Document doc=null;
		String mmsName=null;
		try
		{
			DOMBuilder domb = new DOMBuilder();
			doc = domb.build(new URL(url));
			Element root = doc.getRootElement();
			Element MobileHeaderInfo=root.getChild("MobileHeaderInfo");
			Element PapTitle=MobileHeaderInfo.getChild("PapTitle");
			mmsName=PapTitle.getTextTrim();
			if(mmsName.equals(""))
			{
				log.info("读取手机报时缺少彩信标题");
				return false;
			}
			List<Element> list=root.getChildren("MobileNewsInfo");
			if(list==null||list.size()==0)
			{
				log.info("读取手机报时缺少彩信内容");
				return false;
			}
			for(int i=0;i<list.size();i++)
			{
				MmsFrame fr = new MmsFrame();
				Element MobileNewsInfo=list.get(i);
				Element ImgFile=MobileNewsInfo.getChild("ImgFile");
				Element Content=MobileNewsInfo.getChild("Content");
				if(ImgFile!=null)
				{
					String imageFile=ImgFile.getTextTrim();
					//这里做处理图片
					String newFileName=newFileName(realPath,"img_",imageFile);
					File img = getTempFile(realPath,newFileName);
					FileUtil.saveData(new URL(imageFile).openStream(),img);
					//图片缩放,以宽260px等比例缩放
					ImageHelper.resize(img,260);
					//设置到帧
					fr.setImage(Constants.UPLOAD_FILE_DIR + File.separator
							+ img.getName());
					fr.setImageFileName(img.getName());
					fr.setImageFileSize(img.length());
					fr.setImageFileType(FileTypeHelper.WJPEG);
				}
				if(Content!=null)
				{				
					String text=Content.getTextTrim();									
					//这里做处理文字
					text=parse(text);	
					fr.setText(text);
					fr.setTextFileName(null);
					fr.setTextFileSize(text.getBytes(Constants.CHARSET).length);
					fr.setTextFileType(FileTypeHelper.TEXT);
					mmsFile.getFrameMap().put(i+1,fr);//添加帧
				}
				reSizeFrame(fr);//计算帧大小
			}
			reSizeMms(mmsFile);//计算彩信大小
			changeCurrentFrame(mmsFile,1);//设置当前帧
			mmsFile.setMmsName(mmsName);
			
			
		}
		catch (Exception ex)
		{
			log.error("解析失败或没有该条数据",ex);
			return false;
			
		}
		finally
		{
			;
		}
		return true;
	}

	/**
	 * 把数据库中查出来的数据组成一个彩信
	 * 
	 * @param mmsFile
	 * @param absolutePath
	 * @return
	 */
	public static boolean makeMMS(MmsFile mmsFile, String realPath)
	{
		// 创建需要的所有帧
		for (int i = 0; i < mmsFile.getFrames(); i++)
		{
			MmsFrame fr = new MmsFrame();
			mmsFile.getFrameMap().put(i + 1, fr);
		}

		// 根据所有upload文件创建temp预览文件并设置MmsFrame中的字段
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
							Constants.CHARSET));
					fr.setTextFileName(upload.getFilename());
					fr.setTextFileSize(upload.getFilesize());
					fr.setTextFileType(upload.getFiletype());
				}
				else if (Constants.FILE_TYPE_IMAGE.equals(fileType))
				{
					Blob blob = upload.getFiledata();
					String filename = upload.getFilename();
					File file =getTempFile(realPath, filename);
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
					File file =getTempFile(realPath, filename);
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

		// 通过smil的内容辅助得到各个帧的duringTime
		SmilParser parse = new SmilParser(mmsFile.getSmildata());
		parse.parse();
		for (int i = 0; i < parse.getFramecount(); i++)
		{
			SmilParser.Frame f = parse.getFrames().get(i);
			MmsFrame fr = mmsFile.getFrameMap().get(f.framenumber);
			fr.setDuringTime(f.dur);
		}
		// 重新计算各帧大小
		Iterator<Integer> keys = mmsFile.getFrameMap().keySet().iterator();
		while (keys.hasNext())
		{
			MmsFrame fr = mmsFile.getFrameMap().get(keys.next());
			MMSFileHelper.reSizeFrame(fr);
		}
		MMSFileHelper.reSizeMms(mmsFile);
		return true;
	}

	/**
	 * 插入一帧，帧号为frameid
	 */
	public static void addFrame(MmsFile mmsFile)
	{
		// 1.得到插入的帧号，如果当前没有帧，那么帧号为1，否则帧号为当前帧加1
		Integer frameid;
		if (mmsFile.getCurrentFrameId() == null)
		{
			frameid = 1;
			// 如果彩信大小为空，那么置为0
			/*
			 * if (mmsFile.getMmsSize() == null) { mmsFile.setMmsSize(0L); }
			 */
		}
		else
		{
			frameid = mmsFile.getCurrentFrameId() + 1;
		}

		// 2.插入帧，需要有排序
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

		// 3. 当前帧被改变了
		MMSFileHelper.changeCurrentFrame(mmsFile, frameid);
	}

	public static void delFrame(MmsFile mmsFile, Integer frameid)
	{
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
		MmsFrame mf = mmsFile.getFrameMap().get(frameid);
		if (mf == null)
		{
			currentFrameNumber = frameid - 1;
			mf = mmsFile.getFrameMap().get(currentFrameNumber);
		}
		if (mf == null)
		{
			// 当前彩信为空
			currentFrameNumber = null;
			MMSFileHelper.clearCurrentFrame(mmsFile);
		}
		else
		{
			// 设置当前帧
			MMSFileHelper.changeCurrentFrame(mmsFile, currentFrameNumber);
		}
		MMSFileHelper.reSizeMms(mmsFile);

	}

	public static void uploadImage(MmsFile mmsFile,String realPath, File image,
			String fileName, String fileType)
	{
		MmsFrame fr = mmsFile.getFrameMap().get(mmsFile.getCurrentFrameId());
		
		//重新生成文件名
		String newFileName=newFileName(realPath,"img_",fileName);
		// 拷贝文件到上传目录
		File dest = getTempFile(realPath,newFileName);
		Tools.copyFile(image, dest);
		// 设置image
		long fileSize = image.length();

		fr.setImage(Constants.UPLOAD_FILE_DIR + File.separator + newFileName);
		fr.setImageFileName(newFileName);
		fr.setImageFileSize(fileSize);
		fr.setImageFileType(fileType);
		// 重新计算帧大小和彩信大小
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void uploadAudio(MmsFile mmsFile,String realPath, File audio,
			String fileName, String fileType)
	{
		MmsFrame fr = mmsFile.getFrameMap().get(mmsFile.getCurrentFrameId());
		
		//重新生成文件名
		String newFileName=newFileName(realPath,"aud_",fileName);
		// 拷贝文件到上传目录
		File dest = getTempFile(realPath,newFileName);
		Tools.copyFile(audio, dest);
		// 设置image
		long fileSize = audio.length();

		fr.setAudio(Constants.UPLOAD_FILE_DIR + File.separator + newFileName);
		fr.setAudioFileName(newFileName);
		fr.setAudioFileSize(fileSize);
		fr.setAudioFileType(fileType);
		// 重新计算帧大小和彩信大小
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void uploadText(MmsFile mmsFile, String text)
	{
		MmsFrame fr = mmsFile.getFrameMap().get(mmsFile.getCurrentFrameId());
		// 设置image
		long fileSize = text.getBytes(Charset.forName(Constants.CHARSET)).length;
		//String fileName = mmsFile.getCurrentFrameId() + ".txt";
		fr.setText(text);
		fr.setTextFileName(null);
		fr.setTextFileSize(fileSize);
		// fr.setTextFileType(fileType);
		// 重新计算帧大小和彩信大小
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void clearImage(MmsFile mmsFile)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 清空image
		fr.setImage(null);
		fr.setImageFileName(null);
		fr.setImageFileSize(0);
		fr.setImageFileType(null);
		// 重新计算帧大小和彩信大小
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void clearAudio(MmsFile mmsFile)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 清空audio
		fr.setAudio(null);
		fr.setAudioFileName(null);
		fr.setAudioFileSize(0);
		fr.setAudioFileType(null);
		// 重新计算帧大小和彩信大小
		MMSFileHelper.reSizeFrameAndMms(fr, mmsFile);
	}

	public static void clearText(MmsFile mmsFile)
	{
		Integer frameid = mmsFile.getCurrentFrameId();
		MmsFrame fr = mmsFile.getFrameMap().get(frameid);
		// 清空text
		fr.setText(null);
		fr.setTextFileName(null);
		fr.setTextFileSize(0);
		// 重新计算帧大小和彩信大小
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

		smil.smilAddHead();// 并添加文件头部信息。
		Map<Integer, MmsFrame> map = mmsFile.getFrameMap();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext())
		{
			Integer key=it.next();
			MmsFrame fr = map.get(key);
			smil.setSmilParStart(fr.getDuringTime());// 彩信开始标记
			if (Tools.isNotEmpty(fr.getImage()))
			{
				smil.smilAddImg(fr.getImageFileName());
			}
			if (Tools.isNotEmpty(fr.getText()))
			{
				smil.smilAddText(key+".txt");
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
	
	/**
	 * 重新生成文件名，保证文件名的唯一性
	 * @param fileName
	 * @return
	 */
	public static String newFileName(String realPath,String prefix,String fileName)
	{
		while(true)
		{
			String newFileName=Tools.getRandomFileName(prefix,fileName);
			File newFile=getTempFile(realPath,newFileName);
			if(!newFile.exists())
			{
				return newFileName;
			}
		}
	}
	
	
	/**
	 * 取得tmp文件的文件对象
	 * @param tmpFileName
	 * @return
	 */
	public static File getTempFile(String realPath,String tmpFileName)
	{
		String path=realPath+File.separator+Constants.UPLOAD_FILE_DIR;
		File dir = new File(path);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		File dest = new File(path + File.separator + tmpFileName);
		return dest;
	}
	
	public static String parse(String paper)
	{
		paper=paper.replaceAll("<br/>",Constants.NEWLINE);
		paper=paper.replaceAll("&nbsp;"," ");
		return paper;
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
		/*MmsFile mmsFile = service.getMmsFile(2L);
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
		uploadFile.setFiledata(blob);// 设置文件内容
		uploadFile.setFilename(file.getName());// 设置文件名
		uploadFile.setFilesize(blob.length());// 设置文件大小
		uploadFile.setFiletype(fr.getImageFileType());// 设置文件类型
		uploadFile.setFrameid(1);// 设置帧号
		uploadFile.setUploadtime(DateUtils.getTimestamp14());
		dao.getHibernateTemplate().save(uploadFile);*/
		MmsFile mmsFile=new MmsFile();
		//String url="http://interface.tourzj.gov.cn/MobilePaper/default.aspx?Mtype=0&Title=2";
		String urlFormat="http://interface.tourzj.gov.cn/MobilePaper/default.aspx?Mtype=%s&Title=%s";
		String MType="1";
		String Title="洞头旅游";
		String url=String.format(urlFormat,java.net.URLEncoder.encode(MType,"UTF-8"),java.net.URLEncoder.encode(Title,"UTF-8"));
		System.out.println(url);
		String realPath="D:\\Tomcat 6.0\\webapps\\ylear2";
		boolean b=makeMMSFrom3G(mmsFile,realPath,url);
		String mmsName=mmsFile.getMmsName();
		if(!b)
		{
			System.out.println("解析失败");
		}
		//saveMmsFile(service,mmsFile,realPath);
		MmsFile temp=service.getMmsFile(mmsName);
		if(temp!=null)
		{
			System.out.println(temp.getId());
			System.out.println(temp.getMmsName());
		}
		

	}
}
