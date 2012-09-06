package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class CommonUtil {

//	private static OracleObfuscation x;
//	static {
//		try {
//			x = new OracleObfuscation("$_12345&");
//		} catch (GeneralSecurityException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Pad the String up to the specified length passed.
	 * 
	 * @param text
	 * @param length
	 * @return
	 */
	public static String pad(String text, int length) {
		String retVal = text;

		if (text != null) {
			retVal = String.format("%" + (length - 4) + "s", text).replace(' ',
					'*'); // final string must be a multiples of 8 bytes
		}

		return retVal;
	}

	public static String encode(String text) throws Exception {
		if (text != null) {
			// text = new String(Hex.encodeHex(x.encrypt(CommonUtil.pad(text,
			// DAO.MAX_ANSWER_LENGTH).getBytes())));
			text = AeSimpleMD5.MD5(text);
		}
		return text;
	}

	public static String decode(String text) {
		// if(text != null) {
		// text = new String(x.decrypt(Hex.decodeHex(text.toCharArray())));
		// }
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

	public static String truncate(String text, int maxLength) {
		return StringUtils.abbreviate(text, maxLength);
	}

	/*
	 * Get the time difference between start and end dates (in days).
	 * @reference http://stackoverflow.com/questions/3491679/how-to-calculate-difference-between-two-dates-using-java
	 */
//	public static long daysBetween(Calendar startDate, Calendar endDate) {
//		Calendar date = (Calendar) startDate.clone();
//		long daysBetween = 0;
//		while (date.before(endDate)) {
//			date.add(Calendar.DAY_OF_MONTH, 1);
//			daysBetween++;
//		}
//		return daysBetween;
//	}
	public static long calculateDays(Date dateEarly, Date dateLater) {  
		   return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);  
	}
	
	public static String getPageHeader(String token) {
		String retVal = Constants.RESET_TITLE;

		if(token != null && token.equals("save")) {
			retVal = Constants.SETUP_TITLE;
		} else
		if(token != null && token.equals("change")) {
			retVal = Constants.CHANGE_PASSWORD_TITLE;
		} else
		if(token != null && token.equals("forgot")) {
			retVal = Constants.FORGOT_PASSWORD_TITLE;
		} else
		if(token != null && token.equals("unlock")) {
			retVal = Constants.UNLOCK_PASSWORD_TITLE;
		}
		
		return retVal;
	}
	
}
