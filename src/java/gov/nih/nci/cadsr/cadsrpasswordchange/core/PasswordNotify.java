/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import gov.nih.nci.cadsr.cadsrpasswordchange.domain.User;

import java.util.List;

public interface PasswordNotify {

	public List<User> getPasswordExpiringList(int withinDays, int size, int index, List<String>types) throws Exception;
	
	public User loadQueue(User user) throws Exception;
	
	public void updateQueue(User user) throws Exception;
	
	public void removeQueue(User user) throws Exception;

	public String getProcessTypes() throws Exception;
	
    public String getAdminEmailAddress() throws Exception;

	public String getAdminName() throws Exception;

	public String getErrorText() throws Exception;

	public String getHostName() throws Exception;

	public String getHostPort() throws Exception;
	
	public String getEmailBody() throws Exception;

	public String getEmailSubject() throws Exception;

}