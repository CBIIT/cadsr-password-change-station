/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cadsrpasswordchange.tags;

import gov.nih.nci.cadsr.cadsrpasswordchange.core.PropertyHelper;

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
    			   logoutLink = "<td align=\"right\"><a href=\"javascript:callLogout();\" alt=\"Logout\">Logout</a></td>";

    	   String helpLink = "<td align=\"right\">"
    			   + "<div aria-hidden=true>"
    	   		   + "<a target=\"_blank\" href=\""+ PropertyHelper.getHELP_LINK() +"\">"
    			   + "<img style=\"border: 0px solid black\" title=\"Help Link\" src=\"/cadsrpasswordchange/images/icon_help.gif\" alt=\"Application Help\"></a>"
    			   + "</div>"
    			   + "<br>"
    			   + "<font color=\"brown\" face=\"verdana\" size=\"1\">&nbsp;Help&nbsp;</font></td>";
    	   
    	   out.print("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
    			   + "<tr>"
    			   + "<td width=\"25%\" valign=\"center\" align=\"left\">"
    			   + "<div aria-hidden=true>"
    			   + "<a href=\"https://www.cancer.gov\" target=\"_blank\" alt=\"NCI Logo\">"
    			   + "<img src=\"/cadsrpasswordchange/images/CBIIT-36px-Logo-COLOR_contrast.png\" alt=\"NCI Logo\" border=\"0\"></a>"
    			   + "</div>"
    			   + "</td>"
    			   +"<td width=\"60%\" valign=\"center\" align=\"right\"><img alt=\"Password Change Station Logo\" src=\"/cadsrpasswordchange/images/cadsrpasswordchange_banner_round.gif\"/></td>"
    			   +"<td width=\"15%\" valign=\"top\" align=\"right\"><div aria-hidden=true>"
    			   + "<a href=\"https://www.nih.gov/\" target=\"_blank\"><span>U.S. National Institutes of Health</span></a></div></td>"
    			   +"</tr>"
    			   + "\n</table>\n"
    			   + "<table class=\"secttable\"><colgroup><col /></colgroup><tbody class=\"secttbody\" />"
    			   + "<tr>"
    			   + "<td align=\"left\">"
    			   + "<div aria-hidden=true>"
    			   + "<a target=\"_blank\" href=\""+ PropertyHelper.getLOGO_LINK() +"\">"
    			   + "<img style=\"border: 0px solid black\" title=\"NCICB caDSR\" src=\"/cadsrpasswordchange/images/caDSR_logo2_contrast.png\" alt=\"Application Logo\"></a>"
    			   + "</div>"
    			   + "</td>"
    			   + logoutLink + helpLink + "</tr>"
    			   + "\n</table>\n");
    	   
       } catch (Exception ex) {
    	   logger.error("footer tag creation failed: " + ex.getMessage());       	
       }
       
       logger.debug("HeaderTag showLogout: " + showLogout);       
	   return EVAL_PAGE;
	}


}
