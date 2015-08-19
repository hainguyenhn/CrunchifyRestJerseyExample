/*
 * @author Hai Nguyen
 * 
 */
package com.hai.restjersey;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.zookeeper.KeeperException;
import org.json.JSONObject;

import com.hai.client.leader;

@Path("/process")
public class process {
	/*
	 * process class which takes care of hash post request
	 */
	@POST
	@Path("/hash")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response _hash(String msg) {
		/*
		 * this method takes care of hash call post request
		 * @in msg: string in json format contains from, to, num_i, base_string
		 * @out obj.toString: string in json format contains host ip and result 
		 */
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

	@POST
	@Path("/submit")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response _submit(String msg) {
		/*
		 * this method takes care of hash call post request
		 * @in msg: string in json format contains from, to, num_i, base_string
		 * @out obj.toString: string in json format contains host ip and result 
		 */
		JSONObject obj = new JSONObject(msg);
		
		JSONObject _return_obj = new JSONObject();
		leader _leader = null;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			_return_obj.put("name", ip.getHostAddress());
			_leader = new leader(ip.getHostAddress());

		} catch (UnknownHostException e) {
			System.out.println("Can't get ip address");
			e.printStackTrace();
		}
		catch (KeeperException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int result = _leader.run(obj);

		_return_obj.put("result", result);
		return Response.status(200).entity(_return_obj.toString()).build();
	}
}
