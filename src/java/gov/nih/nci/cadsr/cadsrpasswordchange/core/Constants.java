package gov.nih.nci.cadsr.cadsrpasswordchange.core;

public class Constants {

	public static final String APP_URI = "/cadsrpasswordchange";
	public static final String SERVLET_URI = "";	//production
//	public static final String SERVLET_URI = "/cadsrpasswordchange";	//dev
	
	public static final String LANDING_URL = APP_URI + "/jsp/login.jsp";
	public static final String ASK_USERID_URL = APP_URI + "/jsp/requestUserQuestions.jsp";
	public static final String RESET_URL = APP_URI + "/jsp/resetPassword.jsp";
	
	public static final String Q1 = "question1";
	public static final String Q2 = "question2";
	public static final String Q3 = "question3";
	public static final String USERNAME = "username";

	public static final String PWD_RESTRICTIONS = "Password Restrictions: Must not match previous 24 passwords, starts with a letter, 8-30 characters, contains a special character (_ # $), lowercase, uppercase and a number.";
}