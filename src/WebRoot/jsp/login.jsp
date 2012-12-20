<%@ taglib uri="/WEB-INF/tld/cadsrpasswordchange.tld" prefix="cadsrpasswordchangetags" %>
<%-- <%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %> --%>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
		<!-- Build 1.0 RC7d -->
        <title><%=Constants.RESET_TITLE%></title>

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

    </head>

	<body>
		<%
			MainServlet.initProperties();
			String errorMessage = (String)session.getAttribute("ErrorMessage");
		  			if (errorMessage == null)
		  				errorMessage = "";
			session.setAttribute("ErrorMessage", "");
			
		%>  

	    <table class="secttable"><colgroup></colgroup><tbody class="secttbody" /><tr><td align="center">

		<cadsrpasswordchangetags:header />
		<a name="skip" id="skip"></a>

		<form name="LoginForm" action="/cadsrpasswordchange/login" method="POST" focus="userid" title="Use this screen to login">
		<input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value/>"/>

            <%
            	if (errorMessage.equals("")) {
            %>
        		<p class=std>Please click on one option to manage your account.</p>
            <%
            	} else {
            %>
					<strong class="accessible-error-text" align="center"><%=errorMessage%></strong>
            <%
            	}
            %>

        	<table summary="Login credentials for the caDSR Password Change Station.">
            	<tr>
            	<!--
                	<td valign="middle"><label for="LoginID" class=bstd>Login ID:</p></td>
                	<td valign="middle"><input id="LoginID" type="text" name="userid" value="" style="width: 3.75in" class="std"></td>
            	</tr><tr>
            	<tr>
                	<td valign="middle"><label for="Password" class=bstd>Password:</p></td>
                	<td valign="middle"><input id="Password" type="password" name="pswd" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            	</tr><tr>
                	<td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please enter a Login ID and Password.</p></td>
            	</tr>
                	<td valign="bottom"><input type="submit" name="logon" value="Login" style="text-align: center" class="but2"></td>
				-->
            	<tr>
<td><img id="img1" accesskey="C" tabindex="1" onclick="javascript:ForgotPasswordSubmit();" src="/cadsrpasswordchange/images/change-enroll-glow100x_purple.gif" alt="Setup Security Questions" style="border-width:0px;">&nbsp;&nbsp;</td>
<td style="width:50px;"></td>
<td><img id="img1" accesskey="C" tabindex="2" onclick="javascript:ForgotPasswordSubmit();" src="/cadsrpasswordchange/images/changepw-100x_purple.gif" alt="Change Password" style="border-width:0px;">&nbsp;&nbsp;</td>
<td style="width:50px;"></td>
<td><img id="img2" accesskey="R" tabindex="3" onclick="javascript:ForgotPasswordSubmit();" src="/cadsrpasswordchange/images/forgot-100x_purple.gif" alt="Forgot My Password" style="border-width:0px;">&nbsp;&nbsp;</td>
<td style="width:50px;"></td>
<td><img id="img3" accesskey="U" tabindex="4" onclick="javascript:ForgotPasswordSubmit();" src="/cadsrpasswordchange/images/unlock-100x_purple.gif" alt="Unlock My Account" style="border-width:0px;"></td><p>
            	</tr>
            	<tr>
		<td><center><a target="_top" href="<%=Constants.SETUP_QUESTIONS_URL%>">Setup Security Questions</a></center></td>
		<td></td>
		<td><center><a target="_top" href="<%=Constants.REQUEST_USERID_FOR_CHANGE_PASSWORD_URL%>">Change Password</a></center></td>
		<td></td>
		<td><center><a target="_top" href="<%=Constants.ASK_USERID_URL%>?action=forgot">Forgot My Password</a></center></td>
		<td></td>
		<td><center><a target="_top" href="<%=Constants.ASK_USERID_URL%>?action=unlock">Unlock My Account</a></center></td>
            	</tr>
            	
        	</table>
    	</form>
    	
        	<p>        	
        	Warning: Security questions must be created in order to reset a password if a password is forgotten or locked.
		<cadsrpasswordchangetags:footer />
		
    	</td></tr></table>

	</body>

</html>
