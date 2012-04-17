package test;

public class Test1
{
	public Object lock=new Object();
	private int i=5;
	
	public void method1()
	{
		synchronized (lock)
		{
			
		
		synchronized (lock)
		{
			i=4;
		}
		}
	}
	
	public void method2()
	{
		synchronized (lock)
		{
			i=3;
			method1();
			System.out.println(i);
		}
	}
	public static void main(String[] args)
	{
		new Test1().method2();
	}
}
