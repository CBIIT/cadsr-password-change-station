package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
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
        if (_conn != null)
            return 0;

        try
        {
            OracleDataSource ods = new OracleDataSource();

            String parts[] = _dsurl.split("[:]");
            ods.setDriverType("thin");
            _logger.info("NotifyPassword v1.0 build 13");
//            _logger.debug("NotifyPassword:open before [3]=" + parts[3] + " [4]=" + parts[4] + " [5]=" + parts[5]);
//            parts[3] = parts[3].substring(1, parts[3].length());
//            _logger.info("NotifyPassword:open after [3]=" + parts[3] + " [4]=" + parts[4] + " [5]=" + parts[5]);
//            ods.setServerName(parts[3]);
//            ods.setPortNumber(Integer.parseInt(parts[4]));
//            ods.setServiceName(parts[5]);
            //http://docs.oracle.com/cd/B19306_01/java.102/b14355/jdbcthin.htm
//            String connString="jdbc:oracle:thin:@prodHost:1521:ORCL";
            String connString=_dsurl;
            ods.setURL(connString);
            ods.setUser(_user);
            ods.setPassword(_pswd);
            _logger.info("NotifyPassword:open _dsurl[" + _dsurl + " via _user["+ _user + "]");
            Connection conn = ods.getConnection();
//            _conn = ods.getConnection(_user, _pswd);
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
		_logger.debug("NotifyPassword.process entered ...");
		
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
						saveIntoQueue(u, days);
						if(sendEmail(u, days)) {
							updateStatus(u, Constants.SUCCESS, days);
						} else {
							updateStatus(u, Constants.FAILED, days);
						}
					} else {
						_logger.info("isNotificationValid is not valid, notification aborted for user: " + u.getUsername());
						updateStatus(u, null, days);
						_logger.debug("status date updated for user " + u);
					}
				}
			}
		} else {
			_logger.info("No user for notification of " + days + " found");
		}
		
		_logger.debug("NotifyPassword.process done.");		
	}

	/**
	 * Add or update the queue with the outgoing email.
	 */
	private void saveIntoQueue(User user, int daysLeft) throws Exception {
        open();
		dao = new PasswordNotifyDAO(_conn);
		user.setProcessingType(String.valueOf(daysLeft));
		dao.updateQueue(user);
	}
	
	private boolean sendEmail(User user, int daysLeft) throws Exception {
		emailSubject = "caDSR Password Expiration Notice";
		emailBody = "Your password is about to expire in " + daysLeft + ". Please login to Password Change Station or call NCI Helpdesk to change your password.";
		String emailAddress = user.getElectronicMailAddress();
//		EmailSending ms = new EmailSending("do-not-reply@nih.gov", "uyeiy3wjukhkuqhwgiw7t1f2863f", "mailfwd.nih.gov", "25", emailAddress, emailSubject, emailBody);
		_logger.info("sendEmail:send to test account is true");
		EmailSending ms = new EmailSending("xawave@gmail.com", "uyeiy3wjukhkuqhwgiw7t1f2863f", "mailfwd.nih.gov", "25", "xawave@gmail.com", emailSubject, emailBody);
		_logger.debug("sendEmail:sent");
		return ms.send();
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
		user = refresh(user);
		int currentCount = user.getAttemptedCount();
        open();
		dao = new PasswordNotifyDAO(_conn);
		user.setAttemptedCount(currentCount++);
		user.setProcessingType(String.valueOf(daysLeft));
		user.setDeliveryStatus(status);
		user.setDateModified(new java.sql.Date(new Date(DateTimeUtils.currentTimeMillis()).getTime()));
		dao.updateQueue(user);
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
					_logger.info("isNotificationValid: It has been " + daysSincePasswordChange + " day(s) since the password change, send flag is " + ret);
				} else {
					_logger.debug("daily notification is disabled (types = '"+ _processingNotificationDays + "').");
				}
			}
		} else 
		if(daysSincePasswordChange == 0 || isChangedRecently(daysLeft, daysSincePasswordChange)) {	//reset everything if changed today OR if changed after the last check point
	        open();
			dao = new PasswordNotifyDAO(_conn);
			dao.removeQueue(user);
		}
		
		return ret;
	}
	
	private boolean isChangedRecently(int daysLeft, long daysSincePasswordChange) {
		boolean ret = false;
		if(daysSincePasswordChange <= daysLeft) {
			ret = true;
		}
		return ret;
	}

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
