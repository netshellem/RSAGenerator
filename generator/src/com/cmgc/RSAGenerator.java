package com.cmgc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RSAGenerator {
	public static void main(String[] args) {
		KeyPair keyPair = null;
		String firstArg;
		String version = null;
		System.out.println("start ....");
		if (args.length > 0) {
		   firstArg = args[0];
		   System.out.println(firstArg + "\n");
		   if(firstArg.substring(0,2).toLowerCase().equals("-v")){
			   version = firstArg.substring(2);
		   }
		}
		if(version== null || version.length() < 8)
			return;
		if(version.length() > 8)
			version = version.substring(0, 8);
		if (!isNumeric(version))
			return;
		try {
			keyPair = buildKeyPair();
			PublicKey pubKey = keyPair.getPublic();
			// System.out.println("pub key is:" + pubKey.toString());
			PrivateKey priKey = keyPair.getPrivate();
			byte[] privateKeyBytes = priKey.getEncoded();
			byte[] publicKeyBytes = pubKey.getEncoded();
			generatePublicFile(publicKeyBytes, version);
			generatePrivateFile(privateKeyBytes, version);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
		final int keySize = 2048;
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(keySize);
		return keyPairGenerator.genKeyPair();
	}

	public static void generatePublicFile(byte[] publicKeyBytes,String version) {
		saveFile("data.h", getCppFileStr(version,publicKeyBytes));
	}
	
	public static void generatePrivateFile(byte[] publicKeyBytes,String version) {
		saveFile("V"+version+".java", getJavaFileStr(version,publicKeyBytes));
	}

	public static String byte2HexStr(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			sb.append("0x");
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString().trim();
	}
	
	public static void saveFile(String filename,String content){
		FileOutputStream out = null;   
        try {
			out = new FileOutputStream(new File(filename));
			out.write(content.getBytes());
			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String getCppFileStr(String version, byte[] keyBytes) {

		StringBuffer sb = new StringBuffer();
		sb.append("#ifndef NATIVERSA_DATA_H\n");
		sb.append("#define NATIVERSA_DATA_H\n");
		sb.append("unsigned char c_data[]={\n");
		sb.append(byte2HexStr(keyBytes));
		sb.append("};\n");
		sb.append("const char *version = \"" + version + "\";\n");
		sb.append("#endif\n");

		return sb.toString();
	}

	public static String getJavaFileStr(String version, byte[] keyBytes) {
		boolean leadingByte = true;
		StringBuffer sb = new StringBuffer();
		sb.append("package com.cmgc.keys;\n");
		sb.append("import com.cmgc.iKey;\n");
		sb.append("public class V" + version + " implements iKey {\n");
		sb.append("\tpublic  byte[] key={");

		for (int i = 0; i < keyBytes.length; i++) {
			if (leadingByte) {
				sb.append(keyBytes[i]);
				leadingByte = false;
			} else
				sb.append("," + keyBytes[i]);
		}

		sb.append("};\n");
		sb.append("\t@Override\n");
		sb.append("\tpublic byte[] getKeyBytes() {\n");
		sb.append("\t\treturn key;\n");
		sb.append("\t}\n");
		sb.append("}\n");
		return sb.toString();
	}
}
