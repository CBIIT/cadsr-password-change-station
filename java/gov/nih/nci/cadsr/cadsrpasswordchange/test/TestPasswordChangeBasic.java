package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordChangeHelper;

import org.junit.Test;

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
}
