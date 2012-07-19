<%@ taglib uri="/tags" prefix="cadsrpasswordchangetags" %>
<%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>caDSR Password Change Station</title>
        
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
     
    	<table class="secttable"><colgroup></colgroup><tbody class="secttbody" /><tr><td align="center">

		<cadsrpasswordchangetags:header showlogout="true"/>

		<a name="skip" id="skip"></a>
			
		<form name="PasswordChangeForm" action="../../cadsrpasswordchange/changePassword" method="POST" focus="userid" title="Use this screen to change your password">
		<input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value/>"/>

            <% if (errorMessage.equals("")) {
            		if (userMessage.equals("")) { %>
        				<p class=std>Use this screen to change your password.</p>
        			<%} else { %>
        				<p class=std><%=userMessage%></p>
        			<%} %>
            <%} else { %>
					<strong align="center"><%=errorMessage%></strong>
            <%} %>          
        	
        	<table summary="Login credentials and new password to change password.">
            <tr>
                <td valign="middle"><label for="LoginID" class=bstd>Login ID:</p></td>
                <td valign="middle"><input id="LoginID" type="text" name="userid" value="<%=session.getAttribute("username") %>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="OldPassword" class=bstd>Current Password:</p></td>
                <td valign="middle"><input id="OldPassword" type="password" name="pswd" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="NewPassword" class=bstd>New Password:</p></td>
                <td valign="middle"><input id="NewPassword" type="password" name="newpswd1" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="NewPasswordRepeat" class=bstd>New Password (repeated):</p></td>
                <td valign="middle"><input id="NewPasswordRepeat" type="password" name="newpswd2" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
                <td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please provide your login credentials and your desired new password (repeated to avoid typos).</p></td>
            </tr><tr>
                <td valign="bottom"><input type="submit" name="changePassword" value="Change" style="text-align: center" class="but2">
<!--
                <input type="submit" name="cancel" value="Cancel" style="text-align: center" class="but2">
-->
                </td>
            </tr><tr>
				<td style="width:100%;text-align: center" colspan="2">
					<a target="_top" href="<%=Constants.SETUP_QUESTIONS_URL%>">Setup My Security Questions</a>
					<p>
				</td>
				<td></td>
            </tr><tr>
            	<!--
                <td colspan="2" valign="middle"><a target="_blank" href="https://wiki.nci.nih.gov/x/3AJQB">Please see the NCI Wiki for information on caDSR passwords including restrictions on choice of passwords</a></td>
                -->
                <td colspan="2" valign="middle"><%=Constants.PWD_RESTRICTIONS%></td>
            </tr>
        	</table>
    	</form>
            
		<cadsrpasswordchangetags:footer />
	
    	</td></tr></table>

	</body>

</html>
