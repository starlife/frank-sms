package test;

import java.util.HashMap;
import java.util.Map;

public class Test
{
	
	public static final  Map map=new HashMap();
	//private static final Log log=LogFactory.getLog(PReceiver.class);//记录日志
	
	public Test()
	{
		
	}
	
	public void test()
	{
		try
		{
			Integer.parseInt("ee");
		}catch(Exception ex)
		{
			//log.error(null,ex);
			//ex.printStackTrace();
		}
	}
	private static String [] splitMsg(String msg){
	       int bindex=0;
	       int eindex=70;
	       int index=(int)java.lang.Math.ceil(msg.length()/(70.0));
	       String [] temp=new String[index];
	       if(msg.length()<70)
	           eindex=msg.length();
	       for(int i=0;i<index;i++){
	           temp[i]=msg.substring(bindex, eindex);
	           bindex=eindex;
	           eindex=eindex+70>msg.length()?msg.length():eindex+70;
	       }
	       return temp;
	    }
	
	public static void main(String[] args) throws InterruptedException
	{
		map.put(1,"str1");
		while(true)
		{
			Thread.sleep(1000);
			System.out.println(map);
			
		}
	}
	
}
