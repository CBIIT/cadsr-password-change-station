package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;

public class NotifyPassword {

	private static org.apache.log4j.Logger _logger = org.apache.log4j.Logger.getLogger(NotifyPassword.class);
	private static PasswordNotify dao;
	public static String emailSubject;
	public static String emailBody;
    private Properties          _propList;
    private Connection          _conn;
    private String              _dsurl;
    private String              _user;
    private String              _pswd;
    private String              _processingNotificationDays;
    
    public NotifyPassword(Connection conn) {
    	this._conn = conn;
    }

    /**
     * Open a single simple connection to the database. No pooling is necessary.
     *
     * @param _dsurl
     *        The Oracle TNSNAME entry describing the database location.
     * @param user_
     *        The ORACLE user id.
     * @param pswd_
     *        The password which must match 'user_'.
     * @return The database error code.
     */
    private int open() throws Exception
    {
        // If we already have a connection, don't bother.
        if (_conn != null) {
            //return 0;
        	_conn.close();	//CADSRPASSW-56
        }

        try
        {
//            OracleDataSource ods = new OracleDataSource();
//            String parts[] = _dsurl.split("[:]");
//            ods.setDriverType("thin");
            _logger.info("NotifyPassword v1.0 build 16.91");
//            String connString=_dsurl;
//            ods.setURL(connString);
//            ods.setUser(_user);
//            ods.setPassword(_pswd);
//            _logger.info("NotifyPassword:open _dsurl[" + _dsurl + "] via _user["+ _user + "]");
//            _conn = ods.getConnection(_user, _pswd);

          _logger.debug("got connection using direct jdbc url [" + _dsurl + "]");
          Properties info = new Properties();
          info.put( "user", _user );
          _logger.debug("with user id [" + _user + "]");
          info.put( "password", _pswd );
          Class.forName("oracle.jdbc.driver.OracleDriver");
          _conn = DriverManager.getConnection(_dsurl, info);

            _logger.info("connected to the database");
            _conn.setAutoCommit(true);
            return 0;
        }
        catch (SQLException ex)
        {
            throw ex;
        }
    }

    /**
     * Load the properties from the XML file specified.
     *
     * @param propFile_ the properties file.
     */
    private void loadProp(String propFile_) throws Exception
    {
        _propList = new Properties();

        _logger.debug("\n\nLoading properties...\n\n");

        try
        {
            FileInputStream in = new FileInputStream(propFile_);
            _propList.loadFromXML(in);
            in.close();
        }
        catch (Exception ex)
        {
            throw ex;
        }

        _dsurl = _propList.getProperty(Constants._DSURL);
        if (_dsurl == null)
            _logger.error("Missing " + Constants._DSURL + " connection string in " + propFile_);

        _user = _propList.getProperty(Constants._DSUSER);
        if (_user == null)
            _logger.error("Missing " + Constants._DSUSER + " in " + propFile_);

        _pswd = _propList.getProperty(Constants._DSPSWD);
        if (_pswd == null)
            _logger.error("Missing " + Constants._DSPSWD + " in " + propFile_);

    }
    
	public void doAll(String propFile_) throws Exception {
		_logger.debug("NotifyPassword.doAll entered ...");
        loadProp(propFile_);
        open();
		dao = new PasswordNotifyDAO(_conn);
		_processingNotificationDays = dao.getProcessTypes();
		if(_processingNotificationDays != null) {
			try {
				List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator
				int size = types.size();
				int index = 1;
				for (String t : types) {
					process(Integer.valueOf(t).intValue(), size, index);
					index++;
					_logger.debug("Notification type " + t + " processed.");
				}
				_logger.debug(".doAll.");
			} catch (Exception e) {
				//e.printStackTrace();
				_logger.error(CommonUtil.toString(e));
			}
		} else {
			_logger.error("Missing processing types. Please check EMAIL.NOTIFY_TYPE property value in the table sbrext.tool_options_view_ext.");
		}
		
        if (_conn != null)
        {
            _conn.close();
            _conn = null;
        }
		_logger.debug("NotifyPassword.doAll done.");
	}

