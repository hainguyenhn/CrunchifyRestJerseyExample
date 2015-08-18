package com.crunchify.client;
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

	public static void main(String[] args) {
		CrunchifyRESTJerseyClient crunchifyClient = new CrunchifyRESTJerseyClient();
		crunchifyClient.post();
	}
	

	private void post(ArrayList<JSONObject> list_worker) {
		/*
		 * This methods divides the work and send to multiple workers
		 */
		
		// call to update worker first 
		int num_worker = list_worker.size();
		
		//create a list of worker
		leader [] workers = new leader[num_worker];
		//create a list of future result
		ArrayList<FutureTask<Integer>> results = new ArrayList<FutureTask<Integer>>();
		for(int i = 0; i < num_worker; i++){
			workers[i] = new leader("8000", list_worker.get(i));
			results.add(new FutureTask<Integer>(workers[i]));
		}
		
		
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
		while (true) {
			try {
				if (rs.isDone() && rs_1.isDone()) {
					System.out.println("1 " + rs.get());
					System.out.println("2 " + rs_1.get());
					System.out.println("Done");
					// Initiates an orderly shutdown in which previously
					// submitted tasks are executed, but no new tasks will be
					// accepted. Invocation has no additional effect if already
					// shut down.
					executor.shutdown();
					return;
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
	
	private ArrayList<JSONObject> divide_work(JSONObject work){
		int from = work.getInt("from");
		int to = work.getInt("to");
		int num_i = work.getInt("num_i");
		String base = work.getString("base_string");
		
		
		return null;
		
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