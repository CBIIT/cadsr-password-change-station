<%@ taglib uri="/WEB-INF/tld/cadsrpasswordchange.tld" prefix="cadsrpasswordchangetags" %>
<%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title><%=CommonUtil.getPageHeader((String)request.getSession().getAttribute("action"))%></title>
        
		<div style="position:absolute;">
 			<a href="#skip">
  			<img src="/cadsrpasswordchange/images/skipnav.gif" border="0" height="1" width="1" alt="Skip Navigation" title="Skip Navigation" />
	 		</a>
		</div>
		        
        <html:base />
        <meta http-equiv="Content-Language" content="en-us">
        <meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=WINDOWS-1252">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <LINK href="/cadsrpasswordchange/css/cadsrpasswordchange.css" rel="stylesheet" type="text/css">
        
		<script language="JavaScript" type="text/JavaScript">
		function callLogout(){
		     document.LogoutForm.submit();
		}
		</script>
		
    </head>

	<body>

		<%
			if (session.getAttribute("username") == null) {
			// this shouldn't happen, make the user start over
			//response.sendRedirect("./jsp/loggedOut.jsp");
			response.sendRedirect(Constants.LOGGEDOUT_URL);
			return;
				}
			String errorMessage = (String)session.getAttribute("ErrorMessage");
		  			if (errorMessage == null)
		  				errorMessage = "";
			session.setAttribute("ErrorMessage", "");
			
			String userMessage = (String)session.getAttribute("UserMessage");
		  			if (userMessage == null)
		  				userMessage = "";
			session.setAttribute("UserMessage", "");
		%>
		  
		<form name="LogoutForm" method="post" action="../../cadsrpasswordchange/logout"></form>
     
    	<table class="secttable"><colgroup></colgroup><tbody class="secttbody" /><tr><td>

		<cadsrpasswordchangetags:header showlogout="false"/>

		<table><tr><td align=\"center\"><p class=\"ttl18\"><h3  style="margin-left:300px;"><%=CommonUtil.getPageHeader((String)request.getSession().getAttribute("action"))%></h3></p></td></tr></table>

		<a name="skip" id="skip"></a>
			
		<table>
<h3>Password Restrictions:</h3>
			<td class=\face\ style=\WIDTH: 617px\ colspan=\2\> 
    <ul> 
Your new password may not re-use your last 24 passwords.<br> 
You may not change your password more than once within 24 hours.<br> 
Your new password must be at least 8 and no more than 30 characters long.<br> 
Your new password must start with a letter.<br> 
Your new password may only use characters from the following categories and must include characters from at least three of these categories: 
<br><br>
<div style="margin-left:40px;">
<li>Uppercase Letters (A-Z)</li> 
<li>Lowercase Letters (a-z)</li> 
<li>Numerals (0-9)</li> 
<li>Special Characters ( _  #  $)
</div>
    </ul><tt> 
    </tt></td> 
					</table>
								
		<form name="PasswordChangeForm" action="../../cadsrpasswordchange/resetPassword" method="POST" focus="userid" title="Use this screen to change your password">
		<input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value/>"/>

            <%
            	if (errorMessage.equals("")) {
                        		if (userMessage.equals("")) {
            %>
        				<p class=std>Use this screen to change your password.</p>
        			<%
        				} else {
        			%>
        				<p class=std><%=userMessage%></p>
        			<%
        				}
        			%>
            <%
            	} else {
            %>
					<strong align="center"><%=errorMessage%></strong>
            <%
            	}
            %>          
		</td>
        	
        	<table summary="Login credentials and new password to change password.">
            <tr>
                <!--<td valign="middle"><label for="LoginID" class=bstd>Login ID:</p></td>-->
                <td valign="middle"><input id="LoginID" type="hidden" name="userid" value="<%=session.getAttribute(Constants.USERNAME)%>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="NewPassword" class=bstd>New Password:</p></td>
                <td valign="middle"><input id="NewPassword" type="password" name="newpswd1" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="NewPasswordRepeat" class=bstd>New Password (repeated):</p></td>
                <td valign="middle"><input id="NewPasswordRepeat" type="password" name="newpswd2" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
                <td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please provide your desired new password (repeated to avoid typos).</p></td>
            </tr><tr>
                <td valign="bottom">
                <input type="submit" name="changePassword" value="Change" style="text-align: center" class="but2">
                <input type="submit" name="cancel" value="Cancel" style="text-align: center" class="but2">
                </td>
            </tr><tr>
            <!--
                <td colspan="2" valign="middle"><%=Constants.PWD_RESTRICTIONS%></td>
            -->
            </tr>
        	</table>
    	</form>
            
		<cadsrpasswordchangetags:footer />
	
    	</td></tr></table>

	</body>

</html>
