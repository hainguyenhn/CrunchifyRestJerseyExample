package com.crunchify.client;
import com.crunchify.vm;
import com.crunchify.restjersey.hash;
import com.sun.jersey.api.client.Client;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.json.JSONObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author Crunchify
 * 
 */

public class CrunchifyRESTJerseyClient {
	ArrayList<vm> list_worker;
	
	public CrunchifyRESTJerseyClient(ArrayList<vm> list_vm){
		list_worker = list_vm;
	}

	public static void main(String[] args) {
		JSONObject work =new JSONObject();
		work.put("from", 0);
		work.put("to", 2147483647);
		work.put("num_i", 10);
		work.put("base_string", "This 21is a test");
		
		ArrayList<vm> list_worker = new ArrayList<vm>();
		list_worker.add(new vm("vm1", "localhost:8080"));
		list_worker.add(new vm("vm1", "localhost:8085"));
		CrunchifyRESTJerseyClient crunchifyClient = new CrunchifyRESTJerseyClient(list_worker);
		crunchifyClient.divide_work(work);
		crunchifyClient.post();
	}
	

	private void post() {
		/*
		 * This methods divides the work and send to multiple workers
		 */
		
		// call to update worker first 
		int num_worker = list_worker.size();
		
		//create a list of worker
		leader [] workers = new leader[num_worker];
		//create a list of future result
		ArrayList<FutureTask<Integer>> results = new ArrayList<FutureTask<Integer>>();
		ExecutorService executor = Executors.newFixedThreadPool(num_worker);
		
		
		for(int i = 0; i < num_worker; i++){
			workers[i] = new leader(list_worker.get(i).getString("ip"), list_worker.get(i));
			results.add(new FutureTask<Integer>(workers[i]));
			executor.execute(results.get(i));
		}
		
		/*
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("from", 0);
		jsonObject.put("to", 9999);
		jsonObject.put("num_i", 3);
		jsonObject.put("base_string", "This is a test11");
		JSONObject jsonObject_1 = new JSONObject();
		jsonObject_1.put("from", 0);
		jsonObject_1.put("to", 9999);
		jsonObject_1.put("num_i", 3);
		jsonObject_1.put("base_string", "This is not a test");

		leader ab = new leader("localhost:8085",jsonObject);
		leader bc = new leader("localhost:8080",jsonObject_1);

		FutureTask<Integer> rs = new FutureTask<Integer>(ab);
		FutureTask<Integer> rs_1 = new FutureTask<Integer>(bc);

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(rs);
		executor.execute(rs_1);
		*/
		boolean found = false;
		while (!found) {
			try {
				
				for(int i = 0; i < num_worker; i++){
					if (results.get(i).isDone() && results.get(i).get() != -1){
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
	}
	
	
	private int process(){
		return 0;
	}
	
	private void get_worker(){
		
	}
	
	private void divide_work(JSONObject work){
		System.out.println(work);
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

	class leader implements Callable<Integer> {
		private String ip;
		private JSONObject j_obj;

		public leader(String ip, JSONObject j_obj) {
			this.ip = ip;
			this.j_obj = j_obj;
		}

		@Override
		public Integer call() throws Exception {
			int result = -1;
			try {
				Client client = Client.create();
				WebResource webResource = client.resource("http://" + ip + "/CrunchifyRESTJerseyExample/my_app/process/hash");
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


}