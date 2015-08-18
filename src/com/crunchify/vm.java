package com.crunchify;

import org.json.JSONObject;

public class vm extends JSONObject{
	public vm(String name, String ip){
		this.put("name", name);
		this.put("ip", ip);
		this.put("from", -1);
		this.put("to", -1);
		this.put("num_i", -1);
		this.put("base_string", -1);
	}

}
