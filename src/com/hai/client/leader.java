/**
 * @author Hai Nguyen
 * @based on crunchify
 */
package com.hai.client;
import com.hai.vm;
import com.hai.zk.ZooElectableClient;
import com.sun.jersey.api.client.Client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.apache.zookeeper.KeeperException;
import org.json.JSONObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class leader extends ZooElectableClient {
	ArrayList<vm> list_worker;

	public leader(String name) throws KeeperException, IOException, InterruptedException{
		super(name);

	}
	
	public void serve(String leader_server_path){
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p1 = runtime.exec("cmd /c start " + leader_server_path + "//catalina.bat run ", null, new File(leader_server_path));
			InputStream is = p1.getInputStream();
			int i = 0;
			while( (i = is.read() ) != -1) {
				System.out.print((char)i);
			}
		} catch(IOException ioException) {
			System.out.println(ioException.getMessage() );
		}
	}
	
	public int run(JSONObject work){
		this.divide_work(work);
		return this.post();
	}
	
	public void update_worker(){
		ArrayList<String> list_vm = (ArrayList<String>) this.get_woker_ip();
		for(int i = 0; i < list_vm.size(); i++){
			list_worker.add(new vm("", list_vm.get(0)));
		}
	}

	private int post() {
		/*
		 * This method sends work to workers
		 */
		
		int hash_value = -1;

		// call to update worker first 
		int num_worker = list_worker.size();
		//create a list of worker
		worker [] workers = new worker[num_worker];
		//create a list of future result
		ArrayList<FutureTask<Integer>> results = new ArrayList<FutureTask<Integer>>();
		ExecutorService executor = Executors.newFixedThreadPool(num_worker);	

		for(int i = 0; i < num_worker; i++){
			workers[i] = new worker(list_worker.get(i).getString("ip"), list_worker.get(i));
			results.add(new FutureTask<Integer>(workers[i]));
			executor.execute(results.get(i));
		}

		boolean found = false;
		while (!found) {
			try {
				for(int i = 0; i < num_worker; i++){
					if (results.get(i).isDone() && results.get(i).get() != -1){
						hash_value = results.get(i).get();
						System.out.println("Found " + results.get(i).get());
						System.out.println("worker who found the result is " + list_worker.get(i).getString("ip"));
						executor.shutdown();
						found = true;
						break;
					}
					if(results.get(i).isDone()){
						System.out.println("worker did not found the result: " + list_worker.get(i).getString("ip"));
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return hash_value;
	}

	private void divide_work(JSONObject work){
		/*
		 * This methods divide work evenly among available workers
		 * @in work: json object contains from, to, num_i, base_string
		 */
		int from = work.getInt("from");
		int to = work.getInt("to");
		int num_i = work.getInt("num_i");
		String base = work.getString("base_string");

		int available_workers = this.list_worker.size();
		int range = (int)(to - from)/available_workers;

		for(int i = 0; i < available_workers; i++){
			this.list_worker.get(i).put("from", (range * i) + i);
			this.list_worker.get(i).put("to", (range * i) + range + i);
			this.list_worker.get(i).put("num_i", num_i);
			this.list_worker.get(i).put("base_string", base);
		}	
	}

	class worker implements Callable<Integer> {
		/*
		 * Simple Callable class to send jobs to workers
		 */
		private String ip;
		private JSONObject j_obj;

		public worker(String ip, JSONObject j_obj) {
			this.ip = ip;
			this.j_obj = j_obj;
		}

		@Override
		public Integer call() throws Exception {
			int result = -1;
			try {
				Client client = Client.create();
				WebResource webResource = client.resource("http://" + ip + "/leader_worker/my_app/process/hash");
				System.out.println(j_obj.toString() + ip);
				ClientResponse response = webResource.type("application/json")
						.post(ClientResponse.class, j_obj.toString());

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatus());
				}

				JSONObject json_output = new JSONObject(response.getEntity(String.class));
				result = json_output.getInt("result");

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	public static void main(String[] args) {
		/*
		JSONObject work =new JSONObject();
		work.put("from", 0);
		work.put("to", 5);
		work.put("num_i", 10);
		work.put("base_string", "This 21is a test");

		ArrayList<vm> list_worker = new ArrayList<vm>();
		list_worker.add(new vm("vm1", "localhost:8080"));
		//list_worker.add(new vm("vm1", "localhost:8085"));
		leader _leader = new leader(list_worker);
		_leader.divide_work(work);
		_leader.post();
		 */
	}

	@Override
	public void performRole() {
		// TODO Auto-generated method stub
		
	}
}