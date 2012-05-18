package com.frank.ylear.modules.mms.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Smil
{
	private final Log log = LogFactory.getLog(Smil.class);
	// smil文件存放路径
	private String filepath = "";
	// smil文件名称
	private String filename = "";
	// smil文件内容
	private String content = "";

	// 设置文件存放路径
	public void setSmilPath(String path)
	{
		this.filepath = path;
		// 如果目录不存在，则创建
		File rootDir = new File(this.filepath);
		if (!rootDir.isDirectory())
		{
			rootDir.mkdirs();
		}
	}

	public String getSmilPath()
	{
		return this.filepath;
	}

	// 设置smil文件名称
	public void setSmilName(String name)
	{
		this.filename = name;
	}

	public String getSmilName()
	{
		return this.filename;
	}

	// 重置smil文件
	public void resetSmil()
	{
		this.content = "";
	}

	// 为smil文件加入头部信息
	public void smilAddHead()
	{
		this.content = "<smil xmlns=\"http://www.w3.org/2001/SMIL20/CR/Language\">";
		this.content += "\n  <head>";
		this.content += "\n    <layout>";
		this.content += "\n      <root-layout width=\"320px\" height=\"480px\"/>";
		// this.filecontent+="\n <root-layout height=\"100%\" width=\"100%\"/>";
		// this.filecontent+="\n <region id=\"Text\" height=\"100%\"
		// width=\"100%\" left=\"0\" top=\"0\" fit=\"scroll\"/>";
		// this.filecontent+="\n <region id=\"Image\" height=\"100%\"
		// width=\"100%\" left=\"0\" top=\"0\"/>";
		this.content += "\n    </layout>";
		this.content += "\n  </head>";
		this.content += "\n  <body>";
	}

	// 设置彩信开始标记及彩信播放时间
	public void setSmilParStart(int time)
	{
		this.content += "\n    <par dur=\"" + time + "s\">";
	}

	// 设置彩信结尾标记
	public void setSmilParEnd()
	{
		this.content += "\n    </par>";
	}

	// 设置彩信片断开始标记
	public void setSmilSeqStart()
	{
		this.content += "\n      <seq>";
	}

	// 设置彩信片断结束标记
	public void setSmilSeqEnd()
	{
		this.content += "\n      </seq>";
	}

	// 设置彩信声音
	public void smilAddAudio(String audio)
	{
		this.content += "\n      <audio src=\"" + audio + "\"/>";
	}

	// 给文件添加文字内容
	public void smilAddText(String text)
	{
		this.content += "\n      <text src=\"" + text + "\"/>";
	}

	// //给文件添加图片
	public void smilAddImg(String img)
	{
		this.content += "\n      <img src=\"" + img + "\"/>";
	}

	// 给文件添加尾部信息
	public void smilAddFoot()
	{
		this.content += "\n  </body>";
		this.content += "\n</smil>";
	}

	// 生成彩信smil文件
	public void generateSmil()
	{
		FileWriter fw;
		try
		{
			fw = new FileWriter(this.filepath + this.filename + ".smil");
			fw.write(this.content, 0, this.content.length());
			fw.flush();
			fw.close();
		}
		catch (IOException e)
		{
			log.error(null, e);
		}
	}

	// 读取生成文件内容
	public String getSmil()
	{
		return this.content;
	}

	public static void main(String[] args)
	{
		/*
		 * String path="D:/"; String name="test1"; //创建对象 Smil sml=new Smil();
		 * //设置文件存放路径 sml.setSmilPath(path); //设置文件名 sml.setSmilName(name);
		 * //设置文件内容 sml.resetSmil();//重置文件内容 sml.smilAddHead();//并添加文件头部信息。
		 * sml.setSmilParStart(60);//彩信开始标记 sml.smilAddImg("1.gif");
		 * sml.smilAddText("1.txt"); sml.smilAddAudio("1.wav");//彩信声音文件
		 * sml.setSmilParEnd();//彩信结束标记 sml.setSmilParStart(30);//彩信开始标记
		 * sml.smilAddImg("2.gif"); sml.smilAddText("2.txt");
		 * sml.smilAddAudio("2.wav");//彩信声音文件 sml.setSmilParEnd();//彩信结束标记
		 * sml.smilAddFoot();//加入文件尾部信息 //生成彩信文件 sml.generateSmil(); //读取生成文件
		 * String str=sml.getFileContent(); System.out.println(str);
		 */
	}

}
