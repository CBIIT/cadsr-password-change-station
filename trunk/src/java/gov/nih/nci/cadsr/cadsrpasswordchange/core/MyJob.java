package gov.nih.nci.cadsr.cadsrpasswordchange.core;

import org.quartz.*;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MyJob implements Job {

	int count = 0;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {			
			// Do all my stuff here
			System.out.println("quartz=."+ count++ + ".");

		} catch (Exception ex) {
		}
	}
}