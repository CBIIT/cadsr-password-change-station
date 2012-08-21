package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.CommonUtil;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.ConnectionUtil;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.NotifyPassword;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordNotify;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordNotifyDAO;
import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPasswordNotification {

	private static DataSource datasource = null;
	Connection conn = null;
	private static PasswordNotify dao;
	public String ADMIN_ID = "cadsrpasswordchange";
	public String ADMIN_PASSWORD = "cadsrpasswordchange";
	public String USER_ID = "TEST111";	//this user has to exist, otherwise test will fail

	@Before
	public void setUp() {
		try {
			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dao = new PasswordNotifyDAO(conn);
	}

	@After
	public void tearDown() {
	}
	
	public Connection getConnection(String username, String password)
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
		List u = null;
		try {
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
						+ "] expiry date [" + e.getExpiryDate() + "] password changed date [" + e.getPasswordChangedDate() + "]");
				}
			}
		} else {
			System.out.println("no user");
		}
	}

//	@Test
	public void testUserStatusUpdateWithPasswordExpiring() {
		Connection conn = null;
		List<User>l = new ArrayList();
		try {
			User user = new User();
			user.setUsername(USER_ID);
			user.setAttemptedCount(1);
			user.setProcessingType("14");
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
	
//	@Test
	public void testLoadUserQueueForPasswordExpiring() {
		Connection conn = null;
		List<User>l = new ArrayList();
		try {
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

//	@Test
	public void testLastPasswordChangedDateInDaysFromNow() {
		Connection conn = null;
		List<User>l = new ArrayList();
		try {
			User user = new User();
			user.setUsername(USER_ID);
//			user.setUsername("TEST113");
			user = dao.loadQueue(user);
			l.add(user);
			showUserList(l);
			Date startDate = user.getPasswordChangedDate();
			if(startDate == null) {
				System.out.println("The password change has never been changed or password change date is empty");
			} else {
				Calendar start = Calendar.getInstance();
				start.setTime(startDate);
//				System.out.println("It has been " + CommonUtil.daysBetween(start, Calendar.getInstance()) + " day(s) since the password change");
				System.out.println("It has been " + CommonUtil.calculateDays(startDate, new Date()) + " day(s) since the password change");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
//	@Test
	public void testJodaTimeChange() {
		long millis = 86400000;	//a day
	    DateTimeUtils.setCurrentMillisOffset(millis);
	    long realToday = System.currentTimeMillis();
	    long fakeToday = DateTimeUtils.currentTimeMillis();

	    System.out.println("Today is " + new Date(realToday) + " Fake today is " + new Date(fakeToday));
	    assertTrue((realToday - fakeToday) < 0);
	}

	/**
	 * 
	 * @param name
	 * @param email
	 * @param daysLeft
	 * @param howManyDaysAgo 0 means changed today
	 * @return
	 * @throws Exception 
	 */
	private User getExpiredUser(String name, String email, int daysLeft, int howManyDaysAgo) throws Exception {
		if(howManyDaysAgo < 0) {
			throw new Exception("howManyDaysAgo can not be negative.");
		}
		long millis = 86400000;	//a day
		User user = null;
		//expired in passed in days
		user = new User();
		user.setElectronicMailAddress(email);
		user.setUsername(name);
		user.setAccountStatus("OPEN");
	    DateTimeUtils.setCurrentMillisOffset(millis*daysLeft);	//expired in the passed in date
		user.setExpiryDate(new java.sql.Date(DateTimeUtils.currentTimeMillis()));
		user.setLockDate(null);	//not locked
	    DateTimeUtils.setCurrentMillisOffset(-millis*(howManyDaysAgo));	//changed since a while back
		user.setPasswordChangedDate(new java.sql.Date(DateTimeUtils.currentTimeMillis()));
		//reset the time back
	    DateTimeUtils.setCurrentMillisOffset(0);
		user.setDateModified(new java.sql.Date(DateTimeUtils.currentTimeMillis()));
		System.out.println("getExpiredUser: mail_address '" + user.getElectronicMailAddress() + "', username '" + user.getUsername() + "' expiry_date '" + user.getExpiryDate() + "'");
		
		return user;
	}

	/**
	 * Mockup method for PasswordNotify.getPasswordExpiringList().
	 * 
	 * Days has to be less than 60 days. Otherwise, change cutOffDay accordingly.
	 * @param daysLeft
	 * @return
	 */
	public List<User> getPasswordExpiringList(int daysLeft) {
		String value = null;
		List arr = new ArrayList();
		try {
//			if(daysLeft > 30) {
//				arr.add(getExpiredUser("user1000", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user2000", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user3000", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user4000", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user5000", "k40733@rtrtr.com", daysLeft, 61));
//			} else
//			if(daysLeft > 20) {
//				arr.add(getExpiredUser("user100", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user200", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user300", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user400", "k40733@rtrtr.com", daysLeft, 61));
//			} else
			if(daysLeft > 10) {
				arr.add(getExpiredUser("user10", "k40733@rtrtr.com", daysLeft, 61));
				arr.add(getExpiredUser("user20", "k40733@rtrtr.com", daysLeft, 61));
				arr.add(getExpiredUser("user30", "k40733@rtrtr.com", 7, 11));	//abnomoly - password changed 11 days back
				arr.add(getExpiredUser("user30", "k40733@rtrtr.com", 5, 3));	//abnomoly - password changed 3 days back
				arr.add(getExpiredUser("user40", "k40733@rtrtr.com", 90, 0));	//abnomoly - password changed just today
			} 
//			else
//			if(daysLeft > 3) {
//				arr.add(getExpiredUser("user1", "k40733@rtrtr.com", daysLeft, 61));
//				arr.add(getExpiredUser("user2", "k40733@rtrtr.com", 1, 0));	//abnomoly - password changed just today
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

       return arr;
	}

	/**
	 * Mockup method for NotifyPassword.saveIntoQueue().
	 */
	private void saveIntoQueue(User user, int daysLeft) throws Exception {
	}

	/**
	 * Mockup method for NotifyPassword.sendEmail().
	 */
	private boolean sendEmail(User user, int daysLeft) throws Exception {
		return true;
	}

	/**
	 * Mockup method for NotifyPassword.updateStatus().
	 */
	private void updateStatus(User user, String status, int daysLeft) throws Exception {
		int currentCount = user.getAttemptedCount();
		user.setAttemptedCount(currentCount++);
		user.setProcessingType(String.valueOf(daysLeft));
		user.setDeliveryStatus(status);
		user.setDateModified(new java.sql.Date(new Date(DateTimeUtils.currentTimeMillis()).getTime()));
//		System.out.println("updateStatus: " + user + " days left = " + daysLeft);
	}

	@Test
	public void testNotifications() throws Exception {
		List<User> recipients = null;
		NotifyPassword n = new NotifyPassword(conn);
		int days;
		int size = 3; //change this accordingly if you have more than 3 types of notifications (only in this test, as we bypass config.xml)
		int index;

		days = 14;
		index = 1;
		recipients = getPasswordExpiringList(days);
		for (User u : recipients) {
			if(u != null) {
				System.out.println("testNotifications: Processing user [" + u.getUsername() + "] attempted [" + u.getAttemptedCount() + "] type [" + u.getProcessingType() + "] password updated ["
						+ u.getPasswordChangedDate() + "] email [" + u.getElectronicMailAddress()
						+ "] expiry date [" + u.getExpiryDate() + "]");
				if(n.isNotificationValid(u, days, size, index)) {
					saveIntoQueue(u, days);
					if(sendEmail(u, days)) {
						updateStatus(u, Constants.SUCCESS, days);
					} else {
						updateStatus(u, Constants.FAILED, days);
					}
				} else {
//					System.out.println("testNotifications: isNotificationValid is not valid, notification aborted for user: " + u.getUsername());
					updateStatus(u, null, days);
				}
			}
		}

		int count = 1;
		for (User u : recipients) {
			if(count >0 && count < 3) {
				assertEquals(Constants.SUCCESS, u.getDeliveryStatus());
			} else {
//				assertEquals(null, u.getDeliveryStatus());
			}
			System.out.println("testNotifications: " + u.getDeliveryStatus() + " user " + u);
			count++;
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
alter user TEST113 profile "cadsr_test_2_days"
/
alter user TEST113 identified by Te$t1235 password expire;
/
*/
}