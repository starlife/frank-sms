package test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.frank.ylear.common.model.PageBean;
import com.frank.ylear.modules.base.dao.impl.BaseDaoImpl;
import com.frank.ylear.modules.mms.entity.MmsFile;
import com.frank.ylear.modules.mms.entity.UMms;
import com.frank.ylear.modules.mms.entity.UploadFile;
import com.frank.ylear.modules.mms.service.impl.MmsFileServiceImpl;
import com.frank.ylear.modules.mms.service.impl.MmsServiceImpl;
import com.frank.ylear.modules.phoneaddress.entity.UPhoneaddress;
import com.frank.ylear.modules.user.entity.SysRight;
import com.frank.ylear.modules.user.entity.SysRole;
import com.frank.ylear.modules.user.entity.SysRoleRight;
import com.frank.ylear.modules.user.entity.SysUser;
import com.frank.ylear.modules.user.service.impl.UserServiceImpl;
public class Test
{

	public static void main(String[] args) throws IOException, SQLException
	{
		/*File f=new File("/abcd/def");
		System.out.println(f.getName());
		f=new File("/abcd/def/");
		System.out.println(f.getName());
		System.out.println(Constants.audioFileExt.contains("amr"));*/
		/*String path="E:\\myworkspace\\ylear2\\src\\"+"applicationContext.xml";
		ApplicationContext ctx=new FileSystemXmlApplicationContext(path);
		//BaseDaoImpl dao=(BaseDaoImpl)ctx.getBean("baseDao");
		
		//List l1=dao.getHibernateTemplate().find("select obj from UMms obj inner join fetch obj.mmsFile where 1=1");
		MmsFileServiceImpl service=(MmsFileServiceImpl) ctx.getBean("mmsFileService");
		MmsFile  mmsFile=service.getMms(49L);
		System.out.println(mmsFile);
		Set set=mmsFile.getUploadFiles();
		Iterator<UploadFile> it=set.iterator();
		while(it.hasNext())
		{
			UploadFile file=it.next();
			System.out.println(file.getFilename());
			System.out.println(file.getFiletype());
			System.out.println("================");
		}*/
		
		int[] aa={1,2};
		System.out.println(aa[3]);
		
	}
	
	public static void createAllUploadFile(MmsFile mmsFile) throws IOException, SQLException
	{
		String str="This is a test String!加上中文呢";
		byte[] data=str.getBytes();
		ByteArrayInputStream input=new ByteArrayInputStream(data);
		Blob blob=Hibernate.createBlob(input);
		long filesize=blob.length();
		String filename="my.txt";
		UploadFile f1=new UploadFile();
		f1.setFilename(filename);
		f1.setFiledata(blob);
		f1.setFilesize(filesize);
		f1.setFramenumber(1);
		//f1.setLocalname(filename);
		f1.setUploadtime(new Date());
		f1.setFiletype("text");
		mmsFile.getUploadFiles().add(f1);
		
	}
	
	
	
	
}
