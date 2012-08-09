package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import org.apache.log4j.Logger;

public class NotifyPassword {

	private static Logger _logger = Logger.getLogger(NotifyPassword.class);
	private static int count = 0;

	public void doAll() {
		try {
			if (PropertyHelper.getEMAIL_ID() != null
					&& PropertyHelper.getEMAIL_PWD() != null) {
				_logger.debug("quartz=." + count++ + ".");
			} else {
				_logger.info("-Not able to send, email not setup in the database?-");
			}

		} catch (Exception ex) {
		}
	}

	public static void main(String[] args) {
		NotifyPassword np = new NotifyPassword();

		try {
			_logger.info("");
			_logger.info(NotifyPassword.class.getClass().getName() + " begins");
			np.doAll();
		} catch (Exception ex) {
			_logger.error(ex.toString(), ex);
		}
	}
}
