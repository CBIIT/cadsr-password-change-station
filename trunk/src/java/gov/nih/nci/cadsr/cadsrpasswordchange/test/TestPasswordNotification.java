package gov.nih.nci.cadsr.cadsrpasswordchange.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.CommonUtil;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.ConnectionUtil;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.EmailHelper;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.EmailSending;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.NotifyPassword;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordNotify;
import gov.nih.nci.cadsr.cadsrpasswordchange.core.PasswordNotifyDAO;
import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPasswordNotification {

	private boolean fromDB = /*false;*/	true; //= real; //false = simulated	
	private static DataSource datasource = null;
	Connection conn = null;
	private static PasswordNotify dao;
	public String ADMIN_ID = "cadsrpasswordchange";
	public String ADMIN_PASSWORD = "cadsrpasswordchange";
//	public String ADMIN_PASSWORD = "";
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
		String dbserver = "137.187.181.4"; String dbname = "DSRDEV"; //dev
//		String dbserver = "137.187.181.89"; String dbname = "DSRQA";
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
		String _processingNotificationDays = "14,7,4";
		List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator
		try {
			u = dao.getPasswordExpiringList(60, 3, 3, types);
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
		List<User>l = new ArrayList();
		try {
			User user = new User();
			user.setUsername(USER_ID);
			user.setAttemptedCount(1);
			user.setProcessingType("14");
			user.setDeliveryStatus(Constants.SUCCESS);
			user.setDateModified(new Timestamp(DateTimeUtils.currentTimeMillis()));
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
		user.setDateModified(new Timestamp(DateTimeUtils.currentTimeMillis()));
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
		System.out.println("Queud for user " + user.getUsername() + " type " + daysLeft);
	}

	/**
	 * Mockup method for NotifyPassword.sendEmail().
	 */
	private boolean sendEmail(User user, int daysLeft) throws Exception {
		System.out.println("NotifyPassword.sendEmail entered ...");
        setUp();
		String adminEmailAddress = dao.getAdminEmailAddress();
		System.out.println("NotifyPassword.sendEmail adminEmailAddress [" + adminEmailAddress + "]");
        setUp();
		String emailSubject = EmailHelper.handleDaysToken(dao.getEmailSubject(), daysLeft);
		System.out.println("NotifyPassword.sendEmail emailSubject [" + emailSubject + "]");
        setUp();
		String emailBody = EmailHelper.handleDaysToken(dao.getEmailBody(), daysLeft);
		System.out.println("NotifyPassword.sendEmail emailBody [" + emailBody + "]");
		emailBody = EmailHelper.handleUserIDToken(emailBody, user);		//CADSRPASSW-62
		System.out.println("sendEmail:user id = [" + user.getUsername() + "] body processed = [" + emailBody + "]");
		String emailAddress = user.getElectronicMailAddress();
		System.out.println("NotifyPassword.sendEmail emailAddress [" + emailAddress + "]");
        setUp();
		String host = dao.getHostName();
		System.out.println("NotifyPassword.sendEmail host [" + host + "]");
        setUp();
		String port = dao.getHostPort();
		System.out.println("NotifyPassword.sendEmail port [" + port + "]");
		EmailSending ms = new EmailSending(adminEmailAddress, "dummy", host, port, emailAddress, emailSubject, emailBody);
		System.out.println("NotifyPassword.sendEmail sending email ...");		
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
		user.setDateModified(new Timestamp(DateTimeUtils.currentTimeMillis()));
		System.out.println("updateStatus: " + user + " status " + status + " days left = " + daysLeft);
	}

//	@Test
	public void testNotifications() throws Exception {
		List<User> recipients = null;
		NotifyPassword n = new NotifyPassword(conn);
		int days;
		int size = 3; //change this accordingly if you have more than 3 types of notifications (only in this test, as we bypass config.xml)
		int index;
		String _processingNotificationDays = "14,7,4";
		List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator

		days = 14;
		index = 1;
		if(!fromDB) {
			recipients = getPasswordExpiringList(days);
		} else {
			recipients = dao.getPasswordExpiringList(days, size, index, types);
			showUserList(recipients);
		}
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
					System.out.println("testNotifications: isNotificationValid is not valid, notification aborted for user: " + u.getUsername());
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

	private boolean send(User user, int daysLeft) throws Exception {
		String adminEmailAddress = dao.getAdminEmailAddress();
		setUp();
		String emailSubject = EmailHelper.handleDaysToken(dao.getEmailSubject(), daysLeft);
		setUp();
		String emailBody = EmailHelper.handleDaysToken(dao.getEmailBody(), daysLeft);
		String emailAddress = user.getElectronicMailAddress();
		setUp();
		String host = dao.getHostName();
		setUp();
		String port = dao.getHostPort();
		EmailSending ms = new EmailSending(adminEmailAddress, "dummy", host, port, emailAddress, emailSubject, emailBody);
		return ms.send();
	}

//	@Test
	public void testEmailSending() throws Exception {
		int daysLeft = 7;
		User u = getExpiredUser("user10", "james.tan@nih.gov", daysLeft, 61);
		send(u, daysLeft);
	}

	private boolean isChangedRecently(int daysLeft, long daysSincePasswordChange) {
		boolean ret = false;
		if(daysSincePasswordChange <= daysLeft) {
			ret = true;
		}
		return ret;
	}

	public boolean isNotificationValid(User user, int daysLeft, int totalNotificationTypes, int currentNotificationIndex) throws Exception {
		boolean ret = false;
		boolean daysCondition = false;
		boolean deliveryStatus = false;
		String processedType = null;
		int attempted = -1;
		String status = null;
		long daysSincePasswordChange = -1;

		java.sql.Date passwordChangedDate = user.getPasswordChangedDate();
		if(passwordChangedDate == null) {
			throw new Exception("Not able to determine what is the password changed date or password change date is empty (from sys.cadsr_users view).");
		}
		daysSincePasswordChange = CommonUtil.calculateDays(passwordChangedDate, new Date(DateTimeUtils.currentTimeMillis()));

		if(daysSincePasswordChange != 0 && !isChangedRecently(daysLeft, daysSincePasswordChange)) {	//not recently changed (today)
			if(totalNotificationTypes != currentNotificationIndex) {
					//not the last type - send only once
					if(user.getDeliveryStatus() == null && user.getProcessingType() == null) {
						//has not been processed at all
						ret = true;
					} else 
					if(user.getDeliveryStatus().equals(Constants.FAILED)) {
						//processed but failed
						ret = true;
					} else 
					if(!user.getProcessingType().equals(String.valueOf(daysLeft))) {
						//it is different type of notification
						ret = true;
					}
			} else {
				if(daysLeft != Constants.DEACTIVATED_VALUE) {
					//the last notification type
					Calendar start = Calendar.getInstance();
					start.setTime(passwordChangedDate);
					if(daysSincePasswordChange >= 1) {
						ret = true;
					}
					System.out.println("isNotificationValid: It has been " + daysSincePasswordChange + " day(s) since the password change, send flag is " + ret);
				} else {
					System.out.println("daily notification is disabled");// (types = '"+ _processingNotificationDays + "').");
				}
			}
		} else 
		if(daysSincePasswordChange == 0 || isChangedRecently(daysLeft, daysSincePasswordChange)) {	//reset everything if changed today OR if changed after the last check point
//			conn = getConnection(ADMIN_ID, ADMIN_PASSWORD);		
//			dao = new PasswordNotifyDAO(conn);
//			dao.removeQueue(user);
			System.out.println("*** RESET BYPASSED AS IT IS JUST A JUNIT TEST!!! ****");		
		}
		
		return ret;
	}

	private void process(int days, int size, int index) throws Exception {
		System.out.println("\nNotifyPassword.process entered ...");
		String _processingNotificationDays = "14,7,4";
		List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator		
		List<User> recipients = null;
		setUp();
		recipients = dao.getPasswordExpiringList(days, size, index, types);
		
		if (recipients != null && recipients.size() > 0) {
			for (User u : recipients) {
				if(u != null) {
					System.out.println("Processing user [" + u.getUsername() + "] attempted [" + u.getAttemptedCount() + "] type [" + u.getProcessingType() + "] password updated ["
							+ u.getPasswordChangedDate() + "] email [" + u.getElectronicMailAddress()
							+ "] expiry date [" + u.getExpiryDate() + "]");
					if(isNotificationValid(u, days, size, index)) {
						saveIntoQueue(u, days);
						if(sendEmail(u, days)) {
							updateStatus(u, Constants.SUCCESS, days);
							System.out.println("*** SUCCESS for " + u.getUsername() + "***");
						} else {
							updateStatus(u, Constants.FAILED, days);
							System.out.println("*** FAILED for " + u.getUsername() + "***");
						}
					} else {
						System.out.println(">>>>>> isNotificationValid is not valid, notification aborted for user: " + u.getUsername());
						updateStatus(u, null, days);
						System.out.println("status date updated for user " + u);
					}
				}
			}
		} else {
			System.out.println("--- No user for notification of " + days + " found! ---");
		}
		
		System.out.println("NotifyPassword.process done.");		
	}
	
	/**
	 * Mockup method for NotifyPassword.doAll(String propFile_).
	 */
	public void doAll(String propFile_) throws Exception {
		dao = new PasswordNotifyDAO(conn);
		String _processingNotificationDays = dao.getProcessTypes();
		if(_processingNotificationDays != null) {
			try {
				List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator
				int size = types.size();
				int index = 1;
				for (String t : types) {
					process(Integer.valueOf(t).intValue(), size, index);
					index++;
					System.out.println("Notification type " + t + " processed.");
				}
				System.out.println(".doAll.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Missing processing types. Please check EMAIL.NOTIFY_TYPE property value in the table sbrext.tool_options_view_ext.");
		}
	}

	@Test
	public void testMainLoop() throws Exception {
//		doAll(null);
		setUp();
		NotifyPassword np = new NotifyPassword(conn);
		np.doAll("C:\\Workspaces\\demo\\cadsrpasswordchange\\dist\\bin\\config.xml");
	}

//	@Test
	public void testEmailWithUserID() throws Exception {
		List<User> recipients = null;
		NotifyPassword n = new NotifyPassword(conn);
		int days;
		int size = 3; //change this accordingly if you have more than 3 types of notifications (only in this test, as we bypass config.xml)
		int index;
		String _processingNotificationDays = "14,7,4";
		List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator

		days = 14;
		index = 1;
		if(!fromDB) {
			recipients = getPasswordExpiringList(days);
		} else {
			recipients = dao.getPasswordExpiringList(days, 3, 3, types);
			
			showUserList(recipients);
		}
		for (User u : recipients) {
			if(u != null) {
				System.out.println("testNotifications: Processing user [" + u.getUsername() + "] attempted [" + u.getAttemptedCount() + "] type [" + u.getProcessingType() + "] password updated ["
						+ u.getPasswordChangedDate() + "] email [" + u.getElectronicMailAddress()
						+ "] expiry date [" + u.getExpiryDate() + "]");
				sendEmail(u, days);
				System.out.println("NotifyPassword.sendEmail email sent");
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
update SBREXT.PASSWORD_NOTIFICATION set date_modified = sysdate, attempted_count = -2, processing_type = 1, delivery_status = 'test' where ua_name = 'TANJ'

update sbrext.tool_options_view_ext set value = 'james.tan@nih.gov' where Tool_name = 'PasswordChangeStation' and Property = 'EMAIL.ADDR'

update sbrext.tool_options_view_ext set value = '14' where Tool_name = 'PasswordChangeStation' and Property = 'EMAIL.NOTIFY_TYPE'

delete from SBREXT.PASSWORD_NOTIFICATION

select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property like 'EMAIL.NOTIFY_TYPE'

select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property like '%EMAIL%'

select * from sys.dba_profiles where profile like 'cadsr_user_test11'

select
			   a.USERNAME, 
			   b.ATTEMPTED_COUNT, 
			   b.DATE_MODIFIED, 
			   b.DELIVERY_STATUS,
			   b.PROCESSING_TYPE,
			   a.created,
			   a.profile,
			   a.EXPIRY_DATE, 
			   a.PTIME,  
			   a.ACCOUNT_STATUS, 
			   c.electronic_mail_address, 
			   a.LOCK_DATE
			 from 
			 SYS.CADSR_USERS a, SBREXT.PASSWORD_NOTIFICATION b, sbr.user_accounts_view c 
			 where a.username = b.UA_NAME(+) and a.username = c.UA_NAME
--and a.EXPIRY_DATE BETWEEN SYSDATE AND SYSDATE+14
and a.username in ('PW14','PW7_','PW4_')
order by a.EXPIRY_DATE desc

select * from 
--SYS.CADSR_USERS
--SBREXT.PASSWORD_NOTIFICATION
sbr.user_accounts_view
where 
--username = 'PW11'
UA_NAME = 'PW11'

create profile "cadsr_user_test11" limit
 password_life_time 1
 password_grace_time 0
 password_reuse_max 24
 password_reuse_time 1
 failed_login_attempts 6
 password_lock_time 60/1440
 password_verify_function password_verify_casdr_user

update sbrext.tool_options_view_ext set value = 'Your password is about to expire in ${daysLeft} days. Please login to Password Change Station or call NCI Helpdesk to change your password.'
where Tool_name = 'PasswordChangeStation' and Property = 'EMAIL.INTRO'

update sbrext.tool_options_view_ext set value = 'caDSR Password Expiration Notice (in ${daysLeft} day(s))'
where Tool_name = 'PasswordChangeStation' and Property = 'EMAIL.SUBJECT'

select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'SENTINEL' and Property like '%EMAIL%'

select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property like '%EMAIL%'

select tool_name, property, VALUE from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property like 'EMAIL.NOTIFY_TYPE'

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
	
/*
create profile "cadsr_user_test14" limit
 password_life_time 14
 password_grace_time 0
 password_reuse_max 24
 password_reuse_time 1
 failed_login_attempts 6
 password_lock_time 60/1440
 password_verify_function password_verify_casdr_user
  
alter user PW14 profile "cadsr_user_test14"

alter user PW7 profile "cadsr_user_test7"

alter user PW4 profile "cadsr_user_test4"

select
			   a.USERNAME, 
			   b.ATTEMPTED_COUNT, 
			   b.DATE_MODIFIED, 
			   b.DELIVERY_STATUS,
			   b.PROCESSING_TYPE,
			   a.created,
			   a.profile,
			   a.EXPIRY_DATE, 
			   a.PTIME,  
			   a.ACCOUNT_STATUS, 
			   c.electronic_mail_address, 
			   a.LOCK_DATE
			 from 
			 SYS.CADSR_USERS a, SBREXT.PASSWORD_NOTIFICATION b, sbr.user_accounts_view c 
			 where a.username = b.UA_NAME(+) and a.username = c.UA_NAME
and a.EXPIRY_DATE BETWEEN SYSDATE+7 AND SYSDATE+14
--and a.EXPIRY_DATE BETWEEN SYSDATE+4 AND SYSDATE+7
--and a.EXPIRY_DATE BETWEEN SYSDATE+0 AND SYSDATE+4
order by a.EXPIRY_DATE desc
order by 
a.EXPIRY_DATE,
a.PTIME  asc
 */
}
