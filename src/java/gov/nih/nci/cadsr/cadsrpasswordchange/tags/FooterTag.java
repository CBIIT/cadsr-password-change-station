package gov.nih.nci.cadsr.cadsrpasswordchange.tags;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.ToolProperties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;



public class FooterTag extends TagSupport {


    
	private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(FooterTag.class);	

	
	public FooterTag() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int doEndTag() throws JspException {

       try {
    	   JspWriter out = pageContext.getOut();


	       String version = ToolProperties.getInstance().getProperty("version");
	       String buildTime = ToolProperties.getInstance().getProperty("buildtime");           
	       out.print("<table class=\"table3\"><colgroup></colgroup><tbody class=\"secttbody\" />"
	    		   + "<tr><td class=\"ncifmenu\"><div aria-hidden=true><span style=\"color: #dddddd\">" + version + "." + buildTime + "</span></div></td></tr>"
	    		   + "<tr><td class=\"nciftrtable\">"
	    		   + "<div aria-hidden=true role=presentation tabindex=-1>"
	    		   + "<a href=\"mailto:ncicb@pop.nci.nih.gov?subject=caDSR%20Password%20Change%20Station\"><img border=\"0\" src=\"/cadsrpasswordchange/images/email_icon.gif\" alt=\"Email NCI Help Desk\" title=\"Email NCI Help Desk\"></a>"
	    		   + "<a target=\"_blank\" href=\"http://www.cancer.gov/\"><img border=\"0\" src=\"/cadsrpasswordchange/images/footer_nci.gif\" alt=\"National Cancer Institute Logo\" title=\"National Cancer Institute\"></a>"
	    		   + "<a target=\"_blank\" href=\"http://www.dhhs.gov/\"><img border=\"0\" src=\"/cadsrpasswordchange/images/footer_hhs.gif\" alt=\"Department of Health and Human Services Logo\" title=\"Department of Health and Human Services\"></a>"
	    		   + "<a target=\"_blank\" href=\"http://www.nih.gov/\"><img border=\"0\" src=\"/cadsrpasswordchange/images/footer_nih.gif\" alt=\"National Institutes of Health Logo\" title=\"National Institutes of Health\"></a>"
	    		   + "<a target=\"_blank\" href=\"http://www.usa.gov/\"><img border=\"0\" src=\"/cadsrpasswordchange/images/footer_usagov.gif\" alt=\"USA.gov\" title=\"USA.gov\"></a>"
	    		   + "<a target=\"_blank\" href=\"https://wiki.nci.nih.gov/x/qxEhAQ\">Privacy Notice</a>"
	    		   + "</div>"
	    		   + "</td>\n</tr>\n</table>\n");
       } catch (Exception ex) {
    	   		logger.error("footer tag creation failed: " + ex.getMessage());        	
       }
	   return EVAL_PAGE;
	}


}
