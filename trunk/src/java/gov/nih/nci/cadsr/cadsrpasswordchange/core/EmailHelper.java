package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

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
	
	public static String handleUserIDToken(String originalText, User user) throws Exception {
		String ret = originalText;
		if(user == null || user.getUsername() == null) {
			throw new Exception("User or user id is null or empty.");
		}

		if(originalText != null) {
			ret = StringUtils.replace(originalText, Constants.EMAIL_USER_ID_TOKEN, user.getUsername());
		}
		
		return ret;
	}
	
}