
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
public class Test
{
	
	public static void createTable()
	{
		//Configuration cfg=new Configuration().configure();
		//SchemaExport se=new SchemaExport(cfg);
		
	}
	public static void main(String[] args) throws IOException
	{
		/*UPhoneaddress obj=new UPhoneaddress("13777802386","abc","dddd",
				"aaa");
		MyDaoImpl dao = MyDaoImpl.getInstance();
		dao.save(obj);*/
		//createTable();
		FileInputStream input=new FileInputStream("D:/test/1.txt");
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		FileOutputStream output=new FileOutputStream("D:/test/11.txt");
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(output,"GBK"));
		String str=null;
		while((str=reader.readLine())!=null)
		{
			System.out.println(str);
		}
		input.close();
		output.close();
			
		
		
	}
	
}