	private void process(int days, int size, int index) throws Exception {
		_logger.debug("\nNotifyPassword.process entered ...");
		
		List<User> recipients = null;
        open();
		dao = new PasswordNotifyDAO(_conn);
		recipients = dao.getPasswordExpiringList(days);
		if (recipients != null && recipients.size() > 0) {
			for (User u : recipients) {
				if(u != null) {
					_logger.info("Processing user [" + u.getUsername() + "] attempted [" + u.getAttemptedCount() + "] type [" + u.getProcessingType() + "] password updated ["
							+ u.getPasswordChangedDate() + "] email [" + u.getElectronicMailAddress()
							+ "] expiry date [" + u.getExpiryDate() + "]");
					if(isNotificationValid(u, days, size, index)) {
						_logger.info("NotifyPassword.process saving into queue for user: " + u.getUsername());
						saveIntoQueue(u, days);
						_logger.debug("NotifyPassword.process queued email for user: " + u.getUsername() + " under type " + days);
						_logger.info("NotifyPassword.process sending email for user: " + u.getUsername() + " under type " + days);
						if(sendEmail(u, days)) {
							_logger.debug("NotifyPassword.sendEmail *** DONE ***");
							_logger.info("NotifyPassword.process updating success for user: " + u.getUsername() + " under type " + days);
							updateStatus(u, Constants.SUCCESS + String.valueOf(days), days);
							_logger.debug("NotifyPassword.process updated success for user: " + u.getUsername() + " under type " + days);
						} else {
							_logger.info("NotifyPassword.process updating failure for user: " + u.getUsername() + " under type " + days);
							updateStatus(u, Constants.FAILED + String.valueOf(days), days);
							_logger.debug("NotifyPassword.process updated failure for user: " + u.getUsername() + " under type " + days);
						}
					} else {
						_logger.info("isNotificationValid is not valid, notification aborted for user: " + u.getUsername());
						updateStatus(u, null, days);
						_logger.debug("status date updated for user " + u);
					}
				}
			}
		} else {
			_logger.info("------- No user for notification of " + days + " found ------- ");
		}
		
		_logger.debug("NotifyPassword.process done.\n\n");
	}

	/**
	 * Add or update the queue with the outgoing email.
	 */
	private void saveIntoQueue(User user, int daysLeft) throws Exception {
		_logger.debug("saveIntoQueue entered");
		_logger.info("saveIntoQueue:user [" + user + "] type " + daysLeft);
        open();
		dao = new PasswordNotifyDAO(_conn);
		user.setProcessingType(String.valueOf(daysLeft));
		_logger.info("saveIntoQueue:type " + daysLeft + " set");
        open();
		dao = new PasswordNotifyDAO(_conn);
		dao.updateQueue(user);
		_logger.debug("saveIntoQueue done");
	}
	
	private boolean sendEmail1(User user, int daysLeft) throws Exception {
		emailSubject = "caDSR Password Expiration Notice";
		emailBody = "Your password is about to expire in " + daysLeft + ". Please login to Password Change Station or call NCI Helpdesk to change your password.";
		String emailAddress = user.getElectronicMailAddress();
		EmailSending ms = new EmailSending("do-not-reply@nih.gov", "uyeiy3wjukhkuqhwgiw7t1f2863f", "mailfwd.nih.gov", "25", emailAddress, emailSubject, emailBody);
		_logger.info("sendEmail:send to test account is true");
		_logger.debug("sendEmail:sent");
		return ms.send();
	}

	private boolean sendEmail(User user, int daysLeft) throws Exception {
		boolean retVal = false;

		_logger.debug("NotifyPassword.sendEmail entered ...");
        open();
		dao = new PasswordNotifyDAO(_conn);
		String adminEmailAddress = dao.getAdminEmailAddress();
		_logger.debug("NotifyPassword.sendEmail adminEmailAddress [" + adminEmailAddress + "]");
        open();
		dao = new PasswordNotifyDAO(_conn);
		String emailSubject = EmailHelper.handleDaysToken(dao.getEmailSubject(), daysLeft);
		_logger.debug("NotifyPassword.sendEmail emailSubject [" + emailSubject + "]");
        open();
		dao = new PasswordNotifyDAO(_conn);
		String emailBody = EmailHelper.handleDaysToken(dao.getEmailBody(), daysLeft);
		_logger.debug("NotifyPassword.sendEmail emailBody [" + emailBody + "]");
		emailBody = EmailHelper.handleUserIDToken(emailBody, user);		//CADSRPASSW-62
		_logger.info("sendEmail:user id = [" + user.getUsername() + "] body processed = [" + emailBody + "]");
		String emailAddress = user.getElectronicMailAddress();
		_logger.debug("NotifyPassword.sendEmail emailAddress [" + emailAddress + "]");
        open();
		dao = new PasswordNotifyDAO(_conn);
		String host = dao.getHostName();
		_logger.debug("NotifyPassword.sendEmail host [" + host + "]");
        open();
		dao = new PasswordNotifyDAO(_conn);
		String port = dao.getHostPort();
		_logger.debug("NotifyPassword.sendEmail port [" + port + "]");
		EmailSending ms = new EmailSending(adminEmailAddress, "dummy", host, port, emailAddress, emailSubject, emailBody);
		_logger.debug("NotifyPassword.sendEmail sending email ...");
		retVal = ms.send();
		_logger.debug("NotifyPassword.ms.send() is " + retVal);

		return retVal;
	}
	
