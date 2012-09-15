package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;

public class PasswordNotifyDAO implements PasswordNotify {

	private Connection conn;
	private DataSource datasource;

    protected static final String SELECT_SQL = 
			"select" +
			"   c.electronic_mail_address," +
			"   a.USERNAME," +
			"   a.ACCOUNT_STATUS," +
			"   a.EXPIRY_DATE," +
			"   a.LOCK_DATE," +
			"   a.PTIME, " +
			"   b.DATE_MODIFIED," +
			"   b.ATTEMPTED_COUNT," +
			"   b.PROCESSING_TYPE," +
			"   b.DELIVERY_STATUS" +
			" from" +
			" SYS.CADSR_USERS a, SBREXT.PASSWORD_NOTIFICATION b, sbr.user_accounts_view c" +
			" where a.username = b.UA_NAME(+) and a.username = c.UA_NAME";
	
	private Logger logger = Logger.getLogger(PasswordNotifyDAO.class);

    public PasswordNotifyDAO(DataSource datasource) {
    	this.datasource = datasource;
    }

    public PasswordNotifyDAO(Connection conn) {
    	this.conn = conn;
    }

	/**
	 * Method to retrieve all the user's with password which are expiring within the days specified in the passed in number.
	 * @param withinDays
	 * @return
	 */
	public List<User> getPasswordExpiringList(int withinDays) {

		logger.info("getPasswordExpiringList entered");
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String value = null;
		List arr = new ArrayList();
		try {
	        if(conn == null) {
	        	throw new Exception("Connection is NULL or empty.");
	        }
	        logger.debug("connected");

			stmt = conn.prepareStatement(SELECT_SQL + " and a.EXPIRY_DATE BETWEEN SYSDATE AND SYSDATE+?"
			);
			stmt.setInt(1, withinDays);
			rs = stmt.executeQuery();
			if(rs.next()) {
				User user = new User();
				user.setElectronicMailAddress(rs.getString("electronic_mail_address"));
				user.setUsername(rs.getString("username"));
				user.setAccountStatus(rs.getString("account_status"));
				user.setExpiryDate(rs.getDate("expiry_date"));
				user.setLockDate(rs.getDate("lock_date"));
				user.setPasswordChangedDate(rs.getDate("ptime"));
				user.setDateModified(rs.getDate("DATE_MODIFIED"));
				logger.info ("getRecipientList: mail_address '" + user.getElectronicMailAddress() + "', username '" + user.getUsername() + "' expiry_date '" + user.getExpiryDate() + "'");
				arr.add(user);
			}
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}

       return arr;
	}

