package com.frank.ylear.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class makeSmil_temp 
{	
	//smil文件存放路径
	private String filepath="";
	//smil文件名称
	private String filename="";
	//smil文件内容
	private String filecontent="";
	//记录文本文件数量（写入彩信中文件条数）
	private int filetxt=0;
	
	//设置文件存放路径
	public void setSmilPath(String path)
	{
		this.filepath=path;
		//如果目录不存在，则创建
		File rootDir=new File(this.filepath);
		if(!rootDir.isDirectory())
		{
			rootDir.mkdirs();
		}
	}
	public String getSmilPath()
	{
		return this.filepath;
	}
	
	//设置smil文件名称
	public void setSmilName(String name)
	{
		this.filename=name;
	}
	public String getSmilName()
	{
		return this.filename;
	}
	
	//重置smil文件
	public void  resetSmil()
	{
		this.filecontent="";
	}
	//为smil文件加入头部信息
	public void  smilAddHead()
	{
		this.filecontent= "<smil xmlns=\"http://www.w3.org/2001/SMIL20/CR/Language\">";
		this.filecontent+="\n  <head>";
		this.filecontent+="\n    <layout>";
		this.filecontent+="\n      <root-layout height=\"100%\" width=\"100%\"/>";
		this.filecontent+="\n      <region id=\"Text\" height=\"100%\" width=\"100%\" left=\"0\" top=\"0\" fit=\"scroll\"/>";
		this.filecontent+="\n      <region id=\"Image\" height=\"100%\" width=\"100%\"  left=\"0\" top=\"0\"/>";
		this.filecontent+="\n    </layout>";
		this.filecontent+="\n  </head>";
		this.filecontent+="\n  <body>";
	}	
	//设置彩信开始标记及彩信播放时间
	public void setSmilParStart(int time)
	{
		this.filecontent+="\n    <par dur=\""+time+"s\">";
	}
	//设置彩信结尾标记
	public void setSmilParEnd()
	{
		this.filecontent+="\n    </par>";
	}
	//设置彩信片断开始标记
	public void setSmilSeqStart()
	{
		this.filecontent+="\n      <seq>";
	}
	//设置彩信片断结束标记
	public void setSmilSeqEnd()
	{
		this.filecontent+="\n      </seq>";
	}
	//设置彩信声音
	public void smilAddAudio(String audio)
	{
		this.filecontent+="\n    <audio src=\""+audio+"\"/>";
	}
	//给文件添加文字内容
	public void smilAddTxt(String str,int time)
	{
		filetxt++;
		FileWriter fw;
		try 
		{
			fw = new FileWriter(this.filepath+this.filename+filetxt+".txt");
			fw.write(str,0,str.length());
			fw.flush();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.filecontent+="\n        <text src=\""+this.filename+filetxt+".txt\" region=\"Text\"  dur=\""+time+"s\"/>";
	}
	//给文件添加图片
	public void smilAddImg(String img,int time)
	{
		this.filecontent+="\n        <img src=\""+img+"\" region=\"Image\"  dur=\""+time+"s\"/>";
	}
	//给文件添加尾部信息
	public void smilAddFoot()
	{
		this.filecontent+="\n  </body>";
		this.filecontent+="\n</smil>";
	}
	
	//生成彩信smil文件
	public void generateSmil()
	{
		FileWriter fw;
		try 
		{
			fw = new FileWriter(this.filepath+this.filename+".smil");
			fw.write(this.filecontent,0,this.filecontent.length());
			fw.flush();
			fw.close();
		} 
		catch (IOException e)
		{			
			e.printStackTrace();
		}
	}
	
	//读取生成文件内容
	public String getFileContent()
	{
		String strContent="";
		File f=new File(this.getSmilPath()+this.getSmilName()+".smil");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			String line;
			while((line=reader.readLine())!=null)
			{
			  strContent+=line;
			}
		} catch (IOException e) {
			return e.toString();
		}

		return strContent;
	}
	/*
	public static void main(String[] args) 
	{	
		String path="D:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/mms/mms/";
		String name="smil1";
		//创建对象
		makeSmil sml=new makeSmil();
		//设置文件存放路径
		sml.setSmilPath(path);
		//设置文件名
		sml.setSmilName(name);
		//设置文件内容
		sml.resetSmil();//重置文件内容
		sml.smilAddHead();//并添加文件头部信息。
		sml.setSmilParStart(60);//彩信开始标记
		sml.smilAddAudio("a.wav");//彩信声音文件
		sml.setSmilSeqStart();//彩信片断开始标记
		sml.smilAddTxt("彩信生成程序测试1！",5);//加入文字
		sml.smilAddImg("smil1.jpg",5);//加入图片
		sml.smilAddTxt("彩信生成程序测试2！",5);//加入文字
		sml.setSmilSeqEnd();//彩信片断结束标记
		sml.setSmilParEnd();//彩信结束标记
		sml.smilAddFoot();//加入文件尾部信息
		
		//生成彩信文件
		sml.generateSmil();
		
		//读取生成文件
		
		String str=sml.getFileContent();
		
		System.out.println(str);
	}*/
}
