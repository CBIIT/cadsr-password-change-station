package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTimeUtils;

public class PasswordEntryDAO implements PasswordEntry {

    public static String _jndiUser = "java:/jdbc/caDSR";
    public static String _jndiSystem = "java:/jdbc/caDSRPasswordChange";	
	private Connection conn;
	private DataSource datasource;

	private Logger logger = Logger.getLogger(PasswordEntryDAO.class);

    public PasswordEntryDAO(DataSource datasource) {
    	this.datasource = datasource;
    }

    public PasswordEntryDAO(Connection conn) {
    	this.conn = conn;
    }

    //CADSRPASSW-46
    private Connection getConnection() throws Exception {
//		DataSource ds = ConnectionUtil.getDS(PasswordChangeDAO._jndiSystem);
//        logger.debug("got DataSource for " + _jndiSystem);    	
////        conn = ds.getConnection();
//        conn = ds.getConnection(PropertyHelper.getDatabaseUserID(), PropertyHelper.getDatabasePassword());

        String jdbcurl = PropertyHelper.getDatabaseURL();
        logger.debug("got connection using direct jdbc url [" + jdbcurl + "]");
        Properties info = new Properties();
        info.put( "user", PropertyHelper.getDatabaseUserID() );
        logger.debug("with user id [" + PropertyHelper.getDatabaseUserID() + "]");
        info.put( "password", PropertyHelper.getDatabasePassword() );
        Connection conn = DriverManager.getConnection(jdbcurl, info);
        
        return conn;
    }
    
	/**
	 * This should be moved into a common utility class.
	 * @param toolName
	 * @param property
	 * @return
	 */
	public String getToolProperty(String toolName, String property) {

		logger.info("accessing sbrext.tool_options_view_ext table, toolName '" + toolName + "', property '" + property + "'");
		
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String value = null;
		
		try {
	        if(conn == null) {				
//	        	DataSource ds = ConnectionUtil.getDS(PasswordChangeDAO._jndiSystem);
//		        logger.debug("got DataSource for " + _jndiSystem);
//	        	
//		        conn = ds.getConnection();
	          conn = getConnection();
	        }
	        logger.debug("connected");

			stmt = conn.prepareStatement("select value from sbrext.tool_options_view_ext where Tool_name = ? and Property = ?");
			stmt.setString(1, toolName);
			stmt.setString(2, property);
			rs = stmt.executeQuery();
			if(rs.next()) {
				//assuming all user Ids are unique/no duplicate
				value = rs.getString("value");
				logger.info ("getToolProperty: toolName '" + toolName + "', property '" + property + "' value '" + value + "'");			
			}
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
		} finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
            if (stmt != null) {  try { stmt.close(); } catch (SQLException e) { logger.error(e.getMessage()); } }
        	if (conn != null) { try { conn.close(); conn = null; } catch (SQLException e) { logger.error(e.getMessage()); } }
		}

       return value;
	}
}