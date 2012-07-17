package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;



public class DAO implements AbstractDao {
	
	private Connection conn;
	private DataSource datasource;
    private static final String  QUESTION_TABLE_NAME = "User_Security_Questions";

    protected static final String SELECT_COLUMNS = "ua_name, question1, answer1, question2, answer2, question3, answer3, date_modified";

    protected static final String PK_CONDITION = "ua_name=?";

    private static final String SQL_INSERT = "INSERT INTO User_Security_Questions (ua_name,question1,answer1,question2,answer2,question3,answer3,date_modified) VALUES (?,?,?,?,?,?,?,?)";

    private static String _jndiUser = "java:/jdbc/caDSR";
    private static String _jndiSystem = "java:/jdbc/caDSRPasswordChange";
    
    private Logger logger = Logger.getLogger(DAO.class);

    public DAO(DataSource datasource) {
    	this.datasource = datasource;
    }

    public DAO(Connection conn) {
    	this.conn = conn;
    }
    
	public boolean checkValidUser(String username) {
		boolean retVal = false;
		
		logger.info ("checkValidUser user: " + username);

//		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
//			Context envContext = new InitialContext();	
//	        DataSource ds = (DataSource)envContext.lookup(_jndiUser);
//	        logger.debug("got DataSource for " + _jndiUser);
//	        conn = ds.getConnection(adminUser, adminPassword);

//	        logger.debug("connected");
			pstmt = conn.prepareStatement("select * from USER_ACCOUNTS_VIEW where UA_NAME = ?");
			pstmt.setString(1, username);
			
			ResultSet result = pstmt.executeQuery();
			int count = 0;
			while(result.next()) {
			    count++;
			}
			if(count > 0) {
				//assuming all user Ids are unique/no duplicate
				retVal = true;
			}
	    } catch (Exception ex) {
	    	logger.debug(ex.getMessage());
        }
        finally {
//        	if (conn != null) {
//        		try {
//        			conn.close();
//        		} catch (Exception ex) {
//        			logger.error(ex.getMessage());
//        		}
//	        }
	    }
		
		
        logger.info("checkValidUser(): " + retVal);
        return retVal;
	}

	public UserBean checkValidUser(String username, String password) throws Exception {

		if(datasource == null) {
			throw new Exception("DataSource is empty or NULL.");
		}
		
		logger.info ("checkValidUser user: " + username);

		UserBean userBean = new UserBean(username);
		
		Connection conn = null;
		try {
			Context envContext = new InitialContext();	
	        DataSource ds = (DataSource)envContext.lookup(_jndiUser);
	        logger.debug("got DataSource for " + _jndiUser);
	        conn = ds.getConnection(username, password);
//			datasource.getConnection(username, password);
	        logger.debug("connected");	        
			userBean.setLoggedIn(true);
			userBean.setResult(new Result(ResultCode.NOT_EXPIRED));
	    } catch (Exception ex) {
	    	logger.debug(ex.getMessage());
			Result result = ConnectionUtil.decode(ex);
			
	    	// expired passwords are acceptable as logins
			if (result.getResultCode() == ResultCode.EXPIRED) {
				userBean.setLoggedIn(true);
				logger.debug("considering expired password acceptable login");
				
			}
			
			userBean.setResult(result);
        }
//        finally {
//        	if (conn != null) {
//        		try {
//        			conn.close();
//        		} catch (Exception ex) {
//        			logger.error(ex.getMessage());
//        		}
//	        }
//	    }
        logger.info("returning isLoggedIn " + userBean.isLoggedIn());        
        return userBean;
	}
	

	public Result changePassword(String user, String password, String newPassword) {

		logger.info("changePassword  user " + user );
		
		Result result = new Result(ResultCode.UNKNOWN_ERROR);  // (should get replaced)
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean isConnectionException = true;  // use to modify returned messages when exceptions are system issues instead of password change issues  
		
		try {
//			Context envContext = new InitialContext();	
//			DataSource ds = (DataSource)envContext.lookup(_jndiSystem);
//	        logger.debug("got DataSource for " + _jndiSystem);
//			conn = ds.getConnection();
//	        logger.debug("connected");
	        
	        isConnectionException = false;
		
			// can't use parameters with PreparedStatement and "alter user", create a single string
	        // (must quote password to retain capitalization for verification function)
			String alterUser = "alter user " + user + " identified by \"" + newPassword + "\" replace " + password;
			pstmt = conn.prepareStatement(alterUser);
			logger.debug("attempted to alter user " + user);
			pstmt.execute();
			result = new Result(ResultCode.PASSWORD_CHANGED);
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
			if (isConnectionException)
				result = new Result(ResultCode.UNKNOWN_ERROR);  // error not related to user, provide a generic error 
			else
				result = ConnectionUtil.decode(ex);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
//			if (conn != null) {
//				try {
//					conn.close();
//				} catch (Exception ex) {
//					logger.error(ex.getMessage());
//				}
//			}
		}

       logger.info("returning ResultCode " + result.getResultCode().toString());        
       return result;
	}	
		
    private ResultSet query( PreparedStatement pstmt, Object[] params) throws SQLException {
        params( pstmt, params );

        return pstmt.executeQuery();
    }
	
    private void params( PreparedStatement pstmt, Object[] params) throws SQLException {
        int i = 1;
        for (Object param : params) {
            if (param != null) {
                pstmt.setObject( i++, param );
            }
        }
    }
	
    public UserSecurityQuestion findByPrimaryKey( String uaName ) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        UserSecurityQuestion q = new UserSecurityQuestion();
        try {
            sql = "select * from " + QUESTION_TABLE_NAME + " where uaName = ?";

                PreparedStatement pstmt = conn.prepareStatement( sql );
                pstmt.setString(0, uaName);
    			ResultSet result = pstmt.executeQuery();
    			int count = 0;
    			while(result.next()) {
    				q.setUaName(rs.getString("ua_name"));
    				q.setQuestion1(rs.getString("question1"));
    				q.setAnswer1("answer1");
    				q.setQuestion2("question2");
    				q.setAnswer2("answer2");
    				q.setQuestion3("question3");
    				q.setAnswer3("answer3");
    				//q.setDateModified(new Timestamp());
    			    count++;
    			}
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
        }
        return q;
    }

    public UserSecurityQuestion findByUaName( String uaName ) throws Exception {
    	return findByPrimaryKey( uaName );
    }

    public UserSecurityQuestion[] findAll( ) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = null;
        ArrayList<UserSecurityQuestion> qList = new ArrayList<UserSecurityQuestion>();
        try {
            sql = "select * from " + QUESTION_TABLE_NAME;

                PreparedStatement pstmt = conn.prepareStatement( sql );
    			ResultSet result = pstmt.executeQuery();
    			int count = 0;
    			UserSecurityQuestion q = null;
    			while(result.next()) {
    				q = new UserSecurityQuestion();
    				q.setUaName(rs.getString("ua_name"));
    				q.setQuestion1(rs.getString("question1"));
    				q.setAnswer1("answer1");
    				q.setQuestion2("question2");
    				q.setAnswer2("answer2");
    				q.setQuestion3("question3");
    				q.setAnswer3("answer3");
    				//q.setDateModified(new Timestamp());
    				qList.add(q);
    			    count++;
    			}
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
        }
        return toArray(qList);
    }

    public boolean deleteByPrimaryKey( String uaName ) throws Exception {
    	//no requirement to delete anything

    	return false;
    }

    private void checkMaxLength( String name, String value, int maxLength)
            throws Exception {

        if ( value != null && value.length() > maxLength ) {
            throw new Exception("Value of column '" + name + "' cannot have more than " + maxLength + " chars");
        }
    }
    
    public void insert( UserSecurityQuestion dto ) throws Exception {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement( SQL_INSERT );

            if ( dto.getUaName() == null ) {
                throw new Exception("Value of column 'ua_name' cannot be null");
            }
            checkMaxLength( "ua_name", dto.getUaName(), 30 );
            stmt.setString( 1, dto.getUaName() );

            if ( dto.getQuestion1() == null ) {
                throw new Exception("Value of column 'question1' cannot be null");
            }
            checkMaxLength( "question1", dto.getQuestion1(), 500 );
            stmt.setString( 2, dto.getQuestion1() );

            if ( dto.getAnswer1() == null ) {
                throw new Exception("Value of column 'answer1' cannot be null");
            }
            checkMaxLength( "answer1", dto.getAnswer1(), 500 );
            stmt.setString( 3, dto.getAnswer1() );

            if ( dto.getQuestion2() == null ) {
                throw new Exception("Value of column 'question2' cannot be null");
            }
            checkMaxLength( "question2", dto.getQuestion2(), 500 );
            stmt.setString( 4, dto.getQuestion2() );

            if ( dto.getAnswer2() == null ) {
                throw new Exception("Value of column 'answer2' cannot be null");
            }
            checkMaxLength( "answer2", dto.getAnswer2(), 500 );
            stmt.setString( 5, dto.getAnswer2() );

            if ( dto.getQuestion3() == null ) {
                throw new Exception("Value of column 'question3' cannot be null");
            }
            checkMaxLength( "question3", dto.getQuestion3(), 500 );
            stmt.setString( 6, dto.getQuestion3() );

            if ( dto.getAnswer3() == null ) {
                throw new Exception("Value of column 'answer3' cannot be null");
            }
            checkMaxLength( "answer3", dto.getAnswer3(), 500 );
            stmt.setString( 7, dto.getAnswer3() );

            if ( dto.getDateModified() == null ) {
                dto.setDateModified( new Date( System.currentTimeMillis()));
            }
            stmt.setDate( 8, dto.getDateModified() );

            int n = stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
        }
    }

    public boolean update( String uaName, UserSecurityQuestion dto ) throws Exception {
        StringBuffer sb = new StringBuffer();
        ArrayList<Object> params = new ArrayList<Object>();

        if ( dto.getUaName() != null ) {
            checkMaxLength( "ua_name", dto.getUaName(), 30 );
            sb.append( "ua_name=?" );
            params.add( dto.getUaName());
        }

        if ( dto.getQuestion1() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "question1", dto.getQuestion1(), 500 );
            sb.append( "question1=?" );
            params.add( dto.getQuestion1());
        }

        if ( dto.getAnswer1() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "answer1", dto.getAnswer1(), 500 );
            sb.append( "answer1=?" );
            params.add( dto.getAnswer1());
        }

        if ( dto.getQuestion2() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "question2", dto.getQuestion2(), 500 );
            sb.append( "question2=?" );
            params.add( dto.getQuestion2());
        }

        if ( dto.getAnswer2() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "answer2", dto.getAnswer2(), 500 );
            sb.append( "answer2=?" );
            params.add( dto.getAnswer2());
        }

        if ( dto.getQuestion3() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "question3", dto.getQuestion3(), 500 );
            sb.append( "question3=?" );
            params.add( dto.getQuestion3());
        }

        if ( dto.getAnswer3() != null ) {
            if (sb.length() > 0) {
                sb.append( ", " );
            }

            checkMaxLength( "answer3", dto.getAnswer3(), 500 );
            sb.append( "answer3=?" );
            params.add( dto.getAnswer3());
        }

        if (sb.length() == 0) {
            return false;
        }

        params.add( uaName );

        Object[] oparams = new Object[ params.size() ];

        return updateOne( sb.toString(), PK_CONDITION, params.toArray( oparams ));
    }

    private boolean updateOne( String setstring, String cond, Object... params) throws Exception {
        int ret = executeUpdate( getUpdateSql( setstring, cond ), params );

        if (ret > 1) {
            throw new Exception("More than one record updated");
        }

        return ret == 1;
    }
    
    private int executeUpdate( String sql, Object... params) throws Exception {
        Statement stmt = null;

        try {
            if (params != null && params.length > 0) {
                PreparedStatement pstmt = conn.prepareStatement( sql );
                stmt = pstmt;

                params( pstmt, params);

                return pstmt.executeUpdate();
            }
            else {
                stmt = conn.createStatement();

                return stmt.executeUpdate( sql );
            }
        }
        catch (SQLException e) {
            throw new Exception( e );
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch (SQLException e) {}
        }
    }
    
    private String getUpdateSql(String setstring, String cond) {
        return getUpdateSql(setstring) + getSqlCondition( cond );
    }

    /**
     * Returns the condition starting with " WHERE " or an empty string.
     */
    private String getSqlCondition(String cond) {
        return cond != null && cond.length() > 0 ? (" WHERE " + cond) : "";
    }

    private String getUpdateSql(String setstring) {
        return "UPDATE " + QUESTION_TABLE_NAME + " SET " + setstring;
    }
    
    
    private UserSecurityQuestion fetch( ResultSet rs ) throws SQLException {
        UserSecurityQuestion dto = new UserSecurityQuestion();
        dto.setUaName( rs.getString( 1 ));
        dto.setQuestion1( rs.getString( 2 ));
        dto.setAnswer1( rs.getString( 3 ));
        dto.setQuestion2( rs.getString( 4 ));
        dto.setAnswer2( rs.getString( 5 ));
        dto.setQuestion3( rs.getString( 6 ));
        dto.setAnswer3( rs.getString( 7 ));
        dto.setDateModified( rs.getDate( 8 ));

        return dto;
    }

    private UserSecurityQuestion[] toArray(ArrayList<UserSecurityQuestion> list ) {
        UserSecurityQuestion[] ret = new UserSecurityQuestion[ list.size() ];
        return list.toArray( ret );
    }

	public Result resetPassword(String user, String newPassword) {

		logger.info("resetPassword  user " + user );
		
		Result result = new Result(ResultCode.UNKNOWN_ERROR);  // (should get replaced)
		Connection conn = null;
		PreparedStatement pstmt = null;
//			boolean isConnectionException = true;  // use to modify returned messages when exceptions are system issues instead of password change issues  
		
		try {
//				Context envContext = new InitialContext();	
//				DataSource ds = (DataSource)envContext.lookup(_jndiSystem);
//		        logger.debug("got DataSource for " + _jndiSystem);
//				conn = ds.getConnection();
//		        logger.debug("connected");

//		        isConnectionException = false;
		
			// can't use parameters with PreparedStatement and "alter user", create a single string
	        // (must quote password to retain capitalization for verification function)
			String alterUser = "alter user " + user + " identified by \"" + newPassword + "\" account unlock";
			pstmt = conn.prepareStatement(alterUser);
			logger.debug("attempted to alter user " + user);
			pstmt.execute();
			result = new Result(ResultCode.PASSWORD_CHANGED);
		} catch (Exception ex) {
			logger.debug(ex.getMessage());
//				if (isConnectionException)
//					result = new Result(ResultCode.UNKNOWN_ERROR);  // error not related to user, provide a generic error 
//				else
//					result = decode(ex);
		} finally {
		}

       return result;
	}	
}	
