package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.*;

import org.apache.commons.codec.binary.Hex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPasswordNotification {

	private static DataSource datasource = null;
	private static AbstractDao dao;
	public static String ADMIN_ID = "cadsrpasswordchange";
	public static String ADMIN_PASSWORD = "cadsrpasswordchange";
	public static String USER_ID = "TEST111";	//this user has to exist, otherwise test will fail

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
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

//	@Test
	public void testUserListWithPasswordExpiring() {
		Connection conn = null;
		List u = null;
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new DAO(conn);
			u = dao.getPasswordExpiringList(60);
			showUserList(u);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	private void showUserList(List<User> results) throws Exception {
		if (results.size() > 0) {
			for (User e : results) {
				if(e != null) {
				System.out.println("User [" + e.getUsername() + "] attempted [" + e.getAttemptedCount() + "] type [" + e.getProcessingType() + "] password updated ["
						+ e.getPasswordChangedDate() + "] email [" + e.getElectronicMailAddress()
						+ "] expiry date [" + e.getExpiryDate() + "]");
				}
			}
		} else {
			System.out.println("no user");
		}
	}

	@Test
	public void testUserStatusUpdateWithPasswordExpiring() {
		Connection conn = null;
		List<User>l = new ArrayList();
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new DAO(conn);
			User user = new User();
			user.setUsername(USER_ID);
			user.setAttemptedCount(1);
			user.setProcessingType(user.getProcessingType()!=null?user.getProcessingType():" " + "14");
			user.setDeliveryStatus(Constants.SUCCESS);
			user.setDateModified(new java.sql.Date(new Date().getTime()));
			dao.updateQueue(user);
			l.add(user);
			showUserList(l);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	@Test
	public void testLoadUserQueueForPasswordExpiring() {
		Connection conn = null;
		List<User>l = new ArrayList();
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new DAO(conn);
			User user = new User();
			user.setUsername(USER_ID);
			user = dao.loadQueue(user);
			if(user != null) {
				l.add(user);
				showUserList(l);
			} else {
				System.out.println("User " + USER_ID + " does not exists in queue");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
//	@Test
	public void testRemoveUserQueueForPasswordExpiring() {
		Connection conn = null;
		List<User>l = new ArrayList();
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new DAO(conn);
			User user = new User();
			user.setUsername(USER_ID);
			dao.removeQueue(user);
			l.add(user);
			showUserList(l);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

/*
select username, expiry_date, account_status from dba_users where expiry_date < sysdate+2 and expiry_date < sysdate+60 and account_status IN ( 'OPEN', 'EXPIRED(GRACE)' ) order by account_status, expiry_date, username

SELECT mail_address, username, account_status, expiry_date, lock_date FROM dba_users a, user_accounts b WHERE a.username = b.ua_name and EXPIRY_DATE BETWEEN SYSDATE AND SYSDATE+60;

desc user_accounts;

select * from user_accounts-- where mail_address is null;

create profile "cadsr_test_2_days" limit
password_life_time 2
password_grace_time 0
password_reuse_max 24
password_reuse_time 1
failed_login_attempts 6
password_lock_time 60/1440
password_verify_function password_verify_casdr_user
/
alter user TEST112 profile "cadsr_test_2_days"
/
*/
}