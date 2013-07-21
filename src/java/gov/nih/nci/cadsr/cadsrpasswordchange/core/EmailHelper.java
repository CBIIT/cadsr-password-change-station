/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class EmailHelper {
	public static String handleExpiryDateToken(String originalText, Date expiryDate) {
		String ret = originalText;
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		String fDate = formatter.format(expiryDate);

		if(originalText != null) {
			ret = StringUtils.replace(originalText, Constants.EMAIL_EXPIRY_DATE_TOKEN, fDate);
		}
		
		return ret;
	}

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
	
	public static String handleHostToken(String originalText, String host) {
		String ret = originalText;

		if(originalText != null) {
			ret = StringUtils.replace(originalText, Constants.EMAIL_WEB_HOST_TOKEN, host);
		}
		
		return ret;
	}
}