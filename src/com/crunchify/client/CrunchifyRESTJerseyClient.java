package com.crunchify.client;
import com.crunchify.restjersey.hash;
import com.sun.jersey.api.client.Client;
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
		try {

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
			//String output = response.getEntity(String.class);
			String json_output = response.getEntity(String.class);
			System.out.println(json_output);

		} catch (Exception e) {
			e.printStackTrace();
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
}