	/**
	 * Method to populate queue information specific to a user for password expiring notification.
	 * @param user
	 * @return populated user if found; otherwise null is returned
	 * @throws Exception
	 */
	public User loadQueue(User user) throws Exception {

		logger.info("getQueue entered");
		if(user == null || user.getUsername() == null) {
			throw new Exception("User or user's name can not be NULL or empty.");
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String value = null;
		List arr = new ArrayList();
		User ret = null;
		try {
	        if(conn == null) {				
	        	throw new Exception("Connection is NULL or empty.");
	        }
	        logger.debug("connected");

			stmt = conn.prepareStatement(SELECT_SQL + " and a.username = ?");
			stmt.setString(1, user.getUsername().toUpperCase());
			rs = stmt.executeQuery();
			boolean found = false;
			if(rs.next()) {
				//assuming all user Ids are unique/no duplicate
				found = true;
				logger.debug ("getQueue user found: " + user.getUsername());
			}
			if(found) {
				user.setElectronicMailAddress(rs.getString("electronic_mail_address"));
				user.setAccountStatus(rs.getString("ACCOUNT_STATUS"));
				user.setExpiryDate(rs.getDate("EXPIRY_DATE"));
				user.setLockDate(rs.getDate("LOCK_DATE"));
				user.setPasswordChangedDate(rs.getDate("PTIME"));
				user.setDateModified(rs.getDate("DATE_MODIFIED"));
				user.setAttemptedCount(rs.getInt("ATTEMPTED_COUNT"));
				user.setProcessingType(rs.getString("PROCESSING_TYPE"));
				user.setDeliveryStatus(rs.getString("DELIVERY_STATUS"));
				ret = user;
			}
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}
		
		return ret;
	}

	/**
	 * Method to insert/update a password expiration queue specific to a user.
	 * @param user the user (to be) notified
	 * @throws Exception 
	 */
	public void updateQueue(User user) throws Exception {

		logger.info("updateQueue entered");
		if(user == null || user.getUsername() == null) {
			throw new Exception("User or user's name can not be NULL or empty.");
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String value = null;
		List arr = new ArrayList();
		try {
	        if(conn == null) {
	        	throw new Exception("Connection is NULL or empty.");
	        }
	        logger.debug("connected");

			stmt = conn.prepareStatement("select * from SBREXT.PASSWORD_NOTIFICATION where UA_NAME = ?");
			stmt.setString(1, user.getUsername().toUpperCase());
			rs = stmt.executeQuery();
			boolean found = false;
			if(rs.next()) {
				//assuming all user Ids are unique/no duplicate
				found = true;
				logger.debug ("updateQueue user found: " + user.getUsername());
			}
			if(!found) {
				logger.debug ("updateQueue user not found: " + user.getUsername());
				stmt = conn.prepareStatement("insert into SBREXT.PASSWORD_NOTIFICATION (ua_name, date_modified, attempted_count, processing_type, delivery_status) values(?,?,?,?,?)");
				logger.debug ("updateQueue new queue");
			} else {
				logger.debug ("updateQueue user found: " + user.getUsername());
				stmt = conn.prepareStatement("update SBREXT.PASSWORD_NOTIFICATION set ua_name = ?, date_modified = ?, attempted_count = ?, processing_type = ?, delivery_status = ?");
				logger.debug ("updateQueue existing queue");
			}
			stmt.setString(1, user.getUsername().toUpperCase());
			stmt.setDate(2, user.getDateModified());
			stmt.setInt(3, user.getAttemptedCount());
			stmt.setString(4, user.getProcessingType());
			stmt.setString(5, user.getDeliveryStatus());
			stmt.executeUpdate();
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}
	}

	/**
	 * Method to delete a queue specific to a user.
	 * @param user the user (to be) notified
	 * @throws Exception 
	 */
	public void removeQueue(User user) throws Exception {

		logger.info("removeQueue entered");
		if(user == null || user.getUsername() == null) {
			throw new Exception("User or user's name can not be NULL or empty.");
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String value = null;
		List arr = new ArrayList();
		try {
	        if(conn == null) {
	        	throw new Exception("Connection is NULL or empty.");
	        }
	        logger.debug("connected");

			stmt = conn.prepareStatement("delete from SBREXT.PASSWORD_NOTIFICATION where UA_NAME = ?");
			stmt.setString(1, user.getUsername().toUpperCase());
			rs = stmt.executeQuery();
			logger.debug ("removeQueue user : " + user.getUsername() + " queue removed");
			stmt = conn.prepareStatement("alter user " + user.getUsername().toUpperCase() + " profile \"cadsr_user\"");
			stmt.executeUpdate();
			logger.debug ("updateQueue cadsr_user profile reapplied");
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}
	}
	
	public String getProcessTypes() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.NOTIFICATION_TYPE);
        return (value != null) ? value : "";
	}
	
    public String getAdminEmailAddress() throws Exception
    {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_ADDRESS);
        return (value != null) ? value : "";
    }

	public String getAdminName() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_ADMIN_NAME);
        return (value != null) ? value : "";
	}

	public String getErrorText() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_ERROR);
        return (value != null) ? value : "";
	}

	public String getHostName() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_HOST);
        return (value != null) ? value : "";
	}

	public String getHostPort() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_PORT);
        return (value != null) ? value : "";
	}

	public String getEmailBody() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_INTRO);
        return (value != null) ? value : "";
	}

	public String getEmailSubject() throws Exception {
        if(conn == null) {
        	throw new Exception("Connection is NULL or empty.");
        }

        PasswordEntry pe = new PasswordEntryDAO(conn);
        String value = pe.getToolProperty(Constants.TOOL_NAME, Constants.EMAIL_SUBJECT);
        return (value != null) ? value : "";
	}

}