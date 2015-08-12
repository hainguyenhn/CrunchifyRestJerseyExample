package com.crunchify.restjersey;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

@Path("/process")
public class process {

	@Path("{f}")
	@GET
	@Produces("application/json")
	public Response double_value(@PathParam("f") float f) throws JSONException {

		JSONObject jsonObject = new JSONObject();
		float in = f;
		float out = f * 2;
		jsonObject.put("Original Value", in); 
		jsonObject.put("New Double Value", out);

		String result = "@Produces(\"application/json\") Output: \n\n Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
	}

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postMsg(String msg) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject(msg);
		String name = obj.getString("name");
		name = name + "hehe";
		int age = obj.getInt("age");
		age = age * 2;
		JSONObject obj_new = new JSONObject();
		obj_new.put("name", name);
		obj_new.put("age", age);  
		return Response.status(200).entity(obj_new.toString()).build();
	}
	
	@POST
	@Path("/hash")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response _hash(String msg) {
		JSONObject obj = new JSONObject(msg);
		int from = obj.getInt("from");
		int to = obj.getInt("to");
		int num_i = obj.getInt("num_i");
		return Response.status(200).entity(obj.toString()).build();
	}
}
