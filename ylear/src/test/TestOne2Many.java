package test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.frank.ylear.common.util.FileUtil;
import com.frank.ylear.modules.base.dao.impl.BaseDaoImpl;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.entity.UploadFile;


public class TestOne2Many extends HibernateDaoSupport
{
	public static void main(String[] args) throws Exception
	{
		
		/*String path="E:\\myworkspace\\lyear\\src\\"+"applicationContext.xml";
		ApplicationContext ctx=new FileSystemXmlApplicationContext(path);
		BaseDaoImpl impl=(BaseDaoImpl)ctx.getBean("baseDao");
		UploadFile upload=(UploadFile) impl.get(UploadFile.class,22L);
		String text=FileUtil.getData(upload.getFiledata().getBinaryStream(),"gb2312");
		System.out.println(new String(text.getBytes("utf-8")));
		System.out.println("================================");
		text=FileUtil.getData(upload.getFiledata().getBinaryStream(),"gb2312");
		//System.out.println(new String(text.getBytes("utf-8")));
		System.out.println(text);*/
		operateBlob();
		
		
		//************************************************************
		/*String str="This is a test String!加上中文呢";
		byte[] data=str.getBytes();
		ByteArrayInputStream input=new ByteArrayInputStream(data);
		Blob blob=Hibernate.createBlob(input);
		
		long filesize=blob.length();
		String filename="my.txt";
		UploadFile f1=new UploadFile();
		f1.setFilename("1.gif");
		f1.setFiledata(blob);
		f1.setFilesize(filesize);
		f1.setFramenumber(1);
		f1.setLocalname(filename);
		f1.setUploadtime(new Date());
		Serializable serial=impl.add(f1);
		input.close();
		//**********************************************************
		UploadFile f2=(UploadFile) impl.get(UploadFile.class,serial);
		Blob bb=f2.getFiledata();
		InputStream input2=bb.getBinaryStream();
		File f=new File("D:/2.txt");
		FileOutputStream output2=new FileOutputStream(f);
		int len2=0;
		byte[] buf2=new byte[102400];
		System.out.println(bb.length());
		while((len2=input2.read(buf2))!=-1)
		{
			output2.write(buf2,0,len2);
		}
		output2.close();
		input2.close();
		
		//*****************************************************
		/*File f=new File("D:/1.gif");
		FileOutputStream output=new FileOutputStream(f);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output,"gb2312"));
		writer.write(data);
		writer.flush();
		writer.close();*/
		
		/*SysRole sysRole=(SysRole) impl.get(SysRole.class,1L);
		SysRight sysRight=(SysRight) impl.get(SysRight.class,1L);
		System.out.println(sysRole);
		SysRoleRight roleRight=new SysRoleRight();
		roleRight.setSysRole(sysRole);
		//roleRight.setSysRight(sysRight);
		System.out.println(roleRight);
		//sysRole.setSysRoleRights(sysRoleRights)
		//System.out.println(sysUser.getUsrRole());*/
		/*String str="a林";
		byte[] b=str.getBytes("utf-8");
		for(int i=0;i<b.length;i++)
		{
			System.out.println(b[i]);
		}
		str=new String(b,"iso-8859-1");
		System.out.println(new String(str.getBytes("iso-8859-1"),"utf-8"));*/	
		
		
	}
	
	public static void  operate()
	{
		Session session=HibernateSessionFactory.getSession();
		//业务逻辑
		Serializable id=save(session);
		try
		{
			System.out.println("sleep 60s");
			Thread.sleep(60000);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("sleep 60s end");
		del(session,id);
		
		session.close();
		HibernateSessionFactory.getSessionFactory().close();
		
	}
	
	/**
	 * 一对多关系插入：数据由一方维护（插入一方的数据，多方的数据跟着插入）
	 * 配置文件里one-to-many一定要配置2个参数 inverse="false" cascade="all"
	 * @param session
	 */
	public static Serializable save(Session session)
	{
		session.beginTransaction();
		Serializable id=0;
		session.beginTransaction();
		MmsFile mms=new MmsFile();
		mms.setMmsName("第一个彩信");
		mms.setFrames(3);
		mms.setContentSize(100);
		mms.setContentSmil("");
		mms.setCreatetime(new Date());
		//new UploadFile对象
		UploadFile f1=new UploadFile();
		f1.setFilename("1.txt");
		//f1.setFiledata("this is a test");
		f1.setFilesize(10L);
		f1.setFramenumber(1);
		f1.setFiletype("image");
		//f1.setLocalname("D:/1.txt");
		f1.setUploadtime(new Date());
		//关联2个对象
		mms.getUploadFiles().add(f1);
		id=session.save(mms);
		session.getTransaction().commit();
		return id;
		
	}
	
	public static void del(Session session,Serializable mmsid)
	{
		session.beginTransaction();
		MmsFile mms=(MmsFile)session.load(MmsFile.class, mmsid);
		session.delete(mms);
		session.getTransaction().commit();
	}
	
	private static void operateBlob() throws Exception
	{
		String path="E:\\myworkspace\\ylear2\\src\\"+"applicationContext.xml";
		ApplicationContext ctx=new FileSystemXmlApplicationContext(path);
		BaseDaoImpl impl=(BaseDaoImpl)ctx.getBean("baseDao");
		
		//************************************************************
		File file=new File("F:\\转发_ 林新正为您制作的彩信\\img_07.gif");
		if(!file.exists())
		{
			System.out.println("file not exist");
			return;
		}
		
		long filesize=file.length();
		String filename=file.getName();
		UploadFile f1=new UploadFile();
		f1.setFilename("1.gif");
		f1.setFiletype("image");
		//写blob数据
		FileInputStream input=new FileInputStream(file);
		Blob blob=Hibernate.createBlob(input);
		f1.setFiledata(blob);
		f1.setFilesize(filesize);
		f1.setFramenumber(1);
		//f1.setLocalname(filename);
		f1.setUploadtime(new Date());
		//Serializable serial=impl.add(f1);
		//System.out.println(serial);
		input.close();//注意：这条语句一定要在数据库插入语句之后，插入的时候才会读取文件的数据
		//**********************************************************
		UploadFile f2=(UploadFile) impl.get(UploadFile.class,34L);
		//读取blob数据
		Blob bb=f2.getFiledata();
		InputStream input2=bb.getBinaryStream();
		input2.close();
		input2=bb.getBinaryStream();
		File f=new File("D:/1.gif");
		FileOutputStream output2=new FileOutputStream(f);
		int len2=0;
		byte[] buf2=new byte[102400];
		System.out.println(bb.length());
		while((len2=input2.read(buf2))!=-1)
		{
			output2.write(buf2,0,len2);
		}
		
		output2.close();
		input2.close();
	}
}
