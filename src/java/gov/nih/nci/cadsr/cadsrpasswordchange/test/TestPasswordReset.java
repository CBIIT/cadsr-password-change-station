package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.*;
import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;
import gov.nih.nci.cadsr.cadsrpasswordchange.domain.UserSecurityQuestion;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPasswordReset {

	// private static Connection connection = null;
	private static DataSource datasource = null;
	private static PasswordChange dao;
	public static String ADMIN_ID = "cadsrpasswordchange";
	public static String ADMIN_PASSWORD = "cadsrpasswordchange";
	public static String USER_ID = "TEST111";	//this user has to exist, otherwise test will fail
	private static OracleObfuscation x;
	static {
		try {
			x = new OracleObfuscation("$_12345&");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}
	
	// @Test
	public void testBadUserNameInLogin() {
		String username = "@hongj"; // bad
		String password = "test123";
		String errorMessage = PasswordChangeHelper.validateLogin(username,
				password);
		assertNotNull(errorMessage, errorMessage);
	}

	// @Test
	public void testBadPasswordInLogin() {
		String username = "chongj";
		String password = "@7esT123"; // bad
		String errorMessage = PasswordChangeHelper.validateLogin(username,
				password);
		assertNotNull(errorMessage, errorMessage);
	}

	// @Test
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

	// @Test
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

	// @Test
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

	// @Test
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

	// @Test
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

	// @Test
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

	public static Connection getConnection(String username, String password)
			throws Exception {
		String dbtype = "oracle";
		String dbserver = "137.187.181.4";
		String dbname = "DSRDEV";
		// String username = "root";
		// String password = "root";
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

	// @Test
	// public void testPasswordReset() {
	// String username = "GUEST";
	// String newPassword = "GUEST";
	// Result returned = null;
	// try {
	// Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
	// dao = new DAO(conn);
	// returned = dao.resetPassword(username, newPassword);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Result expected = new Result(ResultCode.PASSWORD_CHANGED);
	// assertEquals(expected.getResultCode(), returned.getResultCode());
	// }

	// @Test
	public void testPasswordChange() {
		String username = "GUEST";
		String oldPassword = "GUEST";
		String newPassword = "test@Lie777";
		Result returned = null;
		try {
			Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			returned = dao.changePassword(username, oldPassword, newPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Result expected = new Result(ResultCode.PASSWORD_CHANGED);
		assertNotSame(expected.getResultCode(), returned.getResultCode());
	}

	public byte[] toBytes(InputStream in) throws Exception {
		OutputStream out = null;
		// Read bytes, decrypt, and write them out.
		int bufferSize = 2048;
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[bufferSize];

		while ((nRead = in.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();		
	}

	//@Test
	public void testEncryptionDecryptionWithOracleFunctions() {
		//TBD - this approach is currently not used, we had issue with "java.sql.SQLException: ORA-28232: invalid input length for obfuscation toolkit
		//ORA-06512: at "SYS.DBMS_OBFUSCATION_TOOLKIT_FFI", line 84
		//ORA-06512: at "SYS.DBMS_OBFUSCATION_TOOLKIT", line 255
		//ORA-06512: at "SBREXT.DECRYPT", line 7"
		//while invoking the database functions
		String key = "1234567890123456";
		String text = "testtest";	//data has to be in a multiples of 8 bytes
		String encryptedText = null;
		byte[] encryptedBytes = null;
		String decryptedText = null;
		byte[] decryptedBytes = null;
		PreparedStatement stmt = null;
        ResultSet rs = null;
        ByteArrayInputStream in = null;
        try {
        	Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			String sql = "select sbrext.encrypt('"+key+"', ?) from dual";
			stmt = conn.prepareStatement(sql);
			stmt.setBytes(1, text.getBytes());
			rs = stmt.executeQuery();
			while(rs.next()) {
//				encryptedText = rs.getString(1);
				in = (ByteArrayInputStream) rs.getBinaryStream(1);
				encryptedBytes = toBytes(in);
			}
			
			String sql2 = "select sbrext.decrypt('"+key+"', ?) from dual";
			stmt = conn.prepareStatement(sql2);
			stmt.setBytes(1, encryptedBytes);
			rs = stmt.executeQuery();
			while(rs.next()) {
//				decryptedText = rs.getString(1);
				in = (ByteArrayInputStream) rs.getBinaryStream(1);
				decryptedBytes = toBytes(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        assertEquals(text.getBytes(), decryptedBytes);
	}

	private void showUserSecurityQuestionList() throws Exception {
		Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
		dao = new PasswordChangeDAO(conn);
		UserSecurityQuestion[] results = dao.findAll();
		if (results.length > 0) {
			for (UserSecurityQuestion e : results) {
				System.out.println("User [" + e.getUaName() + "] updated ["
						+ new Date() + "] question1 [" + e.getQuestion1()
						+ "] answer1 [" + CommonUtil.decode(e.getAnswer1()) + "]" + "] question2 [" + e.getQuestion2()
						+ "] answer2 [" + CommonUtil.decode(e.getAnswer2()) + "]" + "] question3 [" + e.getQuestion3()
						+ "] answer3 [" + CommonUtil.decode(e.getAnswer3()) + "]");
			}
		} else {
			System.out.println("no question");
		}
	}

//	@Test
	public void testUserSecurityQuestionSave() throws Exception {
		UserSecurityQuestion qna = new UserSecurityQuestion();
		qna.setUaName(USER_ID);
		qna.setQuestion1("question 1 from dao");
//		qna.setAnswer1(new String(Hex.encodeHex(x.encrypt((CommonUtil.pad("answer for question 1 of dao", DAO.MAX_ANSWER_LENGTH).getBytes())))));
		qna.setAnswer1(CommonUtil.encode("answer for question 1 of dao"));
		qna.setQuestion2("question 2 from dao");
//		qna.setAnswer2(new String(Hex.encodeHex(x.encrypt((CommonUtil.pad("answer for question 2 of dao", DAO.MAX_ANSWER_LENGTH).getBytes())))));
		qna.setAnswer2(CommonUtil.encode("answer for question 2 of dao"));
		qna.setQuestion3("question 3 from dao");
//		qna.setAnswer3(new String(Hex.encodeHex(x.encrypt((CommonUtil.pad("answer for question 3 of dao", DAO.MAX_ANSWER_LENGTH).getBytes())))));
		qna.setAnswer3(CommonUtil.encode("answer for question 3 of dao"));
		try {
			Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			UserSecurityQuestion qna1 = dao.findByPrimaryKey(USER_ID);
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			if(qna1 == null) {
				dao.insert(qna);
			} else {
				dao.update(USER_ID, qna);
			}
			showUserSecurityQuestionList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
//	@Test
	public void testUserSecurityQuestionUpdate() {
		UserSecurityQuestion qna = new UserSecurityQuestion();
		try {
			Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			qna = dao.findByUaName(USER_ID);
			if(qna == null) {
				throw new Exception("No questions found. Have it been setup?");
			}
			qna.setQuestion2("question 2 from dao *updated*");
//			qna.setAnswer2(new String(Hex.encodeHex(x.encrypt((CommonUtil.pad("answer for question 2 of dao *updated*", DAO.MAX_ANSWER_LENGTH).getBytes())))));
			qna.setAnswer2(CommonUtil.encode("answer for question 2 of dao"));
			//dao.update(qna.getId(), qna);
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			dao.update(qna.getUaName(), qna);
			showUserSecurityQuestionList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private String UPPERCASE_USER_ID = "TEST111";
	private String LOWERCASE_USER_ID = "test111";
	private String PROPERCASE_USER_ID = "Test111";
	private String PASSWORD = "Te$t1235";
	
//	@Test
	public void testUserIDUpperCase() {
		boolean status = false;
		try {
			Connection conn = getConnection(UPPERCASE_USER_ID, PASSWORD);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		assertTrue(status);
	}

//	@Test
	public void testUserIDLowerCase() {
		boolean status = false;
		try {
			Connection conn = getConnection(LOWERCASE_USER_ID, PASSWORD);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		assertTrue(status);
	}

//	@Test
	public void testUserIDProperCaseInSetup() {
		boolean status = false;
		try {
			Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			status = dao.checkValidUser(PROPERCASE_USER_ID);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		assertTrue(status);
	}

//	@Test
	public void testUserIDProperCaseInPasswordChange() {
		UserBean status = null;
		try {
			Connection conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			status = dao.checkValidUser(PROPERCASE_USER_ID, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
//		assertTrue(status.getResult().equals(ResultCode.));
	}

	/**
	 * Mockup method for PasswordChangeDAO.checkValidUser(String username, String password).
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public UserBean checkValidUser(String username, String password) throws Exception {

		if(username == null || password == null) {
			throw new Exception("username and/or password is empty or NULL.");
		}
		
		UserBean userBean = new UserBean(username);
		
		Connection conn = null;
		try {
			conn = getConnection(username, password);
			userBean.setLoggedIn(true);
			userBean.setResult(new Result(ResultCode.NOT_EXPIRED));
	    } catch (Exception ex) {
			Result result = ConnectionUtil.decode(ex);
			
	    	// expired passwords are acceptable as logins
			if (result.getResultCode() == ResultCode.EXPIRED) {
				userBean.setLoggedIn(true);
				System.out.println("considering expired password acceptable login");
			}
			
			userBean.setResult(result);
        }
        finally {
        	if (conn != null) {
        		try {
        			conn.close();
        		} catch (Exception ex) {
        			ex.printStackTrace();
        		}
	        }
	    }
		System.out.println("returning isLoggedIn " + userBean.isLoggedIn());
        return userBean;
	}
	
//	@Test
	public void testUserIDLockedInPasswordChange() throws Exception {
		UserBean userBean = null;
		String userid = "tanj";
		try {
			userBean = checkValidUser(userid,userid);
		} catch (Exception e) {
//			e.printStackTrace();
		} finally {
		}
		Connection conn = null;		
		conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
		dao = new PasswordChangeDAO(conn);
		System.out.println("status is " + userBean.getResult().getResultCode() + " getAccountStatus is " + dao.getAccountStatus(userid));
		assertTrue(userBean.getResult().getResultCode() == ResultCode.LOCKED_OUT);
	}
	
//	@Test
	public void testUserAttemptedCountUpdate() {
		Connection conn = null;
		String userID = "TEST111";
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			UserSecurityQuestion qna = dao.findByPrimaryKey(userID);
			if(qna != null) {
				qna = new UserSecurityQuestion();
				qna.setAttemptedCount(1l);
				conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
				dao = new PasswordChangeDAO(conn);
				dao.update(userID, qna);
			} else {
				throw new Exception(userID + " does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
//	@Test
	public void testJodaTimePeriod() {
		DateTime myBirthDate = new DateTime(1978, 3, 26, 12, 35, 0, 0);
		DateTime now = new DateTime();
		Period period = new Period(myBirthDate, now);

		PeriodFormatter formatter = new PeriodFormatterBuilder()
		    .appendSeconds().appendSuffix(" seconds ago\n")
		    .appendMinutes().appendSuffix(" minutes ago\n")
		    .appendHours().appendSuffix(" hours ago\n")
		    .appendDays().appendSuffix(" days ago\n")
		    .appendMonths().appendSuffix(" months ago\n")
		    .appendYears().appendSuffix(" years ago\n")
		    .printZeroNever()
		    .toFormatter();

		String elapsed = formatter.print(period);
		System.out.println(elapsed);
	}
	
	@Test
	public void testUserAttemptedCountReset() {
		Connection conn = null;
		String userID = "TEST111";
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new PasswordChangeDAO(conn);
			UserSecurityQuestion qna = dao.findByPrimaryKey(userID);
			if(qna != null) {
				if(qna.getDateModified() == null) {
					throw new Exception("Security questions date modified is NULL or empty.");
				}
				DateTime now = new DateTime();
				System.out.println("last modified is " + qna.getDateModified());
				Period period = new Period(new DateTime(qna.getDateModified()), now);
				if(period.getHours() > 1) {
					qna.setAttemptedCount(0l);
					System.out.println("Over 1 hour, answer limit count reset (" + period.getMinutes() + " minutes has passed).");
				} else {
					System.out.println("Not over 1 hour yet, nothing is done (" + period.getMinutes() + " minutes has passed).");
				}
				//qna.setDateModified(new Timestamp(DateTimeUtils.currentTimeMillis()));
				conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
				dao = new PasswordChangeDAO(conn);
				dao.update(userID, qna);
			} else {
				throw new Exception(userID + " does not exist.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	
/*
select ua_name, attempted_count from sbrext.user_security_questions

select * from sys.cadsr_users where lower(username) like 'test111'

select * from sys.cadsr_users where lower(username) = 'test111'

select * from sbrext.user_security_questions

alter user test111 ACCOUNT LOCK PASSWORD EXPIRE

alter user test111 PASSWORD EXPIRE

alter user test111 ACCOUNT LOCK

INSERT INTO "SBREXT"."USER_SECURITY_QUESTIONS" (ua_name, QUESTION1, ANSWER1, QUESTION2, ANSWER2, QUESTION3, ANSWER3, ATTEMPTED_COUNT ) VALUES  ( 'test111', 'q1', 'a1', 'q2', 'a2', 'q3', 'a3',  -1)

select * from sys.dba_profiles where lower(profile) = 'cadsr_user'

use the admin tool to create the user

alter user UATDEV1 profile "cadsr_user"

select * from sbr.user_accounts_view where ua_name = 'UATDEV1'
UA_NAME	DESCRIPTION	DATE_CREATED	CREATED_BY	DATE_MODIFIED	MODIFIED_BY	NAME	ORG_IDSEQ	TITLE	PHONE_NUMBER	FAX_NUMBER	TELEX_NUMBER	MAIL_ADDRESS	ELECTRONIC_MAIL_ADDRESS	DER_ADMIN_IND	ENABLED_IND	ALERT_IND	
UATDEV1	<NULL>	2012-08-27 00:00:00.0	test111	2012-08-27 00:00:00.0	test111	UAT DEV TEST 1 USER	E66B9F0E-BE4D-1A5B-E034-0003BA3F9857	<NULL>	<NULL>	<NULL>	<NULL>	<NULL>	<NULL>	No	Yes	Yes	

select * from sys.cadsr_users where username = 'UATDEV1'
USERNAME	USER_ID	ACCOUNT_STATUS	LOCK_DATE	EXPIRY_DATE	DEFAULT_TABLESPACE	TEMPORARY_TABLESPACE	CREATED	PROFILE	INITIAL_RSRC_CONSUMER_GROUP	EXTERNAL_NAME	PTIME	
UATDEV1	276	OPEN	<NULL>	2012-10-26 11:44:35.0	USERS	TEMP	2012-08-27 11:44:35.0	cadsr_user	DEFAULT_CONSUMER_GROUP	<NULL>	2012-08-27 11:44:35.0	

select * from sbrext.user_security_questions where ua_name = 'UATDEV1'
*/
	
	/*
	 * --asign profile to an existing user alter user SCOTT profile "cadsr_user"
	 * / --expire the users password to force the user to change it: alter user
	 * SCOTT password expire /
	 */
}