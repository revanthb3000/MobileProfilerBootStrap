package org.iitg.mobileprofiler.p2p.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilityFunctions {
	
	/**
	 * Utility function that generates the hexdigest for a given input string.
	 * @param inputString
	 * @return
	 */
	public static String getHexDigest(String inputString){
		MessageDigest md = null;;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		md.update(inputString.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}
		return sb.toString();
	}
	
}