	/**
	 * Method to make sure the latest processing details is reflected with the passed user.
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	private User refresh(User user) throws Exception {
        open();
		dao = new PasswordNotifyDAO(_conn);
		return dao.loadQueue(user);
	}

	/**
	 * Update the status of the delivery, sent or not sent.
	 * 
	 * @param users list of users affected
	 * @throws Exception 
	 */
	private void updateStatus(User user, String status, int daysLeft) throws Exception {
		if(user == null) {
			throw new Exception("User is NULL or empty.");
		}
		user = refresh(user);
		int currentCount = user.getAttemptedCount();
		String dStatus = user.getDeliveryStatus();
		if(dStatus == null) {
			dStatus = "";
		}
        open();
		dao = new PasswordNotifyDAO(_conn);
		user.setAttemptedCount(currentCount++);
		user.setProcessingType(String.valueOf(daysLeft));
		user.setDeliveryStatus(dStatus + " " + status);
		user.setDateModified(new Timestamp(DateTimeUtils.currentTimeMillis()));
		dao = new PasswordNotifyDAO(_conn);
//just for debugging
//if(status != null && status.equals(Constants.SUCCESS)) {
		_logger.debug("user id [" + user.getUsername() + "] status = [" + status + "]");
//}
		dao.updateQueue(user);
	}

	public boolean isNotificationValid(User user, int daysLeft, int totalNotificationTypes, int currentNotificationIndex) throws Exception {
		_logger.debug("isNotificationValid entered");
		boolean ret = false;
		boolean daysCondition = false;
		boolean deliveryStatus = false;
		String processedType = null;
		int attempted = -1;
		String status = null;
		long daysSincePasswordChange = -1;

		_logger.info("isNotificationValid: calculating last password change time ...");
		java.sql.Date passwordChangedDate = user.getPasswordChangedDate();
		if(passwordChangedDate == null) {
			throw new Exception("Not able to determine what is the password changed date or password change date is empty (from sys.cadsr_users view).");
		}
		daysSincePasswordChange = CommonUtil.calculateDays(passwordChangedDate, new Date(DateTimeUtils.currentTimeMillis()));
//_logger.info("isNotificationValid: FOR TEST ONLY *** this should be removed *** ===> daysSincePasswordChange hardcoded to 1");
//daysSincePasswordChange = 1;	//open this just for test
		_logger.info("isNotificationValid: last password change time was " + daysSincePasswordChange);

		if(daysSincePasswordChange != 0 && !isChangedRecently(daysLeft, daysSincePasswordChange) && !isAlreadySent(user, daysLeft)) {	//not recently changed (today)
			_logger.info("isNotificationValid: password was not recently changed");
			if(totalNotificationTypes != currentNotificationIndex) {
				_logger.info("isNotificationValid: type " + daysLeft + " is not the last notification type");
				if(user != null) {
					_logger.debug("isNotificationValid: checking user ...");
					//not the last type - send only once
					if(user.getDeliveryStatus() == null && user.getProcessingType() == null) {
						//has not been processed at all
						ret = true;
						_logger.debug("isNotificationValid is true: has not been processed before");
					} else 
					if(user.getDeliveryStatus() != null && user.getDeliveryStatus().equals(Constants.FAILED)) {
						//processed but failed
						ret = true;
						_logger.debug("isNotificationValid is true: processed but failed");
					} else 
					if(user.getProcessingType() != null && !user.getProcessingType().equals(String.valueOf(daysLeft))) {
						//it is different type of notification
						ret = true;
						_logger.debug("isNotificationValid is true: it is of different processing type, current type is " + daysLeft + " but the user's last processed type was " + user.getProcessingType());
					} else {
						_logger.info("isNotificationValid is false: none of the condition(s) met");
					}
					_logger.debug("isNotificationValid: check user done");
				} else {
					throw new Exception("User is NULL or empty.");
				}
			} else {
				if(daysLeft != Constants.DEACTIVATED_VALUE) {
					//the last notification type
					Calendar start = Calendar.getInstance();
					start.setTime(passwordChangedDate);
					if(daysSincePasswordChange >= 1) {
						ret = true;
						_logger.debug("isNotificationValid is true: current type is " + daysLeft + "(daily notification) and it has been over a day since the last notice");
					} else {
						_logger.debug("isNotificationValid is false: current type is " + daysLeft + "(daily notification) and it has not been over a day since the last notice sent");
					}
					_logger.info("isNotificationValid is " + ret + ": it has been " + daysSincePasswordChange + " day(s) since the password change");
				} else {
					_logger.debug("daily notification is disabled (types = '"+ _processingNotificationDays + "').");
				}
			}
		}
		/*
		else 
		if(daysSincePasswordChange == 0 || isChangedRecently(daysLeft, daysSincePasswordChange)) {	//reset everything if changed today OR if changed after the last check point
			_logger.debug("isNotificationValid is false, removing the user from the queue ...");
	        open();
			dao = new PasswordNotifyDAO(_conn);
			dao.removeQueue(user);
			_logger.info("isNotificationValid is false: user [" + user + "] removed from the queue.");
		}
		*/

		_logger.debug("isNotificationValid exiting with ret " + ret + " ...");
		
		return ret;
	}
	
