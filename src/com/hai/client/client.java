/**
 * @author Hai Nguyen
 * @based on crunchify
 */
package com.hai.client;
import com.sun.jersey.api.client.Client;

import org.json.JSONObject;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class client {
	/*
	 * Simple client class
	 */

	public static void main(String[] args) {
		String ip = "";
		JSONObject work =new JSONObject();
		work.put("from", 0);
		work.put("to", 5);
		work.put("num_i", 10);
		work.put("base_string", "This 21is a test");

		int result = -1;
		try {
			Client client = Client.create();
			WebResource webResource = client.resource("http://" + ip + "/leader_worker/my_app/process/submit");
			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, work.toString());

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			JSONObject json_output = new JSONObject(response.getEntity(String.class));
			result = json_output.getInt("result");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(result);
	}
}
