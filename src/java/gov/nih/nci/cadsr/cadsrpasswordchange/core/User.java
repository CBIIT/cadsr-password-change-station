package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.sql.Date;

public class User {

	private String electronicMailAddress;
	private String username;
	private String accountStatus;
	private Date expiryDate;
	private Date lockDate;
	private Date passwordChangedDate;
	private Date dateModified;
	private int attemptedCount;
	private String processing_type;
	private String delivery_status;
	
	public String getElectronicMailAddress() {
		return electronicMailAddress;
	}

	public void setElectronicMailAddress(String electronic_mail_address) {
		this.electronicMailAddress = electronic_mail_address;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String account_status) {
		this.accountStatus = account_status;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiry_date) {
		this.expiryDate = expiry_date;
	}

	public Date getLockDate() {
		return lockDate;
	}

	public void setLockDate(Date lock_date) {
		this.lockDate = lock_date;
	}

	public Date getPasswordChangedDate() {
		return passwordChangedDate;
	}

	public void setPasswordChangedDate(Date password_changed_date) {
		this.passwordChangedDate = password_changed_date;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public int getAttemptedCount() {
		return attemptedCount;
	}

	public void setAttemptedCount(int attemptedCount) {
		this.attemptedCount = attemptedCount;
	}

	public String getProcessing_type() {
		return processing_type;
	}

	public void setProcessing_type(String processing_type) {
		this.processing_type = processing_type;
	}

	public String getDelivery_status() {
		return delivery_status;
	}

	public void setDelivery_status(String delivery_status) {
		this.delivery_status = delivery_status;
	}

}