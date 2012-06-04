

package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.io.Serializable;



public final class UserBean implements Serializable
{


/**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String USERBEAN_SESSION_ATTRIBUTE = "UserBeanSessionAttribute"; 
  
  // Attributes
  private boolean loggedIn;
  private String username;
  private boolean passwordExpired;
  private Result result;

  public UserBean()
  {
	  super();
	  loggedIn = false;
	  username = "";
	  passwordExpired = false;
	  result = new Result(ResultCode.NONE);
  }

  public UserBean(String username) {
		super();
		this.loggedIn = false;
		this.username = username;
		this.passwordExpired = false;
		this.result = new Result(ResultCode.NONE);
	}
  
  /**
   * The getUsername method returns the username for this bean.
   *
   * @return String The username
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * The setUsername method sets the username for this bean.
   *
   * @param username The username to set
   */
  public void setUsername(String username)
  {
	    this.username = username;
  }

public boolean isPasswordExpired() {
	return passwordExpired;
}

public void setPasswordExpired(boolean passwordExpired) {
	this.passwordExpired = passwordExpired;
}

public boolean isLoggedIn() {
	return loggedIn;
}

public void setLoggedIn(boolean loggedIn) {
	this.loggedIn = loggedIn;
}

public Result getResult() {
	return result;
}

public void setResult(Result result) {
	this.result = result;
}

  


}


