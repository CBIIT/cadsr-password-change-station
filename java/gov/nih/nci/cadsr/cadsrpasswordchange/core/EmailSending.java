package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import java.util.Properties;
//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

public class EmailSending {
//	
//	String senderEmailId;
//	String senderPassword;
//	String emailHostName;
//	String emailPortNumber;
//	String recieverEmailId;
//	String emailSubject;
//	String emailBody;
//
//	static Properties props = new Properties();
//
//	public EmailSending(String senderEmailId, String senderPassword,
//			String emailHostName, String emailPortNumber,
//			String recieverEmailId, String emailSubject, String emailBody) {
//		this.senderEmailId = senderEmailId;
//		this.senderPassword = senderPassword;
//		this.emailHostName = emailHostName;
//		this.emailPortNumber = emailPortNumber;
//		this.recieverEmailId = recieverEmailId;
//		this.emailSubject = emailSubject;
//		this.emailBody = emailBody;
//		props.put("mail.smtp.user", senderEmailId);
//		props.put("mail.smtp.host", emailHostName);
//		props.put("mail.smtp.port", emailPortNumber);
//		props.put("mail.smtp.starttls.enable", "true");
////		props.put("mail.smtp.auth", "true");
//		props.put("mail.smtp.auth", "false");
//		// props.put("mail.smtp.debug", "true");
//		props.put("mail.smtp.socketFactory.port", emailPortNumber);
//		props.put("mail.smtp.socketFactory.class",
//				"javax.net.ssl.SSLSocketFactory");
//		props.put("mail.smtp.socketFactory.fallback", "false");
//	}
//
//	SecurityManager security = System.getSecurityManager();
//
//	public void send() throws Exception {
//
//		Authenticator auth = new SMTPAuthenticator();
//		Session session = Session.getInstance(props, auth);
//		// session.setDebug(true);
//		MimeMessage msg = new MimeMessage(session);
//		msg.setContent(emailBody, "text/html");
//		msg.setSubject(emailSubject);
//		msg.setFrom(new InternetAddress(senderEmailId));
//		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
//				recieverEmailId));
//		Transport.send(msg);
//	}
//
//	private class SMTPAuthenticator extends javax.mail.Authenticator {
//		public PasswordAuthentication getPasswordAuthentication() {
//			return new PasswordAuthentication(senderEmailId, senderPassword);
//		}
//	}
//
//	public static void main(String args[]) throws Exception {
//		EmailSending ms = new EmailSending("warzeld@mail.nih.gov", "uyeiy3wjukhkuqhwgiw7t1f2863f",
//				"mailfwd.nih.gov", "465", "James.Tan@nih.gov", "caDSR Password Expiration Notice", "Hello");
//		ms.send();
//	}
}
