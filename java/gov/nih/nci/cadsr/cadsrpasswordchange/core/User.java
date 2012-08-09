package gov.nih.nci.cadsr.cadsrpasswordchange.core;

public class User {

	private String electronic_mail_address;
	private String username;
	private String account_status;
	private String expiry_date;
	private String lock_date;

	public String getElectronic_mail_address() {
		return electronic_mail_address;
	}

	public void setElectronic_mail_address(String electronic_mail_address) {
		this.electronic_mail_address = electronic_mail_address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccount_status() {
		return account_status;
	}

	public void setAccount_status(String account_status) {
		this.account_status = account_status;
	}

	public String getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}

	public String getLock_date() {
		return lock_date;
	}

	public void setLock_date(String lock_date) {
		this.lock_date = lock_date;
	}

}
