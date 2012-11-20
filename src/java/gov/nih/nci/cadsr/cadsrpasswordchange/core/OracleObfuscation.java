package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * Source: https://forums.oracle.com/forums/thread.jspa?threadID=586804
 * References: 
 * http://www.di-mgt.com.au/cryptopad.html
 * http://docstore.mik.ua/orelly/java-ent/security/ch13_05.htm
 *
 */
public class OracleObfuscation {
	private String algorithm1 = "DES";// magical mystery constant
//	private String algorithm2 = "DES/CBC/PKCS5Padding";// magical mystery constant
	private String algorithm2 = "DES/CBC/NoPadding";// magical mystery constant
	private IvParameterSpec iv = new IvParameterSpec(new byte[] { 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00 });// magical mystery constant
	private Cipher cipher;
	private SecretKey key;

	public OracleObfuscation(String secretString)
			throws GeneralSecurityException {
		key = new SecretKeySpec(secretString.getBytes(), algorithm1);
		cipher = Cipher.getInstance(algorithm2);
	}

	/*
	 * Source: http://eternusuk.blogspot.com/2008/09/java-triple-des-example.html
	 * Remark: Leave this for JDK 1.6 (3DES)
	 */
	/*
	public OracleObfuscation(String secretString) {
		 byte[] keyValue = Hex.decodeHex("secretString".toCharArray());
		
		 DESedeKeySpec keySpec = new DESedeKeySpec(keyValue);
		
		 IvParameterSpec iv = new IvParameterSpec(new byte[8]);
		
		 SecretKey key = SecretKeyFactory.getInstance(algorithm).generateSecret(keySpec);
		 Cipher encrypter = Cipher.getInstance(transformation);
		 encrypter.init(Cipher.ENCRYPT_MODE, key, iv);
		
		 byte[] input = PLAIN_TEXT.getBytes("UTF-8");
		
		 byte[] encrypted = encrypter.doFinal(input);
		
		 assertEquals("Ensure that we have encrypted the token correctly", CIPHER_TEXT, new String(Hex
		         .encodeHex(encrypted)).toUpperCase());
		
		 Cipher decrypter = Cipher.getInstance(transformation);
	}
	*/
	
	public byte[] encrypt(byte[] bytes) throws GeneralSecurityException {
		cipher.init(Cipher.ENCRYPT_MODE, key, iv); // normally you could leave
													// out the IvParameterSpec
													// argument, but not with
													// Oracle

		return cipher.doFinal(bytes);
	}

	public byte[] decrypt(byte[] bytes) throws GeneralSecurityException {
		cipher.init(Cipher.DECRYPT_MODE, key, iv); // normally you could leave
													// out the IvParameterSpec
													// argument, but not with
													// Oracle

		return cipher.doFinal(bytes);
	}

	// invoke like:
	// java -cp commons-codec-1.3.jar:. OracleObfuscation cleartext
	// or
	// java -cp commons-codec-1.3.jar:. OracleObfuscation ecrypted
	// so you'd run this twice to encrypt and then decrypt

	public static void main(String[] args) throws Exception {
		OracleObfuscation x = new OracleObfuscation("$_12345&"); // just a
																	// random
																	// pick for
																	// testing
		String text = CommonUtil.pad("BB", Constants.MAX_ANSWER_LENGTH);	//args[0];

		byte[] encrypted = x.encrypt(text.getBytes());
		String encoded = new String(Hex.encodeHex(encrypted));
		System.out.println("Encrypted/Encoded: \"" + encoded + "\"");

		byte[] decoded = Hex.decodeHex(encoded.toCharArray());
		String decrypted = new String(x.decrypt(decoded));
//		String decrypted = new String(x.decrypt(encrypted));
		System.out.println("Decoded/Decrypted: \"" + decrypted + "\"");
	}
}
