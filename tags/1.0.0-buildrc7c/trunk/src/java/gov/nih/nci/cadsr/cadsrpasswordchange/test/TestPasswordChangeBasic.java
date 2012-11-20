package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordChangeHelper;

import org.junit.Test;

/*
 * To run this, set up the folllowing -
 * 1. Build the app
 * 2. Add src/java/gov/nih/nci/cadsr/cadsrpasswordchange/core (need messages.properties) into your classpath
 * 
 */
public class TestPasswordChangeBasic {
	
	@Test
	public void testBadUserNameInLogin() {
		String username = "@hongj";			//bad
		String password = "test123";
		String errorMessage = PasswordChangeHelper.validateLogin(username, password);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testBadPasswordInLogin() {
		String username = "chongj";
		String password = "@7esT123";		//bad
		String errorMessage = PasswordChangeHelper.validateLogin(username, password);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testBadUserIdInChangePassword() {
		String username = "@hongj";			//bad
		String oldPassword = "test123";
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testBadOldPasswordInChangePassword() {
		String username = "chongj";			
		String oldPassword = "@7esT123";		//bad
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testBadNewPasswordInChangePassword() {
		String username = "chongj";			
		String oldPassword = "test123";		
		String newPassword = "@7esT123";		//bad
		String newPassword2 = "@7esT123";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testNewPasswordNotMatchingInChangePassword() {
		String username = "chongj";			
		String oldPassword = "test123";		
		String newPassword = "test456";		
		String newPassword2 = "test123";		//bad
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testLoginIdNotMatchingInChangePassword() {
		String username = "chongj";			
		String oldPassword = "test123";		
		String newPassword = "test456";		
		String newPassword2 = "test456";		
		String sessionUsername = username + "xs";		//bad
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testNewRequestedPasswordNotMatchingInChangePassword() {
		String username = "chongj";			
		String oldPassword = "test123";		
		String newPassword = "test456";		
		String newPassword2 = "test456";		
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2 + "yy";	//bad
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testWithoutLettersChangePassword() {
		String username = "test111";	//should not matter what it is
		String oldPassword = "test123";	//should not matter what it is
		String newPassword = "123456";
		String newPassword2 = "123456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		if(errorMessage != null) System.out.println("No Letters Error: " + errorMessage);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testWithoutNumeralsChangePassword() {
		String username = "test111";	//should not matter what it is
		String oldPassword = "test123";	//should not matter what it is
		String newPassword = "testing";
		String newPassword2 = "testing";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		if(errorMessage != null) System.out.println("No Numerals Error: " + errorMessage);
		assertNotNull(errorMessage, errorMessage);
	}
	
	@Test
	public void testWithoutSpecialCharactersChangePassword() {
		String username = "test111";	//should not matter what it is
		String oldPassword = "test123";	//should not matter what it is
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		if(errorMessage != null) System.out.println("No Special Characters Error: " + errorMessage);
		assertNotNull(errorMessage, errorMessage);
	}

	@Test
	public void testGoodChangePassword() {
		String username = "test111";	//should not matter what it is
		String oldPassword = "test123";	//should not matter what it is
		String newPassword = "te$t456";
		String newPassword2 = "te$t456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		if(errorMessage != null) System.out.println("Good Characters Error: " + errorMessage);
		assertNull(errorMessage, errorMessage);
	}
	
	@Test
	public void testUnmatchedRepeatedpasswordChangePassword() {
		String username = "test111";	//should not matter what it is
		String oldPassword = "test123";	//should not matter what it is
		String newPassword = "Pw4_0017";
		String newPassword2 = "abcde";	//CADSRPASSW-88
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(username, oldPassword, newPassword, newPassword2, sessionUsername, httpRequestNewPassword2);
		if(errorMessage != null) System.out.println("Unmatched Repeated Password Error: " + errorMessage);
		assertNotNull(errorMessage, errorMessage);
	}
	
}
