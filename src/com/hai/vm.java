/*
 * @author Hai Nguyen
 */
package com.hai;
import org.json.JSONObject;

public class vm extends JSONObject{
	/*
	 * Simple class represents each vm
	 * Initialize with name and ip
	 * Other fields will be set after jobs are divided
	 */
	public vm(String name, String ip){
		this.put("name", name);
		this.put("ip", ip);
		this.put("from", -1);
		this.put("to", -1);
		this.put("num_i", -1);
		this.put("base_string", -1);
	}

}
