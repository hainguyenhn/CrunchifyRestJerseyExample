import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.hai.vm;

public class leader_worker extends Thread
{
	private ServerSocket serverSocket;
	Socket server;
	private Socket client;
	boolean leader = false;
	String name;
	String server_path = "";
	final int port = 9090;
	ArrayList<String> workers;

	public leader_worker(boolean leader, String name, String path) 
	{
		this.name = name;
		this.server_path = path;
		this.leader = leader;
		this.workers = new ArrayList<String>();
		System.out.print(this.leader);
		if (!this.leader){
			try {
				serverSocket = new ServerSocket(this.port);
				serverSocket = new ServerSocket(9090, 0, InetAddress.getByName("localhost"));
				serverSocket.setSoTimeout(99999);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else{
			try
			{
				System.out.println("Connecting to " + "server"
						+ " on port " + this.port);
				this.client = new Socket("localhost", this.port);

			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public void run(){
		if(this.leader){
			leader_run(this.name);
		}
		else{
			worker_run(this.server_path);
		}
	}

	public void worker_run(String path)
	{
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p1 = runtime.exec("cmd /c start " + path + "//catalina.bat run ", null, new File(path));
			InputStream is = p1.getInputStream();
			int i = 0;
			while( (i = is.read() ) != -1) {
				System.out.print((char)i);
			}
		} catch(IOException ioException) {
			System.out.println(ioException.getMessage() );
		}

	}



	public void leader_run(String name){
		ArrayList<vm> list_worker = new ArrayList<vm>();
	}

	public boolean got_promoted(boolean promoted){
		/*
		 * Switch role
		 */
		boolean success = false;
		if (promoted && !this.leader){
			try {
				server.close();
				this.interrupt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				success = false;
			}	
		}
		else{
			success = false;
		}
		return success;
	}

	public void update_workers(ArrayList<String> worker_list){
		/*
		 * This method updates list of workers  
		 */
		this.workers = worker_list;
	}

	/* May not need to use for now
	class worker{
		String name;
		String ip;
		String port;

		public worker(String name, String ip, String port){
			this.name = name;
			this.ip = ip;
			this.port = port;
		}

		public String getname(){
			return this.name;
		}

		public String getip(){
			return this.name;
		}

		public String getport(){
			return this.name;
		}
	}
	 */

	public static void main(String[] args){
		leader_worker worker = new leader_worker(false, "hai");
		leader_worker leader = new leader_worker(true, "hai");
		Thread t1 = new Thread(worker);
		Thread t2 = new Thread(leader);
		t1.start();
		t2.start();


	}
}