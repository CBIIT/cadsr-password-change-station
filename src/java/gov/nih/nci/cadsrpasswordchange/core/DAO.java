package gov.nih.nci.cadsrpasswordchange.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;


public class DAO {

    private Logger _logger = Logger.getLogger(DAO.class);

    public static final String CHANGEPASSWORD = "select 1 from dual";
    
    private void debug(String msg) {
//		_logger.debug(msg);
//		
//        Logger rootLogger = Logger.getRootLogger();
//        if (rootLogger != null) {
//        	rootLogger.info("DAO using root logger: " + msg);
//        }
		
    }
    
    private static String _jndiName = "java:/jdbc/caDSR";

    public DAO() { 	
    }
    
	public boolean checkValidUser(String user, String password) {

debug ("checkValid user   user " + user + " password " + password);
		boolean success = false;
		Connection conn = null;
		try {
			Context envContext = new InitialContext();
debug("got Context");			
	        DataSource ds = (DataSource)envContext.lookup(_jndiName);
debug("got DataSource");
	        conn = ds.getConnection(user, password);
debug("got Connection");	        
	        success = true;
	    } catch (Exception ex) {
	    	// ?
debug(ex.getMessage());	    	
        }
        finally {
        	if (conn != null) {
        		try {
        			conn.close();
        		} catch (Exception ex) {
        			// ?
        		}
	        }
	    }
        return success;
	}
	

	public boolean changePassword(String user, String password, String newPassword) {

		debug ("changePassword  user " + user + " password " + password + " newPassword " + newPassword);
				boolean success = false;
				Connection conn = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					Context envContext = new InitialContext();
		debug("got Context");			
			        DataSource ds = (DataSource)envContext.lookup(_jndiName);
		debug("got DataSource");
			        conn = ds.getConnection(user, password);
		debug("got Connection");	    
        pstmt = conn.prepareStatement(CHANGEPASSWORD);
//        pstmt.setString(1, user);
//        pstmt.setString(2, password);
//        pstmt.setString(3, newPassword);
        rs = pstmt.executeQuery();
        if (rs.next()) {
 debug(rs.getString(1));
        }
// how do we interpret the rs?		
			        success = true;
			    } catch (Exception ex) {
			    	// ?
		debug(ex.getMessage());	    	
		        }
		        finally {
		        	if (rs != null) {
		        		try {
		        			rs.close();
		        		} catch (Exception ex) {
		        			// ?
		        		}
			        }
		        	if (pstmt != null) {
		        		try {
		        			pstmt.close();
		        		} catch (Exception ex) {
		        			// ?
		        		}
			        }
		        	if (conn != null) {
		        		try {
		        			conn.close();
		        		} catch (Exception ex) {
		        			// ?
		        		}
			        }
			    }
		        return success;
			}	
	
	
}
