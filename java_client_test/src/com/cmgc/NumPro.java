package com.cmgc;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class NumPro {
	public static void main(String[] args){
		NumPro obj = new NumPro();
		System.out.println(obj.setPhoneNumber("13811447905"));
	}
	public String setPhoneNumber(String number){
		String ret = null;
		byte[] pubkeyv1 = { 48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0,
				48, -126, 1, 10, 2, -126, 1, 1, 0, -114, 54, -106, -90, 15, 105, 105, -48, 116, -115, -20, -62, 98, 95,
				81, -114, 118, 107, -86, 115, -30, 119, -109, 83, 27, 125, -43, 71, -16, -98, -18, -27, -109, 44, -96,
				75, -97, -17, -7, -96, -36, -6, -53, -101, -72, -90, 43, -72, -98, -8, -18, -54, 70, 83, 90, 87, 99, 51,
				-124, -94, 30, -127, -64, -77, -64, 9, -60, -92, 115, -1, -3, 79, 66, -90, -82, 46, 82, -113, 48, 26,
				23, 80, -86, -10, 87, -50, -108, 8, -83, 72, -103, 123, 38, 113, -62, -89, -110, -7, -31, -32, 106, -53,
				-29, -105, -60, -30, -58, 50, -112, -4, 18, -57, 7, 34, -128, 125, -52, 43, 48, 118, 62, 4, 27, -27, -3,
				-92, -46, 122, 26, -72, 127, -9, 99, 66, -114, 116, 62, 92, -34, -25, -2, -50, -117, 1, 2, 50, 19, -92,
				-92, 86, 106, 112, -26, -115, -14, -42, -9, 120, -68, 74, 52, 54, -119, -111, -22, 116, -105, 59, 12,
				-89, -76, 32, 66, 108, -60, 45, 30, -119, -64, 97, 13, -103, -13, 39, 15, 28, 33, 120, 97, 90, 57, 81,
				-7, 97, -32, 54, -98, -70, 109, 93, 21, -106, -53, 7, -87, 32, 67, 107, 108, -49, -116, -68, -117, 66,
				-78, 110, 71, 76, -40, -56, -70, 79, -50, -24, 38, 110, 13, -1, -12, -24, -61, 64, -2, -99, -5, -2, 22,
				-50, -58, -64, -49, 0, 19, 45, 107, -123, -110, 101, -29, 24, -14, -63, -115, -90, 66, 15, 2, 3, 1, 0,
				1 };
		
		try {
			PublicKey publicKeyV1 = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pubkeyv1));
			byte[] encrypted = encrypt(publicKeyV1, "13811447905uuid");
			return "00000000"+Base64.encode(encrypted);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public static byte[] encrypt(PublicKey publicKey, String message) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return cipher.doFinal(message.getBytes());
	}
}
