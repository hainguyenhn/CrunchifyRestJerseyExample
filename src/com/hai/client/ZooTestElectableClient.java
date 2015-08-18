package com.hai.client;
/**
 * ZooTestElectableClient
 *
 */
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.KeeperException;
import com.hai.zk.ZooElectableClient;
import com.hai.client.leader;
import com.hai.client.worker;

// See http://zookeeper.apache.org/doc/trunk/recipes.html#sc_leaderElection
public class ZooTestElectableClient extends ZooElectableClient {

	String worker_server_path = "";
	String leader_server_path = "";
	boolean isFirstRun = true;
	boolean wasLeader = false;
	leader_worker worker; 
	Thread t;

	public ZooTestElectableClient(String name) throws KeeperException, IOException, InterruptedException {
		super(name);
		this.name = name;
	}

	// Override this function to determine what work should be done
	public void performRole() {
		/*
		if ( isFirstRun || ( wasLeader != getCachedIsLeader() ) ) {
			System.out.println( "ZooTestElectableClient::performRole:: work performed (" + getElectionGUIDZNodePath() + ") with state (leader=" + getCachedIsLeader() 
					+ ", isFirstRun=" + isFirstRun + ", wasLeader=" + wasLeader + ")" );			
		}
		else {
			System.out.println( "ZooTestElectableClient::performRole:: work  was not performed (" + getElectionGUIDZNodePath() + ")" );
		}
		 */
		boolean leader;
		leader = getCachedIsLeader();
		Server sv = null;

		if(leader){
			// launch leader listening server

			//if there is a request, send to worker
			
			
			
			//print result?
			System.out.println("leader");
			List<String> ip = get_woker_ip();

		}
		else{
			///launch worker server and listening for jobs

			worker _worker = new worker(worker_server_path);
			_worker.run();
			List<String> ip = get_woker_ip();
		}	
	}

	public static String get_name(){
		Random ran = new Random();
		int top = 3;
		char data = ' ';
		String dat = "";

		for (int i=0; i<=top; i++) {
			data = (char)(ran.nextInt(25)+97);
			dat = data + dat;
		}

		return dat;
	}

	// Main entry point
	public static void main(String args[])
			throws KeeperException, IOException, InterruptedException {

		String name = get_name();
		ZooElectableClient zooClient = new ZooTestElectableClient(name);
		zooClient.run();
		System.out.println( "ZooTestElectableClient::main:: client finished." );

	}
}