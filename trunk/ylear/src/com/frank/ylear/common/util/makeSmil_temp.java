package com.frank.ylear.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class makeSmil_temp 
{	
	
	private String filepath="";
	
	private String filename="";
	
	private String filecontent="";
	//��¼�ı��ļ�����д��������ļ�����
	private int filetxt=0;
	
	//�����ļ����·��
	public void setSmilPath(String path)
	{
		this.filepath=path;
		//���Ŀ¼�����ڣ��򴴽�
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
	
	//����smil�ļ����
	public void setSmilName(String name)
	{
		this.filename=name;
	}
	public String getSmilName()
	{
		return this.filename;
	}
	
	//����smil�ļ�
	public void  resetSmil()
	{
		this.filecontent="";
	}

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
	//���ò��ſ�ʼ��Ǽ����Ų���ʱ��
	public void setSmilParStart(int time)
	{
		this.filecontent+="\n    <par dur=\""+time+"s\">";
	}
	//���ò��Ž�β���
	public void setSmilParEnd()
	{
		this.filecontent+="\n    </par>";
	}
	//���ò���Ƭ�Ͽ�ʼ���
	public void setSmilSeqStart()
	{
		this.filecontent+="\n      <seq>";
	}
	//���ò���Ƭ�Ͻ�����
	public void setSmilSeqEnd()
	{
		this.filecontent+="\n      </seq>";
	}
	//���ò�������
	public void smilAddAudio(String audio)
	{
		this.filecontent+="\n    <audio src=\""+audio+"\"/>";
	}
	//���ļ������������
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
	
	public void smilAddImg(String img,int time)
	{
		this.filecontent+="\n        <img src=\""+img+"\" region=\"Image\"  dur=\""+time+"s\"/>";
	}
	
	public void smilAddFoot()
	{
		this.filecontent+="\n  </body>";
		this.filecontent+="\n</smil>";
	}
	
	//��ɲ���smil�ļ�
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
	
	//��ȡ����ļ�����
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
		//��������
		makeSmil sml=new makeSmil();
		//�����ļ����·��
		sml.setSmilPath(path);
		//�����ļ���
		sml.setSmilName(name);
		//�����ļ�����
		sml.resetSmil();//�����ļ�����
		sml.smilAddHead();//������ļ�ͷ����Ϣ��
		sml.setSmilParStart(60);//���ſ�ʼ���
		sml.smilAddAudio("a.wav");//���������ļ�
		sml.setSmilSeqStart();//����Ƭ�Ͽ�ʼ���
		sml.smilAddTxt("������ɳ������1��",5);//��������
		sml.smilAddImg("smil1.jpg",5);//����ͼƬ
		sml.smilAddTxt("������ɳ������2��",5);//��������
		sml.setSmilSeqEnd();//����Ƭ�Ͻ�����
		sml.setSmilParEnd();//���Ž�����
		sml.smilAddFoot();//�����ļ�β����Ϣ
		
		//��ɲ����ļ�
		sml.generateSmil();
		
		//��ȡ����ļ�
		
		String str=sml.getFileContent();
		
		System.out.println(str);
	}*/
}
