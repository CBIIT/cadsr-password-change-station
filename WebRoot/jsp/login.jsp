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
           
    </head>

	<body>


		<%
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

            <% if (errorMessage.equals("")) { %>
        		<p class=std>Please login. (You may login here with an expired password.)</p>
            <%} else { %>
				<strong align="center"><%=errorMessage%></strong>
            <% } %>

        	<table summary="Login credentials for the caDSR Password Change Station.">
            	<tr>
                	<td valign="middle"><label for="LoginID" class=bstd>Login ID:</p></td>
                	<td valign="middle"><input id="LoginID" type="text" name="userid" value="" style="width: 3.75in" class="std"></td>
            	</tr><tr>
            	<tr>
                	<td valign="middle"><label for="Password" class=bstd>Password:</p></td>
                	<td valign="middle"><input id="Password" type="password" name="pswd" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            	</tr><tr>
                	<td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please enter a Login ID and Password.</p></td>
            	</tr><tr>
                	<td valign="bottom"><input type="submit" name="logon" value="Login" style="text-align: center" class="but2"></td>
            	</tr>
        	</table>
    	</form>
		<center><a target="_top" href="<%=Constants.ASK_USERID_URL%>">Forgot My Password</a></center><p>
		<center><a target="_top" href="<%=Constants.ASK_USERID_URL%>">Unlock My Password</a></center>
    	
		<cadsrpasswordchangetags:footer />
		
    	</td></tr></table>

	</body>

</html>
