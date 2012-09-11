v<%@ page isELIgnored ="false" %>
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
 		<SCRIPT type="text/javascript" src="/cadsrpasswordchange/js/cadsrpasswordchange.js"></script>
		<script language="JavaScript" type="text/JavaScript">
		function callLogout(){
		     document.LogoutForm.submit();
		}
		</script>
		
    </head>

	<body onLoad="setFocus('answer', 'text');">

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
     
    	<table class="secttable"><colgroup></colgroup><tbody class="secttbody" /><tr><td align="center">

		<cadsrpasswordchangetags:header showlogout="false"/>
		
		<table><tr><td align=\"center\"><p class=\"ttl18\"><h3><%=CommonUtil.getPageHeader((String)request.getSession().getAttribute("action"))%></h3></p></td></tr></table>

		<a name="skip" id="skip"></a>
			
		<form name="PasswordChangeForm" action="../../cadsrpasswordchange/validateQuestion1" method="POST" focus="userid" title="Use this screen to validate security questions">
<!--			
		<form name="PasswordChangeForm" action="../../cadsrpasswordchange/promptQuestion1" method="POST" focus="userid">
-->
		<input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value/>"/>

            <%
            	if (errorMessage.equals("")) {
                        		if (userMessage.equals("")) {
            %>
        				<p class=std></p>
        			<%
        				} else {
        			%>
        				<p class=std><%=userMessage%> </p>
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
        	
        	<script>
        		function handleOption(option) {
        			if(option.selectedIndex === 1) {
        				document.getElementById('answer').value = '<%=Constants.A2%>';
        			} else 
           			if(option.selectedIndex === 2) {
	        			document.getElementById('answer').value = '<%=Constants.A3%>';
           			}
        			//alert('changed to option ' + option.selectedIndex);
        		}
        	</script>
        	
        	<table summary="Login credentials and new password to change password.">
            <tr>
                <td valign="middle"><label for="question" class=bstd>Question 1:</p></td>
                <td valign="middle"><input id="question" type="text" name="question" value="<%=session.getAttribute(Constants.Q1)%>" style="width: 3.75in" class="std" readonly="readonly"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="answer" class=bstd>Answer 1:</p></td>
                <td valign="middle"><input id="answer" type="password" name="answer" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            	<input id="answer" type="hidden" name="answerIndex" value="answer1">
                <td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please answer your security question 1.</p></td>
            </tr><tr>
                <td valign="bottom"><input type="submit" name="changePassword" value="Next" style="text-align: center" class="but2">
                <input type="submit" name="cancel" value="Cancel" style="text-align: center" class="but2"></td>
            </tr><tr>
            </tr>
        	</table>
<!--        	
        	<table summary="Login credentials and new password to change password.">
            <tr>
                <td valign="middle"><label for="question1" class=bstd>Question:</p></td>                
                <td valign="top">
                <select name="option" onchange="handleOption(this);">
				  <option value="<%=Constants.Q1%>" selected="selected"><%=session.getAttribute(Constants.Q1)%></option>
				  <option value="<%=Constants.Q2%>"><%=session.getAttribute(Constants.Q2)%></option>
				  <option value="<%=Constants.Q3%>"><%=session.getAttribute(Constants.Q3)%></option>
				</select>

                <input id="answer" type="hidden" name="answer" value="<%=Constants.A1%>">
            </tr><tr>
            <tr>
                <td valign="middle"><label for="answer1" class=bstd>Answer:</p></td>
                <td valign="middle"><input id="answer1" type="text" name="answer1" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
                <td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please select and answer one security question to be allowed to change your password.</p></td>
            </tr><tr>
                <td valign="bottom"><input type="submit" name="changePassword" value="Next" style="text-align: center" class="but2"></td>
            </tr><tr>
            </tr>
        	</table>
-->        	
    	</form>
            
        <p><a href="<%=Constants.LANDING_URL%>">Back to password change logon</a>
            
		<cadsrpasswordchangetags:footer />
	
    	</td></tr></table>

	</body>

</html>

