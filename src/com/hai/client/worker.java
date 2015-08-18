package com.hai.client;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class worker extends Thread
{
	boolean leader = false;
	String worker_server_path = "";
	ArrayList<String> workers;

	public worker(String path) 
	{
		this.worker_server_path = path;
		this.workers = new ArrayList<String>();
	}

	public void run(){
		worker_run();
	}

	public void worker_run()
	{
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p1 = runtime.exec("cmd /c start " + worker_server_path + "//catalina.bat run ", null, new File(worker_server_path));
			InputStream is = p1.getInputStream();
			int i = 0;
			while( (i = is.read() ) != -1) {
				System.out.print((char)i);
			}
		} catch(IOException ioException) {
			System.out.println(ioException.getMessage() );
		}
	}
}