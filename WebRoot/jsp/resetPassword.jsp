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
			
		<form name="PasswordChangeForm" action="../../cadsrpasswordchange/resetPassword" method="POST" focus="userid">
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
                <!--<td valign="middle"><label for="LoginID" class=bstd>Login ID:</p></td>-->
                <td valign="middle"><input id="LoginID" type="hidden" name="userid" value="<%=session.getAttribute(Constants.USERNAME) %>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="NewPassword" class=bstd>New Password:</p></td>
                <td valign="middle"><input id="NewPassword" type="password" name="newpswd1" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="NewPasswordRepeat" class=bstd>New Password (repeated):</p></td>
                <td valign="middle"><input id="NewPasswordRepeat" type="password" name="newpswd2" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="question1" class=bstd>Question 1:</p></td>
                <td valign="middle"><input id="question1" type="text" name="question1" value="<%=session.getAttribute(Constants.Q1) %>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="answer1" class=bstd>Answer 1:</p></td>
                <td valign="middle"><input id="answer1" type="text" name="answer1" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="question2" class=bstd>Question 2:</p></td>
                <td valign="middle"><input id="question2" type="text" name="question2" value="<%=session.getAttribute(Constants.Q2) %>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="answer2" class=bstd>Answer 2:</p></td>
                <td valign="middle"><input id="answer2" type="text" name="answer2" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="question3" class=bstd>Question 3:</p></td>
                <td valign="middle"><input id="question3" type="text" name="question3" value="<%=session.getAttribute(Constants.Q3) %>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="answer3" class=bstd>Answer 3:</p></td>
                <td valign="middle"><input id="answer3" type="text" name="answer3" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
                <td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please provide your desired new password (repeated to avoid typos) and at least one correct answer for any one of the security questions you have previously setup.</p></td>
            </tr><tr>
                <td valign="bottom"><input type="submit" name="changePassword" value="Change" style="text-align: center" class="but2"></td>
            </tr><tr>
                <td colspan="2" valign="middle"><%=Constants.PWD_RESTRICTIONS%></td>
            </tr>
        	</table>
    	</form>
            
		<cadsrpasswordchangetags:footer />
	
    	</td></tr></table>

	</body>

</html>
