/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cadsrpasswordchange.core;

public interface PasswordEntry {

	public String getToolProperty(String toolName, String property);

}