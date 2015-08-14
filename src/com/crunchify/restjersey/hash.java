package com.crunchify.restjersey;
import java.io.*;
import org.json.JSONObject;
import java.security.*;
import java.math.*;

public class hash {
	public static String get_string(int length){
		/*
		 * This method generates string of zero with certain length
		 * @in: length of zero string
		 * @out: string of zero
		 */
		String result = "";
		for (int i = 0; i < length; i ++){
			result = result + "0";
		}
		return result;
	}

	public static boolean verify_hash(int length, String target){
		/*
		 * This method verifies that hashed target string will have number of leading zeroes
		 * @in: length of leading zeroes
		 * @in target: string target to be hashed and verified
		 */
		boolean result = false;
		String _result = _hash(target);
		if (_result.toString().substring(0, length).equals(get_string(length))){
			result = true;
		}
		return result;
	}

	public static String _hash(String target){
		/*
		 * This method hashes a string using md5
		 * @in: string to be hashed
		 * @out: hashed string
		 */
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

	public static int work(int length, String base, int from, int to){
		/*
		 * This method generates string of zero with certain length
		 * @in length: length of leading zero
		 * @in string: base string
		 * @in from: starting i 
		 * @out to: ending i
		 * @out:  i if found is number to be combined with base to get number of leading zero
		 * 	   : -1 if not found
		 */
		String s = base;
		int num_i = length;
		boolean found = false;
		int j = from;
		while(!found && j <= to){
			String a = s + j;
			if(verify_hash(num_i, a)){
				found = true;
			}
			else{
				j++;
			}
		}
		if(!found){
			j = -1;
		}
		return j;
	}

	public static void main(String[] args) {
		String s="This is a test";
		int result = work(3, s, 0, 9999);
		System.out.print(result);
		System.out.println(verify_hash(2, s+result));
	}
}
