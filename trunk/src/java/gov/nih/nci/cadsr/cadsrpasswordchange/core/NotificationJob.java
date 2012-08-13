package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import org.apache.log4j.Logger;
import org.quartz.*;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class NotificationJob implements Job {

//	private static Logger log = Logger.getLogger(NotificationJob.class.getName());
//	private static NotifyPassword np = new NotifyPassword();
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
//		np.doAll("config.xml");
	}
}