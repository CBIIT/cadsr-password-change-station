

package gov.nih.nci.cadsrpasswordchange.core;

import java.io.Serializable;



public final class UserBean implements Serializable
{
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String USERBEAN_SESSION_ATTRIBUTE = "UserBeanSessionAttribute"; 
  
  // Attributes
  private String m_Password;
  private String m_username;


  /**
   * Constructor
   */
  public UserBean()
  {
  }

  /**
   * The getPassword method returns the Password for this bean.
   *
   * @return String The Password
   */
  public String getPassword()
  {
    return m_Password;
  }

  /**
   * The setPassword method sets the Password for this bean.
   *
   * @param Password The Password to set
   */
  public void setPassword(String Password)
  {
    m_Password = Password;
  }

  /**
   * The getUsername method returns the username for this bean.
   *
   * @return String The username
   */
  public String getUsername()
  {
    return m_username;
  }

  /**
   * The setUsername method sets the username for this bean.
   *
   * @param username The username to set
   */
  public void setUsername(String username)
  {
    m_username = username.toUpperCase();
  }

  


}