	private boolean isChangedRecently(int daysLeft, long daysSincePasswordChange) {
		boolean ret = false;
		_logger.debug("isChangedRecently entered");
		if(daysSincePasswordChange <= daysLeft) {
			ret = true;
			_logger.info("isChangedRecently:daysSincePasswordChange is " + daysSincePasswordChange + " which is <= " + daysLeft + ", thus set to " + ret);
		}
//ret = false;	//open this just for test
//_logger.info("isNotificationValid: FOR TEST ONLY *** this should be removed *** ===> isChangedRecently hardcoded to false");
		_logger.debug("isChangedRecently is " + ret);
		return ret;
	}

	public boolean isAlreadySent(User user, int daysLeft) throws Exception {
		_logger.info("isAlreadySent user " + user );
        if(user == null || user.getUsername() == null) {
        	throw new Exception("User/ID is NULL or empty.");
        }
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean retVal = false;
		String s = null;
		String t = null;
		
		try {
			open();
			_logger.debug("connected");
	        if(_conn == null) {
	        	throw new Exception("Connection is NULL or empty.");
	        }
			String sql = "select delivery_status, processing_type from SBREXT.PASSWORD_NOTIFICATION where upper(username) =  ?";
	        stmt = _conn.prepareStatement(sql);
	        stmt.setString(1, user.getUsername().toUpperCase());
	        _logger.debug("isAlreadySent:check user [" + user + "] sent status");
			rs = stmt.executeQuery();
			if(rs.next()) {
				s = rs.getString("delivery_status");
				t = rs.getString("processing_type");
			}
	        _logger.debug("isAlreadySent: user [" + user + "] sent status [" + s + "]");
		} catch (Exception ex) {
			_logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { _logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { _logger.error(e.getMessage()); } }
        	if (_conn != null) { try { _conn.close(); _conn = null; } catch (SQLException e) { _logger.error(e.getMessage()); } }
		}

		if(s != null && s.indexOf(Constants.SUCCESS + String.valueOf(daysLeft)) > -1) {
			retVal = true;
		}
       _logger.info("returning isAlreadySent [" + retVal + "]");
       
       return retVal;
	}

	/**
	 * To run this in Eclipse -
	 * 
	 * 1. Copy log4j.properties from bin/ into java/ folder
	 * 2. Add program arguments "[full path]\config.xml" in the run
	 */
	public static void main(String[] args) {
        if (args.length != 1)
        {
            System.err.println(NotifyPassword.class.getName() + " config.xml");
            return;
        }
		
		NotifyPassword np = new NotifyPassword(null);

		try {
			_logger.info("");
			_logger.info(NotifyPassword.class.getClass().getName() + " begins");
			np.doAll(args[0]);
		} catch (Exception ex) {
			_logger.error(ex.toString(), ex);
		}
	}
}
