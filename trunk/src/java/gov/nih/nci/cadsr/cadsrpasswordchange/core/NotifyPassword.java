package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

import org.apache.log4j.Logger;

public class NotifyPassword {

	private static Logger _logger = Logger.getLogger(NotifyPassword.class);
	private static int count = 0;
	private static AbstractDao dao;
	public static String emailSubject;
	public static String emailBody;
    private Properties          _propList;
    private Connection          _conn;
    private String              _dsurl;
    private String              _user;
    private String              _pswd;

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
    }
    
	public void doAll(String propFile_) throws Exception {
        loadProp(propFile_);
        open();
		
		try {
			if (PropertyHelper.getEMAIL_ID() != null
					&& PropertyHelper.getEMAIL_PWD() != null) {
				_logger.debug("quartz=." + count++ + ".");
				List u14 = null;
				List u7 = null;
				List u4 = null;
				dao = new DAO(_conn);
				u14 = dao.getPasswordExpiringList(14);
				updateQueue(u14);
				sendEmail(u14, 14);
				updateStatus(handleEmailNotification(u14));
				u7 = dao.getPasswordExpiringList(7);
				updateQueue(u7);
				sendEmail(u14, 7);
				updateStatus(handleEmailNotification(u7));
				u4 = dao.getPasswordExpiringList(4);
				updateQueue(u4);
				sendEmail(u14, 4);
				updateStatus(handleEmailNotification(u4));
			} else {
				_logger.info("-Not able to send, email not setup in the database?-");
			}

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

	/**
	 * Add or update the queue with the outgoing email.
	 * 
	 * @return success/failed list
	 */
	private void updateQueue(List<User>users) {

	}
	
	/**
	 * Send the email.
	 * 
	 * @return success/failed list
	 */
	private List<User> handleEmailNotification(List<User>users) {

		return null;
	}
	
	/**
	 * Update the status of the delivery, sent or not sent.
	 * 
	 * @param users list of users affected
	 */
	private void updateStatus(List<User>users) {

	}
	
	private void sendEmail(List<User> users, int daysLeft) {
		emailSubject = "caDSR Password Expiration Notice";
		emailBody = "Your password is about to expire in " + daysLeft + ". Please login to Password Change Station or call NCI Helpdesk to change your password.";
		String emailAddress = "";
		EmailSending ms = new EmailSending("warzeld@mail.nih.gov", "uyeiy3wjukhkuqhwgiw7t1f2863f",
				"mailfwd.nih.gov", "25", emailAddress, emailSubject, emailBody);
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
