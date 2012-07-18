package gov.nih.nci.cadsr.cadsrpasswordchange.core;

public class Constants {

	public static final String APP_URI = "/cadsrpasswordchange";
	public static final String SERVLET_URI = "";	//production
//	public static final String SERVLET_URI = "/cadsrpasswordchange";	//dev only
	
	public static final String LANDING_URL = APP_URI + "/jsp/login.jsp";
	public static final String CHANGE_PASSWORD_URL = APP_URI + "/jsp/changePassword.jsp";
	public static final String SETUP_QUESTIONS_URL = APP_URI + "/jsp/setupPassword.jsp";
	public static final String ASK_USERID_URL = APP_URI + "/jsp/requestUserQuestions.jsp";
	public static final String Q1_URL = APP_URI + "/jsp/askQuestion1.jsp";
	public static final String Q2_URL = APP_URI + "/jsp/askQuestion2.jsp";
	public static final String Q3_URL = APP_URI + "/jsp/askQuestion3.jsp";
	public static final String RESET_URL = APP_URI + "/jsp/resetPassword.jsp";
	public static final String SETUP_SAVED_URL = APP_URI + "/jsp/questionsSaved.jsp";
	
	public static final String RESET_TITLE = "caDSR Password Change Station";
	public static final String Q1 = "question1";
	public static final String Q2 = "question2";
	public static final String Q3 = "question3";
	public static final String A1 = "answer1";
	public static final String A2 = "answer2";
	public static final String A3 = "answer3";
	public static final String ALL_QUESTIONS = "questions";
	public static final String ALL_ANSWERS = "answers";
	
	public static final String USERNAME = "username";

	public static final String PWD_RESTRICTIONS = "Password Restrictions: Must not match previous 24 passwords, starts with a letter, 8-30 characters, contains a special character (_ # $), lowercase, uppercase and a number.";
}