package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.AbstractDao;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.ConnectionUtil;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.DAO;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordChangeHelper;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.Result;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.ResultCode;

import org.junit.Test;

public class TestPasswordReset {

	private static Connection connection = null;
	private static DataSource datasource = null;
	private static AbstractDao dao;
    public static String ADMIN_ID = "@systemAccountName@";
    public static String ADMIN_PASSWORD = "@systemAccountPassword@";

	//@Test
	public void testBadUserNameInLogin() {
		String username = "@hongj"; // bad
		String password = "test123";
		String errorMessage = PasswordChangeHelper.validateLogin(username,
				password);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testBadPasswordInLogin() {
		String username = "chongj";
		String password = "@7esT123"; // bad
		String errorMessage = PasswordChangeHelper.validateLogin(username,
				password);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testBadUserIdInChangePassword() {
		String username = "@hongj"; // bad
		String oldPassword = "test123";
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(
				username, oldPassword, newPassword, newPassword2,
				sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testBadOldPasswordInChangePassword() {
		String username = "chongj";
		String oldPassword = "@7esT123"; // bad
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(
				username, oldPassword, newPassword, newPassword2,
				sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testBadNewPasswordInChangePassword() {
		String username = "chongj";
		String oldPassword = "test123";
		String newPassword = "@7esT123"; // bad
		String newPassword2 = "@7esT123";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(
				username, oldPassword, newPassword, newPassword2,
				sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testNewPasswordNotMatchingInChangePassword() {
		String username = "chongj";
		String oldPassword = "test123";
		String newPassword = "test456";
		String newPassword2 = "test123"; // bad
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(
				username, oldPassword, newPassword, newPassword2,
				sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testLoginIdNotMatchingInChangePassword() {
		String username = "chongj";
		String oldPassword = "test123";
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username + "xs"; // bad
		String httpRequestNewPassword2 = newPassword2;
		String errorMessage = PasswordChangeHelper.validateChangePassword(
				username, oldPassword, newPassword, newPassword2,
				sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	//@Test
	public void testNewRequestedPasswordNotMatchingInChangePassword() {
		String username = "chongj";
		String oldPassword = "test123";
		String newPassword = "test456";
		String newPassword2 = "test456";
		String sessionUsername = username;
		String httpRequestNewPassword2 = newPassword2 + "yy"; // bad
		String errorMessage = PasswordChangeHelper.validateChangePassword(
				username, oldPassword, newPassword, newPassword2,
				sessionUsername, httpRequestNewPassword2);
		assertNotNull(errorMessage, errorMessage);
	}

	public static Connection getConnection(String username, String password) throws Exception {
		String dbtype = "oracle";
		String dbserver = "137.187.181.4";
		String dbname = "DSRDEV";
//		String username = "root";
//		String password = "root";
		int port = 1551;
		ConnectionUtil cu = new ConnectionUtil();
		cu.setUserName(username);
		cu.setPassword(password);
		cu.setDbms(dbtype);
		cu.setDbName(dbname);
		cu.setServerName(dbserver);
		cu.setPortNumber(port);
		Connection conn = cu.getConnection();
		return conn;
	}
	
	@Test
	public void testPasswordReset() {
		String username = "GUEST";
		String newPassword = "GUEST";
		Result returned = null;
        try {
        	Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
        	dao = new DAO(conn);
        	returned = dao.resetPassword(username, newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Result expected = new Result(ResultCode.PASSWORD_CHANGED);
		assertEquals(expected.getResultCode(), returned.getResultCode());
	}

	@Test
	public void testPasswordChange() {
		String username = "GUEST";
		String oldPassword = "GUEST";
		String newPassword = "test@Lien202";
		Result returned = null;
        try {
        	Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
        	dao = new DAO(conn);
        	returned = dao.changePassword(username, oldPassword, newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
        Result expected = new Result(ResultCode.PASSWORD_CHANGED);
		assertNotSame(expected.getResultCode(), returned.getResultCode());
	}

}
