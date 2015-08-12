package com.crunchify.restjersey;
import java.io.*;
import org.json.JSONObject;
import java.security.*;
import java.math.*;

public class hash {

	public static String get_string(int length){
		/*
		 * This method generates string of zero with certain length
		 */
		String result = "";
		for (int i = 0; i < length; i ++){
			result = result + "0";
		}
		return result;
	}

	public static boolean verify_hash(int length, String target){
		boolean result = false;
		String _result = _hash(target);
		if (_result.toString().substring(0, length).equals(get_string(length))){
			result = true;
		}
		return result;
	}


	public static String _hash(String target){
		String result = "";
		MessageDigest  m = null;
		try {
			m  = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m.update(target.getBytes(),0,target.length());
		byte byteData[] = m.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		result = sb.toString();
		return result;
	}
	
	public static int work(int length, String base){
		String s = base;
		int num_i = length;
		String _hash = "";
		boolean found = false;
		int j = -1;
		while(!found){
			j++;
			String a = s + j;
			if(verify_hash(num_i, a)){
				found = true;
			}
		}	
		return j;
	}

	public static void main(String[] args) {
		String s="This is a test";

		int result = work(3, s);
		System.out.println(verify_hash(3, s+result));

	}

}
