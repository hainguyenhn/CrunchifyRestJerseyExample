/*
 * @author Hai Nguyen
 * 
 */
package com.hai.restjersey;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;

@Path("/process")
public class process {
	/*
	 * process class which takes care of hash call post request
	 * @in msg: string in json format contains from, to, num_i, base_string
	 * @out obj.toString: string in json format contains host ip and result 
	 */
	@POST
	@Path("/hash")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response _hash(String msg) {
		JSONObject obj = new JSONObject(msg);
		int from = obj.getInt("from");
		int to = obj.getInt("to");
		int num_i = obj.getInt("num_i");
		String base = obj.getString("base_string");
	
		int result = hash.work(num_i, base, from, to);
		JSONObject _return_obj = new JSONObject();
		try {
			InetAddress ip = InetAddress.getLocalHost();
			_return_obj.put("name", ip.getHostAddress());
		} catch (UnknownHostException e) {
			System.out.println("Can't get ip address");
			e.printStackTrace();
		}
		_return_obj.put("result", result);
		return Response.status(200).entity(_return_obj.toString()).build();
	}
}
