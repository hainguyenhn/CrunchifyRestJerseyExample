package com.crunchify.client;
import com.crunchify.restjersey.hash;
import com.sun.jersey.api.client.Client;

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
		crunchifyClient.getCtoFResponse();
		crunchifyClient.getFtoCResponse();

		crunchifyClient.post();
	}

	private void post() {
	
			/*
			 * 	try {
			Client client = Client.create();
			WebResource webResource = client.resource("http://localhost:8080/CrunchifyRESTJerseyExample/my_app/process/hash");
			String input = "{\"message\":\"1\"}";
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("from", 0);
			jsonObject.put("to", 9999);
			jsonObject.put("num_i", 3);
			jsonObject.put("base_string", "This is a test");

			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, jsonObject.toString());

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			System.out.println("Output from Server .... \n");
			JSONObject json_output = new JSONObject(response.getEntity(String.class));
			System.out.println(json_output);
			int result = json_output.getInt("result");
			System.out.println(result);

		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		*/
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("from", 0);
			jsonObject.put("to", 9999);
			jsonObject.put("num_i", 3);
			jsonObject.put("base_string", "This is a test");
			JSONObject jsonObject_1 = new JSONObject();
			jsonObject.put("from", 0);
			jsonObject.put("to", 9999);
			jsonObject.put("num_i", 3);
			jsonObject.put("base_string", "This is not a test");
			
			leader ab = new leader("localhost",jsonObject);
			
			FutureTask<Integer> rs = new FutureTask<Integer>(ab);
			FutureTask<Integer> rs_1 = new FutureTask<Integer>(bc);

			ExecutorService executor = Executors.newFixedThreadPool(2);
			executor.execute(rs);
			executor.execute(rs_1);
			while (true) {
				try {
					if (rs.isDone() ) {
						System.out.println(rs.get());
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

	private void getFtoCResponse() {
		try {
			Client client = Client.create();
			WebResource webResource2 = client.resource("http://localhost:8080/CrunchifyRESTJerseyExample/crunchify/ftocservice/90");
			ClientResponse response2 = webResource2.accept("application/json").get(ClientResponse.class);
			if (response2.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response2.getStatus());
			}

			String output2 = response2.getEntity(String.class);
			System.out.println("\n============getFtoCResponse============");
			System.out.println(output2);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getCtoFResponse() {
		try {

			Client client = Client.create();
			WebResource webResource = client.resource("http://localhost:8080/CrunchifyRESTJerseyExample/crunchify/ctofservice/40");
			ClientResponse response = webResource.accept("application/xml").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);
			System.out.println("============getCtoFResponse============");
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class worker{
		boolean found = false;
		public synchronized  int work(String base, int num_i, int from, int to, String ip){
			int result = -1;
			try {

				Client client = Client.create();
				WebResource webResource = client.resource("http://"+ ip +"/CrunchifyRESTJerseyExample/my_app/process/hash");
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("from", from);
				jsonObject.put("to", to);
				jsonObject.put("num_i", num_i);
				jsonObject.put("base_string", base);

				ClientResponse response = webResource.type("application/json")
						.post(ClientResponse.class, jsonObject.toString());

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatus());
				}

				System.out.println("Output from Server .... \n");
				//String output = response.getEntity(String.class);
				JSONObject json_output = new JSONObject(response.getEntity(String.class));
				System.out.println(json_output);
				result = json_output.getInt("result");
				System.out.print("Here");
				System.out.print(result);

			} catch (Exception e) {
				System.out.print("Here1");
				e.printStackTrace();
			}
			return result;
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
				WebResource webResource = client.resource("http://" + ip + ":8080/CrunchifyRESTJerseyExample/my_app/process/hash");
				ClientResponse response = webResource.type("application/json")
						.post(ClientResponse.class, j_obj.toString());

				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ response.getStatus());
				}

				System.out.println("Output from Server .... \n");
				JSONObject json_output = new JSONObject(response.getEntity(String.class));
				System.out.println(json_output);
				result = json_output.getInt("result");

			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
	}


}