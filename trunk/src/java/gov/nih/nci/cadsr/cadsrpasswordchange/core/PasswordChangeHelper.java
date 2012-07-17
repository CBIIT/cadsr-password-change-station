package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class PasswordChangeHelper {

    private static final String INPUT_FILTER = "[a-zA-Z0-9$#_]+";  // (this also prevents spaces, i.e. multiple words) //$NON-NLS-1$
	    
	public static String validateLogin(String username, String password) {
		String retVal = null;
		// Limit input to legal characters before attempting any processing

		if (!username.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.1"); //$NON-NLS-1$
		}

		if (!password.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.2"); //$NON-NLS-1$
		}

		return retVal;
	}
	
	public static String validateChangePassword(String username, String oldPassword, String newPassword, String newPassword2, String sessionUsername, String httpRequestNewPassword2) {
		String retVal = null;
		
		if (!username.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.3"); //$NON-NLS-1$
		}

		if (oldPassword != null && !oldPassword.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.4"); //$NON-NLS-1$
		}
		
		if (!newPassword.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.5"); //$NON-NLS-1$
		}

		if (!newPassword2.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.6"); //$NON-NLS-1$
		}					
		
		// Basic validation
		
		// make sure username entered is the same as previously authenticated (even though we'll authenticate again)
		if (!username.equalsIgnoreCase(sessionUsername)) {  // (Oracle usernames ignore case, we should too)
			retVal = Messages.getString("PasswordChangeHelper.7"); //$NON-NLS-1$
		}

		if (!newPassword.equals(httpRequestNewPassword2)) {
			retVal = Messages.getString("PasswordChangeHelper.8"); //$NON-NLS-1$
		}
	
		return retVal;
	}
	
	public static String validateChangePassword2(String username, String newPassword, String newPassword2) {
		String retVal = null;

		
		if (!username.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.3"); //$NON-NLS-1$
		}

		if (!newPassword.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.5"); //$NON-NLS-1$
		}

		if (!newPassword2.matches(INPUT_FILTER)) {
			retVal = Messages.getString("PasswordChangeHelper.6"); //$NON-NLS-1$
		}					
		
		if (!newPassword.equals(newPassword2)) {
			retVal = Messages.getString("PasswordChangeHelper.8"); //$NON-NLS-1$
		}					

		return retVal;
	}

	/*
	 * Method to validate that the user setup all the questions and answers properly.
	 */
	public static String validateSecurityQandA(int totaQuestions, String username, Map<String, String> userQuestions, Map<String, String> userAnswers) throws Exception {
		String retVal = null;
		
		if(userQuestions == null) {
			throw new Exception("User provided questions are NULL or empty.");
		}
		
		if(userAnswers == null) {
			throw new Exception("User provided answers are NULL or empty.");
		}

		if (userQuestions.size() != totaQuestions || userAnswers.size() != totaQuestions) {
			retVal = Messages.getString("PasswordChangeHelper.125"); //$NON-NLS-1$
		}					
		
		
		return retVal;
	}

	/*
	 * Method to validate that the user's answer(s) matched the one stored in the database.s
	 */
	public static String validateResetPassword(Map<String, String> userQna, Map<String, String> storedQna) throws Exception {
		String retVal = null;
		int correctAnswerCount = 0;
		
		if(userQna == null) {
			throw new Exception("User submitted Q & A is NULL or empty.");
		}
		if(storedQna == null) {
			throw new Exception("User stored Q & A is NULL or empty.");
		}
		// Security Q & A validation
		if(userQna.size() == 0) {
			retVal = Messages.getString("PasswordChangeHelper.110"); //$NON-NLS-1$			
		} else
		if(storedQna != null && storedQna.size() == 0) {
			retVal = Messages.getString("PasswordChangeHelper.120"); //$NON-NLS-1$			
		} else {
			//iterate through all the questions based on user provided answer(s)
			for (Object key : userQna.keySet()) {
			    String value = userQna.get(key);
			    //match it with the stored answers
			    if(value.equals(storedQna.get(key))) {
			    	correctAnswerCount++;
			    }
			}
			if(correctAnswerCount == 0) {
				retVal = Messages.getString("PasswordChangeHelper.130"); //$NON-NLS-1$
			}
		}
		
		return retVal;
	}
	
	/*
	 * Method to validate that the user's at least one question and/or answer are of the right length.
	 */
	public static boolean validateQuestionsLength(int totaQuestions, Map<String, String> userQuestions, Map<String, String> userAnswers) throws Exception {
		final int QUESTIONS_VALID_LENGTH = 10;
		final int ANSWERS_VALID_LENGTH = 1;
		boolean retVal = false;
		int qcount = 0, acount = 0;
		
		if(userQuestions == null) {
			throw new Exception("Questions are NULL or empty.");
		}
		if(userAnswers == null) {
			throw new Exception("Answers are NULL or empty.");
		}
		for (Object key : userQuestions.keySet()) {
		    String value = userQuestions.get(key);
		    if(value != null && value.length() >= QUESTIONS_VALID_LENGTH) {
		    	qcount++;
		    }
		}
		for (Object key : userAnswers.keySet()) {
		    String value = userAnswers.get(key);
		    if(value != null && value.length() >= ANSWERS_VALID_LENGTH) {
		    	acount++;
		    }
		}
		
		if(qcount == totaQuestions && acount == totaQuestions) retVal = true;

		return retVal;
	}
	
}