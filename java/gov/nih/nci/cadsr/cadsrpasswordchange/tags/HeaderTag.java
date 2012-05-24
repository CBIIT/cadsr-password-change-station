package gov.nih.nci.cadsr.cadsrpasswordchange.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;



public class HeaderTag extends TagSupport {
    
	private static final long serialVersionUID = 1L;

    private Logger logger = Logger.getLogger(HeaderTag.class);	
	

	protected String showLogout = null;  // (remains null if attribute not specified)
	
	public String getShowlogout() {
	   return (this.showLogout);
	}
	
	public void setShowlogout(String showLogout) {
	   this.showLogout = showLogout;
	}	
      
    
	public HeaderTag() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int doEndTag() throws JspException {

       try {
    	   JspWriter out = pageContext.getOut();
    	   
    	   String logoutLink = "";
    	   if (showLogout != null && (showLogout.equalsIgnoreCase("yes") || showLogout.equalsIgnoreCase("true") ) )
    			   logoutLink = "<td align=\"right\"><a href=\"javascript:callLogout();\">Logout</a></td>";
          
    	   out.print("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#A90101\">"
    			   + "<tr bgcolor=\"#A90101\">"
    			   + "<td valign=\"center\" align=\"left\"><a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"NCI Logo\">"
    			   + "<img src=\"/cadsrpasswordchange/images/brandtype.gif\" border=\"0\"></a></td>"
    			   + "<td valign=\"center\" align=\"right\"><a href=\"http://www.cancer.gov\" target=\"_blank\" alt=\"NCI Logo\">"
    			   + "<img src=\"/cadsrpasswordchange/images/tagline_nologo.gif\" border=\"0\"></a></td></tr>"
    			   + "\n</table>\n"
    			   + "<table class=\"secttable\"><colgroup><col /></colgroup><tbody class=\"secttbody\" />"
    			   + "<tr><td align=\"left\"><a target=\"_blank\" href=\"http://ncicb.nci.nih.gov/NCICB/infrastructure/cacore_overview/cadsr\"><img style=\"border: 0px solid black\" title=\"NCICB caDSR\" src=\"/cadsrpasswordchange/images/cadsrpasswordchange_banner.gif\"></a></td>"
    			   + logoutLink + "</tr>"
    			   + "<tr><td align=\"center\"><p class=\"ttl18\">caDSR Password Change Station</p></td></tr>"
    			   + "\n</table>\n");
    	   
       } catch (Exception ex) {
    	   logger.error("footer tag creation failed: " + ex.getMessage());       	
       }
       
       logger.debug("HeaderTag showLogout: " + showLogout);       
	   return EVAL_PAGE;
	}


}
