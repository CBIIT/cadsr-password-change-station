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
	public static String ADMIN_ID = "sbrext";
	public static String ADMIN_PASSWORD = "jjuser";
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
	
	@Test
	public void testUserListWithPasswordExpiring() {
		Connection conn = null;
		List u = null;
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
			dao = new DAO(conn);
			u = dao.getPasswordExpiringList(60);
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
	 */
}