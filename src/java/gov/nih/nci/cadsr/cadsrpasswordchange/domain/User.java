package gov.nih.nci.cadsr.cadsrpasswordchange.domain;

import java.sql.Date;
import java.sql.Timestamp;

public class User {

	private String electronicMailAddress;
	private String username;
	private String accountStatus;
	private Date expiryDate;
	private Date lockDate;
	private Date createdDate;
	private Date passwordChangedDate;
	private Timestamp dateModified;
	private int attemptedCount;
	private String processingType;
	private String deliveryStatus;

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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getPasswordChangedDate() {
		return passwordChangedDate;
	}

	public void setPasswordChangedDate(Date password_changed_date) {
		this.passwordChangedDate = password_changed_date;
	}

	public Timestamp getDateModified() {
		return dateModified;
	}

	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}

	public int getAttemptedCount() {
		return attemptedCount;
	}

	public void setAttemptedCount(int attemptedCount) {
		this.attemptedCount = attemptedCount;
	}

	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(String processing_type) {
		this.processingType = processing_type;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String delivery_status) {
		this.deliveryStatus = delivery_status;
	}

	@Override
	public String toString() {
		return "User [electronicMailAddress=" + electronicMailAddress
				+ ", username=" + username + ", accountStatus=" + accountStatus
				+ ", expiryDate=" + expiryDate + ", lockDate=" + lockDate
				+ ", passwordChangedDate=" + passwordChangedDate
				+ ", dateModified=" + dateModified + ", attemptedCount="
				+ attemptedCount + ", processingType=" + processingType
				+ ", deliveryStatus=" + deliveryStatus + "]";
	}

}