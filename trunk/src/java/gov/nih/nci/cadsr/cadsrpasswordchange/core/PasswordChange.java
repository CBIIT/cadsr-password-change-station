package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.UserSecurityQuestion;

import java.util.List;

public interface PasswordChange {

    /**
     * Finds a record identified by its primary key.
     * @return the record found or null
     * @throws Exception 
     */
    public UserSecurityQuestion findByPrimaryKey( String uaName ) throws Exception;

    /**
     * Finds a record.
     * @throws Exception 
     */
    
    public UserSecurityQuestion findByUaName( String uaName ) throws Exception;
    /**
     * Finds records ordered by ua_name.
     * @throws Exception 
     */
    public UserSecurityQuestion[] findAll( ) throws Exception;

    /**
     * Deletes a record identified by its primary key.
     * @return true if the record was really deleted (existed)
     */
    public boolean deleteByPrimaryKey( String uaName ) throws Exception;
        
    /**
     * Inserts a new record.
     * @throws Exception 
     */
    public void insert( UserSecurityQuestion dto ) throws Exception;

    /**
     * Updates one record found by primary key.
     * @return true if the record was really updated (=found and any change was really saved)
     * @throws Exception 
     */
    public boolean update( String uaName, UserSecurityQuestion dto ) throws Exception;

	public boolean checkValidUser(String username) throws Exception;
	
	public UserBean checkValidUser(String username, String password) throws Exception;
	
	public Result changePassword(String user, String password, String newPassword);

	public Result resetPassword(String username, String newPassword);
	
	public String getToolProperty(String toolName, String property);
	
	public List getAccountStatus(String user);	

}