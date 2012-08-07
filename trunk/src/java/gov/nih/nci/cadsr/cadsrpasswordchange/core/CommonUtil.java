package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class CommonUtil {

	private static OracleObfuscation x;
	static {
		try {
			x = new OracleObfuscation("$_12345&");
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Pad the String up to the specified length passed.
	 * @param text
	 * @param length
	 * @return
	 */
	public static String pad(String text, int length) {
		String retVal = text;

		if(text != null) {
			retVal = String.format("%"+ (length - 4)+"s", text).replace(' ', '*');	//final string must be a multiples of 8 bytes
		}
		
		return retVal;
	}
		
	public static String encode(String text) throws Exception {
		if(text != null) {
//			text = new String(Hex.encodeHex(x.encrypt(CommonUtil.pad(text, DAO.MAX_ANSWER_LENGTH).getBytes())));
			text = AeSimpleMD5.MD5(text);		
		}
    	return text;
    }

	public static String decode(String text) {
//		if(text != null) {
//			text = new String(x.decrypt(Hex.decodeHex(text.toCharArray())));
//		}
    	return text;
    }
	
	public static String toString(Throwable theException) {
		// Create a StringWriter and a PrintWriter both of these object
		// will be used to convert the data in the stack trace to a string.
		//
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		//
		// Instead of writting the stack trace in the console we write it
		// to the PrintWriter, to get the stack trace message we then call
		// the toString() method of StringWriter.
		//
		theException.printStackTrace(pw);

		return sw.toString();

	}
}
