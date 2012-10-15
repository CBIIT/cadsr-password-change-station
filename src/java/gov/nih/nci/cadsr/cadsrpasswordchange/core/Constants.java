package gov.nih.nci.cadsr.cadsrpasswordchange.core;

public class Constants {

	public static int MAX_ANSWER_LENGTH = 500;
	public static final String TOOL_NAME = "PasswordChangeStation";
	public static final String HELP_LINK_PROPERTY = "HELP.ROOT";
	public static final String LOGO_LINK_PROPERTY = "LOGO.ROOT";
	public static final String APP_URI = "/cadsrpasswordchange";
	public static final String SERVLET_URI = "";	//jboss
	public static final String LANDING_URL = APP_URI + "/jsp/login.jsp";
	public static final String CHANGE_PASSWORD_URL = APP_URI + "/jsp/changePassword.jsp";
	public static final String SETUP_QUESTIONS_URL = APP_URI + "/jsp/setupPassword.jsp";
	public static final String ASK_USERID_URL = APP_URI + "/jsp/requestUserQuestions.jsp";
	public static final String Q1_URL = APP_URI + "/jsp/askQuestion1.jsp";
	public static final String Q2_URL = APP_URI + "/jsp/askQuestion2.jsp";
	public static final String Q3_URL = APP_URI + "/jsp/askQuestion3.jsp";
	public static final String RESET_URL = APP_URI + "/jsp/resetPassword.jsp";
	public static final String SETUP_SAVED_URL = APP_URI + "/jsp/questionsSaved.jsp";
	public static final String LOGGEDOUT_URL = APP_URI + "/jsp/loggedOut.jsp";
	
	public static final String RESET_TITLE = "caDSR Password Change Station";
	public static final String SETUP_TITLE = "Setup Security Questions";
	public static final String CHANGE_PASSWORD_TITLE = "Change Password";
	public static final String FORGOT_PASSWORD_TITLE = "Forgot My Password";
	public static final String UNLOCK_PASSWORD_TITLE = "Unlock My Account";
	
	public static final String Q1 = "question1";
	public static final String Q2 = "question2";
	public static final String Q3 = "question3";
	public static final String A1 = "answer1";
	public static final String A2 = "answer2";
	public static final String A3 = "answer3";
	public static final String ALL_QUESTIONS = "questions";
	public static final String ALL_ANSWERS = "answers";
	
	public static final String USERNAME = "username";

	public static final String ACTION_TOKEN = "action";		
	public static final String SAVE_TOKEN = "save";	
	public static final String CHANGE_TOKEN = "change";	
	public static final String FORGOT_TOKEN = "forgot";	
	public static final String UNLOCK_TOKEN = "unlock";	
	public static final String LOCKED_STATUS = "LOCKED";
	public static final String EXPIRED_STATUS = "EXPIRED";
	public static final String OPEN_STATUS = "OPEN";
	
	public static final String PWD_RESTRICTIONS = "<h3>Password Restrictions:</h3><table>" +
"			<td class=\"face\" style=\"WIDTH: 617px\" colspan=\"2\">" +
"    <ul>" +
"Your new password may not re-use your last 24 passwords.<br>" +
"You may not change your password more than once within 24 hours.<br>" +
"Your new password must be at least 8 and no more than 30 characters long.<br>" +
"Your new password must start with a letter.<br>" +
"Your new password may only use characters from the following categories and must include characters from at least three of these categories:" +
"<br><br>" +
"            	<li>Uppercase Letters (A-Z)</li>" +
"            	<li>Lowercase Letters (a-z)</li>" +
"            	<li>Numerals (0-9)</li>" +
"            	<li>Special Characters ( _  #  $)" + 
"    </ul><tt>" +
"    </tt></td>" +
"</table>";
	
    /**
     * The name of the datasource url.
     */
    public static final String _DSURL = "DSurl";
    
    /**
     * The name of the default datasource access account.
     */
    public static final String _DSUSER = "DSusername";
    
    /**
     * The password for the default datasource access account.
     */
    public static final String _DSPSWD = "DSpassword";
    
    //=== notifications status flags
    public static final int DEACTIVATED_VALUE = 0;
    public static final String SUCCESS = "s";
    public static final String FAILED = "f";
    public static final String INVALID = "i";
    public static final String UNKNOWN = "u";
    //=== notifications notification configuration
    public static final String NOTIFICATION_TYPE = "EMAIL.NOTIFY_TYPE";
    public static final String HOST_TOKEN = "${host}";
    //=== notifications email configuration
    public static final String EMAIL_ADDRESS = "EMAIL.ADDR";
    public static final String EMAIL_ADMIN_NAME = "ADMIN.NAME";
    public static final String EMAIL_ERROR = "EMAIL.ERROR";
    public static final String EMAIL_HOST = "EMAIL.HOST"; 
    public static final String EMAIL_PORT = "EMAIL.PORT"; 
    public static final String EMAIL_INTRO = "EMAIL.INTRO";
    public static final String EMAIL_SUBJECT = "EMAIL.SUBJECT";
    public static final String EMAIL_DAYS_TOKEN = "${daysLeft}";
    public static final String EMAIL_EXPIRY_DATE_TOKEN = "${expiryDate}";
    public static final String EMAIL_USER_ID_TOKEN = "${userid}";	//CADSRPASSW-62
}