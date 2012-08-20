package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.util.List;

public interface PasswordNotify {

	public List<User> getPasswordExpiringList(int withinDays);
	
	public User loadQueue(User user) throws Exception;
	
	public void updateQueue(User user) throws Exception;
	
	public void removeQueue(User user) throws Exception;

}