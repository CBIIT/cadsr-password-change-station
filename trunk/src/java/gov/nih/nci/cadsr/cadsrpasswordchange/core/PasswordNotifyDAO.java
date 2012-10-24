package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;

public class PasswordNotifyDAO implements PasswordNotify {

    public static String _jndiSystem = "java:/jdbc/caDSRPasswordChange";
	
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

    private Connection getConnection() throws Exception {
		DataSource ds = ConnectionUtil.getDS(PasswordChangeDAO._jndiSystem);
        logger.debug("got DataSource for " + _jndiSystem);    	
        logger.debug("got connection from jboss pool [" + _jndiSystem + "]");
        conn = ds.getConnection(PropertyHelper.getDatabaseUserID(), PropertyHelper.getDatabasePassword());
        
        conn.setAutoCommit(true);
        return conn;
    }

	/**
	 * Method to retrieve all the user's with password which are expiring within the days specified in the passed in number.
	 * @param withinDays
	 * @return
	 * @throws Exception 
	 */
	public List<User> getPasswordExpiringList(int withinDays, int size, int index, List<String>types) throws Exception {

		logger.info("getPasswordExpiringList entered");
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String value = null;
		List arr = new ArrayList();
		String sql = null;
		try {
	        if(conn == null) {
	        	throw new Exception("Connection is NULL or empty.");
	        }
	        logger.debug("connected");

	        if(size > 1 && size != index) {
	        	sql = SELECT_SQL + " and a.EXPIRY_DATE BETWEEN SYSDATE+? AND SYSDATE+?";
		        logger.debug("getPasswordExpiringList:before executing sql statement");
				stmt = conn.prepareStatement(sql);
		        logger.debug("getPasswordExpiringList:sql statement executed = [" + sql + "]");
		        int excludedDays = Integer.valueOf(types.get(index));	//exclude the next type and beyond
				stmt.setInt(1, excludedDays);	        	
				stmt.setInt(2, withinDays);	        	
		        logger.debug("getPasswordExpiringList:excludedDays = [" + excludedDays + "] for type [" + withinDays + "]");
	        } else {
	        	sql = SELECT_SQL + " and a.EXPIRY_DATE BETWEEN SYSDATE AND SYSDATE+?";
		        logger.debug("getPasswordExpiringList:before executing sql statement");
				stmt = conn.prepareStatement(sql);
		        logger.debug("getPasswordExpiringList:sql statement executed = [" + sql + "]");
				stmt.setInt(1, withinDays);	        	
	        }	        

	        logger.debug("set withinDays '" + withinDays + "'");
			rs = stmt.executeQuery();
	        logger.debug("sql executed, iterating list ...");
	        int debugCount = 0;
			while(rs.next()) {	//CADSRPASSW-56
				User user = new User();
				user.setElectronicMailAddress(rs.getString("electronic_mail_address"));
				user.setUsername(rs.getString("username"));
				user.setAccountStatus(rs.getString("account_status"));
				user.setExpiryDate(rs.getDate("expiry_date"));
				user.setLockDate(rs.getDate("lock_date"));
				user.setPasswordChangedDate(rs.getDate("ptime"));
				user.setDateModified(rs.getTimestamp("DATE_MODIFIED"));
				user.setDeliveryStatus(rs.getString("DELIVERY_STATUS"));
				user.setAttemptedCount(rs.getInt("ATTEMPTED_COUNT"));
				logger.info ("getRecipientList: mail_address '" + user.getElectronicMailAddress() + "', username '" + user.getUsername() + "' expiry_date '" + user.getExpiryDate() + "'");
				arr.add(user);
				debugCount++;
			}
	        logger.debug("iteration done with count " + debugCount);
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
        	throw ex;
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}

		logger.info("getPasswordExpiringList exiting ... arr size is " + arr.size() + " [" + arr + "]");
		
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
				user.setDateModified(rs.getTimestamp("DATE_MODIFIED"));
				user.setAttemptedCount(rs.getInt("ATTEMPTED_COUNT"));
				user.setProcessingType(rs.getString("PROCESSING_TYPE"));
				user.setDeliveryStatus(rs.getString("DELIVERY_STATUS"));
				user.setUsername(rs.getString("USERNAME"));		//CADSRPASSW-62
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

			stmt = conn.prepareStatement("select * from SBREXT.PASSWORD_NOTIFICATION where UPPER(UA_NAME) = ?");
			stmt.setString(1, user.getUsername().toUpperCase());
			rs = stmt.executeQuery();
			boolean found = false;
			if(rs.next()) {
				//assuming all user Ids are unique/no duplicate
				found = true;
				logger.debug ("1 updateQueue user found: " + user.getUsername());
			}
			if(!found) {
				logger.debug ("updateQueue: user not found: " + user.getUsername());
				stmt = conn.prepareStatement("insert into SBREXT.PASSWORD_NOTIFICATION (ua_name, date_modified, attempted_count, processing_type, delivery_status) values(?,?,?,?,?)");
				stmt.setString(1, user.getUsername().toUpperCase());
				logger.debug ("updateQueue: inserting, user date_modified: " + user.getDateModified());
				stmt.setTimestamp(2, handleEmptyDate(user.getDateModified()));
				stmt.setInt(3, user.getAttemptedCount());
				stmt.setString(4, user.getProcessingType());
				logger.debug ("updateQueue: inserting, user delivery_status: " + user.getDeliveryStatus());
				stmt.setString(5, handleEmptyDeliveryStatus(user.getDeliveryStatus()));
				logger.debug ("updateQueue new queue");
			} else {
				logger.debug ("2 updateQueue: updating, user found: " + user.getUsername());
				stmt = conn.prepareStatement("update SBREXT.PASSWORD_NOTIFICATION set date_modified = ?, attempted_count = ?, processing_type = ?, delivery_status = ? where ua_name = ?");
				logger.debug ("updateQueue: updating, user date_modified: " + user.getDateModified());
				stmt.setTimestamp(1, handleEmptyDate(user.getDateModified()));
				stmt.setInt(2, user.getAttemptedCount());
				stmt.setString(3, user.getProcessingType());
				logger.debug ("updateQueue: updating, user delivery_status: " + user.getDeliveryStatus());
				stmt.setString(4, handleEmptyDeliveryStatus(user.getDeliveryStatus()));
				stmt.setString(5, user.getUsername().toUpperCase());
				logger.debug ("updateQueue existing queue");
			}
			stmt.executeUpdate();
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}
	}

	private Timestamp handleEmptyDate(Timestamp date) {
		Timestamp retVal = date;
		if(date == null) {
			retVal = new Timestamp(DateTimeUtils.currentTimeMillis());
		}
		return retVal;
	}
	
	private String handleEmptyDeliveryStatus(String status) {
		String retVal = status;
		if(status == null) {
			retVal = " ";
		} else {
			//CADSRPASSW-70 and CADSRPASSW-71
			retVal = status.trim();
		}
		return retVal;
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
		String sql = null;
		try {
	        if(conn == null) {
	        	conn = getConnection();
	        }
	        if(conn == null) {
	        	throw new Exception("Connection is NULL or empty.");
	        }
	        logger.debug("connected");

			stmt = conn.prepareStatement("delete from SBREXT.PASSWORD_NOTIFICATION where UPPER(UA_NAME) = ?");
			stmt.setString(1, user.getUsername().toUpperCase());
			rs = stmt.executeQuery();
			logger.debug ("removeQueue: user : " + user.getUsername() + " queue removed");
//			sql = "alter user " + user.getUsername().toUpperCase() + " profile \"cadsr_user\"";
//			stmt = conn.prepareStatement(sql);
//			stmt.executeUpdate();
//			logger.debug ("removeQueue: cadsr_user profile re-applied (sql executed = [" + sql + "])");
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