package gov.nih.nci.cadsr.cadsrpasswordchange.core;

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

public class NotifyPassword {

	private static org.apache.log4j.Logger _logger = org.apache.log4j.Logger.getLogger(NotifyPassword.class);
	private static int count = 0;
	private static AbstractDao dao;
	public static String emailSubject;
	public static String emailBody;
    private Properties          _propList;
    private Connection          _conn;
    private String              _dsurl;
    private String              _user;
    private String              _pswd;
    private String              _processingNotificationDays;
    

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
            ods.setServerName(parts[0]);
            ods.setPortNumber(Integer.parseInt(parts[1]));
            ods.setServiceName(parts[2]);

            _conn = ods.getConnection(_user, _pswd);
            _conn.setAutoCommit(false);
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

        _logger.info("\n\nLoading properties...\n\n");

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

        _processingNotificationDays = _propList.getProperty(Constants.NOTIFICATION_TYPE);
        if (_processingNotificationDays == null)
            _logger.error("Missing " + Constants.NOTIFICATION_TYPE + " in " + propFile_);
    }
    
	public void doAll(String propFile_) throws Exception {
        loadProp(propFile_);
		
		try {
			List<String> types = new ArrayList<String>(Arrays.asList(_processingNotificationDays.split(","))); 	//note: no space in between the , separator
			int size = types.size();
			int index = 1;
			for (String t : types) {
				process(Integer.valueOf(t).intValue(), size, index);
				index++;
			}
			_logger.debug("quartz=." + count++ + ".");
		} catch (Exception e) {
			//e.printStackTrace();
			_logger.error(CommonUtil.toString(e));
		}
		
        if (_conn != null)
        {
            _conn.close();
            _conn = null;
        }
	}

	private void process(int days, int size, int index) throws Exception {
		List<User> recipients = null;
        open();
		dao = new DAO(_conn);
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
					}
				}
			}
		} else {
			_logger.debug("No user for notification of " + days + " found");
		}
	}

	/**
	 * Add or update the queue with the outgoing email.
	 * 
	 * @return success/failed list
	 * @throws Exception 
	 */
	private void saveIntoQueue(User user, int daysLeft) throws Exception {
        open();
		dao = new DAO(_conn);
		user.setProcessingType(String.valueOf(daysLeft));
		dao.updateQueue(user);
	}
	
	private boolean sendEmail(User user, int daysLeft) throws Exception {
		emailSubject = "caDSR Password Expiration Notice";
		emailBody = "Your password is about to expire in " + daysLeft + ". Please login to Password Change Station or call NCI Helpdesk to change your password.";
		String emailAddress = user.getElectronicMailAddress();
		EmailSending ms = new EmailSending("do-no-reply@mail.nih.gov", "uyeiy3wjukhkuqhwgiw7t1f2863f", "mailfwd.nih.gov", "25", emailAddress, emailSubject, emailBody);
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
		dao = new DAO(_conn);
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
		dao = new DAO(_conn);
		user.setAttemptedCount(currentCount++);
		user.setProcessingType(String.valueOf(daysLeft));
		user.setDeliveryStatus(status);
		user.setDateModified(new java.sql.Date(new Date().getTime()));
		dao.updateQueue(user);
	}

	private boolean isNotificationValid(User user, int daysLeft, int totalNotificationTypes, int currentNotificationIndex) throws Exception {
		boolean ret = false;
		boolean daysCondition = false;
		boolean deliveryStatus = false;
		String processedType = null;
		int attempted = -1;
		String status = null;
		long daysSincePasswordChange = -1;

		Date startDate = user.getPasswordChangedDate();
		if(startDate == null) {
			throw new Exception("Not able to determine what is the password changed date or password change date is empty");
		}
		daysSincePasswordChange = CommonUtil.calculateDays(startDate, new Date());

		if(daysSincePasswordChange != 0) {	//not recently changed
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
				//the last notification type
				Calendar start = Calendar.getInstance();
				start.setTime(startDate);
				_logger.info("isNotificationValid: It has been " + daysSincePasswordChange + " day(s) since the password change");
				if(daysSincePasswordChange >= 1) {
					ret = true;
				}
			}
		}
		
		return ret;
	}
	
	public static void main(String[] args) {
        if (args.length != 1)
        {
            System.err.println(NotifyPassword.class.getName() + " config.xml");
            return;
        }
		
		NotifyPassword np = new NotifyPassword();

		try {
			_logger.info("");
			_logger.info(NotifyPassword.class.getClass().getName() + " begins");
			np.doAll(args[0]);
		} catch (Exception ex) {
			_logger.error(ex.toString(), ex);
		}
	}
}
