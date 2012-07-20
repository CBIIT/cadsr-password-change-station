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

//	public static final String PWD_RESTRICTIONS = "Password Restrictions: Must not match previous 24 passwords, starts with a letter, 8-30 characters, contains a special character (_ # $), lowercase, uppercase and a number.";
	public static final String PWD_RESTRICTIONS = "<h3>Password Restrictions:</h3> <p><table>" +
"			<td class=\"face\" style=\"WIDTH: 617px\" colspan=\"2\">" +
"You may not re-use your last 24 passwords. (Note: Passwords that only differ by uppercase/lowercase are not considered different passwords.)" +
"    <ul>                                         " +
"You may not change your password more than once within 24 hours.<br>" +
"Your password must be at least 8 and no more than 30 characters long.<br>" +
"Your password must start with a letter.<br>" +

"Your password may only use characters from the following groups and must use characters from at least three of the four groups:" +
"<br><br>" +
"            	<li>Uppercase letters: ABCDEFGHIJKLMNOPQRSTUVWXYZ</li>" +
"            	<li>Lowercase letters: abcdefghijklmnopqrstuvwxyz</li>" +
"            	<li>Numbers: 1234567890</li>" +
"            	<li>Special characters: _ # $" + 
"            	- + _ = | \\ { } [ ] (): ; \" ' &lt; &gt; , . ? / )</li>" +
"<br>For Example: The password ADGHJKL248 uses characters from only two groups (uppercase and number) and is not acceptable. ADGHJKL248# uses characters from three groups (uppercase, number, and special) and is acceptable.<br>" +
"<p><b><font color=\"red\">IT Administrators Please Note:</font></b> Secondary accounts are required to have a 15 character password or passphrase." +
"<br><br>" +
"<b><font color=\"red\">NCI Users Please Note:</font></b> You must include one of each of the four categories" +
"	      and spaces are NOT allowed." +
"<br><br>" +
"Your password can contain spaces, but they are not counted as special characters. Your password may not contain more than three contiguous letters from your name. For more detail about making a good password, see" + 
"            <a href=\"Password_Passcode_Guidance.doc\" tabindex=\"1\">" +
"            NIH User Password Guidance</a>, " +
"            <a href=\"Pass_Phrases.pdf\" tabindex=\"2\">" + 
"            NIH Pass Phrase Guidance</a>, and <a href=\"nottodo.htm\" tabindex=\"3\" target=\"_top\">" +
"            What Not To Do</a>." +
"    </ul><tt>" +
"    </tt></td>" +
"</table>";
}