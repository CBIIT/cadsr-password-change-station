package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtil {

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
