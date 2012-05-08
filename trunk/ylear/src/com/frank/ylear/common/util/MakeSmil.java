package com.frank.ylear.common.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MakeSmil
{
	//smil�ļ����·��
	private String filepath="";
	//smil�ļ�����
	private String filename="";
	//smil�ļ�����
	private String filecontent="";
	
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
	
	//����smil�ļ�����
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
	
	//Ϊsmil�ļ�����ͷ����Ϣ
	public void  smilAddHead()
	{
		this.filecontent= "<smil xmlns=\"http://www.w3.org/2001/SMIL20/CR/Language\">";
		this.filecontent+="\n  <head>";
		this.filecontent+="\n    <layout>";
		this.filecontent+="\n      <root-layout width=\"100%\" height=\"100%\"/>";
		//this.filecontent+="\n    <root-layout height=\"100%\" width=\"100%\"/>";
		//this.filecontent+="\n    <region id=\"Text\" height=\"100%\" width=\"100%\" left=\"0\" top=\"0\" fit=\"scroll\"/>";
		//this.filecontent+="\n    <region id=\"Image\" height=\"100%\" width=\"100%\"  left=\"0\" top=\"0\"/>";
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
	//���ò���Ƭ�Ͻ������
	public void setSmilSeqEnd()
	{
		this.filecontent+="\n      </seq>";
	}
	//���ò�������
	public void smilAddAudio(String audio)
	{
		this.filecontent+="\n      <audio src=\""+audio+"\"/>";
	}
	//���ļ������������
	public void smilAddTxt(String txt)
	{
		this.filecontent+="\n      <text src=\""+txt+"\"/>";
	}
//	//���ļ����ͼƬ
	public void smilAddImg(String img)
	{
		this.filecontent+="\n      <img src=\""+img+"\"/>";
	}
	//���ļ����β����Ϣ
	public void smilAddFoot()
	{
		this.filecontent+="\n  </body>";
		this.filecontent+="\n</smil>";
	}
	
	//���ɲ���smil�ļ�
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
	
	//��ȡ�����ļ�����
	public String getFileContent()
	{
		return this.filecontent;
	}
	
	public static void main(String[] args) 
	{	
		String path="D:/";
		String name="test1";
		//��������
		MakeSmil sml=new MakeSmil();
		//�����ļ����·��
		sml.setSmilPath(path);
		//�����ļ���
		sml.setSmilName(name);
		//�����ļ�����
		sml.resetSmil();//�����ļ�����
		sml.smilAddHead();//������ļ�ͷ����Ϣ��
		sml.setSmilParStart(60);//���ſ�ʼ���
		sml.smilAddImg("1.gif");
		sml.smilAddTxt("1.txt");
		sml.smilAddAudio("1.wav");//���������ļ�
		sml.setSmilParEnd();//���Ž������
		
		sml.setSmilParStart(30);//���ſ�ʼ���
		sml.smilAddImg("2.gif");
		sml.smilAddTxt("2.txt");
		sml.smilAddAudio("2.wav");//���������ļ�
		sml.setSmilParEnd();//���Ž������
		
		sml.smilAddFoot();//�����ļ�β����Ϣ
		
		//���ɲ����ļ�
		sml.generateSmil();
		
		//��ȡ�����ļ�
		
		String str=sml.getFileContent();
		
		System.out.println(str);
	}

}
