package com.cmgc;



import java.security.KeyFactory;

import java.security.PrivateKey;

import java.security.spec.PKCS8EncodedKeySpec;



import javax.crypto.Cipher;



public class NumPro {

	/*public static void main(String[] args) {

		NumPro obj = new NumPro();

		String s1="00000001";

		String s2= null;

		String s3= "";

		String s4= "00000000";

		String test = obj.getPhoneNumber(s4);

		System.out.println(test);

	}*/



	public String getPhoneNumber(String encryptStr) {

		String ret = "2999";

		if(encryptStr == null || encryptStr.equals(""))

			return "2001";

		String version = getLeadingStr(encryptStr);

		byte[] key = null;

		byte[] secret = null;



		if (version == null)

			return "2002";

		try {

			Class<?> cls = Class.forName("com.cmgc.keys.V" + version);

			iKey obj = (iKey) cls.newInstance();

			key = obj.getKeyBytes();



		} catch (ClassNotFoundException e) {

			ret = "2003";

		} catch (InstantiationException | IllegalAccessException ex) {

			ret = "2999";

		}

		if (key == null)

			return ret;

		try {

			String clrEncStr = encryptStr.substring(8);

			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key));

			secret = decrypt(privateKey, Base64.decode(clrEncStr));

		} catch (Exception e) {

			ret = "2004";

		}

		

		if(secret == null)

			return ret;

		String numStr = new String(secret);

		if(numStr.length() > 11)

			numStr = numStr.substring(0, 11);

		if (isNumeric(numStr) && numStr.charAt(0)=='1')

			return numStr;

		

		return ret;

	}

	

	private  byte[] decrypt(PrivateKey privateKey, byte[] encrypted) throws Exception {

		Cipher cipher = Cipher.getInstance("RSA");

		cipher.init(Cipher.DECRYPT_MODE, privateKey);



		return cipher.doFinal(encrypted);

	}

	private String getLeadingStr(String str) {

		String ret = null;

		if (str.length() < 8)

			return ret;

		String s = str.substring(0, 8);

		if (isNumeric(s))

			return s;

		return ret;

	}



	private boolean isNumeric(String str) {

		for (int i = 0; i < str.length(); i++) {

			if (!Character.isDigit(str.charAt(i))) {

				return false;

			}

		}

		return true;

	}

}
