package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class EmailHelper {
	public static String handleDaysToken(String originalText, int daysLeft) {
		String ret = originalText;

		if(originalText != null) {
			ret = StringUtils.replace(originalText, Constants.EMAIL_DAYS_TOKEN, String.valueOf(daysLeft));
		}
		
		return ret;
	}
}