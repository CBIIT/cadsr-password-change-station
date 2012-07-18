<%@ page isELIgnored ="false" %>
<%@ taglib uri="/tags" prefix="cadsrpasswordchangetags" %>
<%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title><%=Constants.RESET_TITLE %></title>
        
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
			
		<form name="PasswordChangeForm" action="../../cadsrpasswordchange/saveQuestions" method="POST" focus="userid" title="Use this screen to setup your security questions">
		<input type="hidden" name="<csrf:token-name/>" value="<csrf:token-value/>"/>

            <% if (errorMessage.equals("")) {
            		if (userMessage.equals("")) { %>
        				<p class=std>Use this screen to setup your security questions.</p>
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
                <td valign="middle"><input id="OldPassword" type="password" name="password" value="" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>            
            <tr>
                <td valign="middle"><label for="Question1" class=bstd>Question 1:</p></td>
                <td valign="middle">
                <!--<input id="Question1" type="text" name="question1" value="${param.question1}" style="width: 3.75in" class="std" autocomplete="off">-->
<select name="question1" id="Question1">
	<option value="">-- Please Select a Question --</option>
<option value="What is the last name of your favorite school teacher?">What is the last name of your favorite school teacher?</option>
<option value="What is the name of your favorite sports team?">What is the name of your favorite sports team?</option>
<option value="What is the name of your favorite singer or band?">What is the name of your favorite singer or band?</option>
<option value="What is the name of your favorite television series?">What is the name of your favorite television series?</option>
<option value="What is the last name of your favorite school teacher?">What is the last name of your favorite school teacher?</option>
<option value="What is the name of your favorite restaurant?">What is the name of your favorite restaurant?</option>
<option value="What is the name of your favorite movie?">What is the name of your favorite movie?</option>
<option value="What is the name of your favorite song?">What is the name of your favorite song?</option>
<option value="What is the furthest place to which you have traveled?">What is the furthest place to which you have traveled?</option>
<option value="What is the name of your favorite actor or actress?">What is the name of your favorite actor or actress?</option>
<option value="Who is your personal hero?">Who is your personal hero?</option>
<option value="What is your favorite hobby?">What is your favorite hobby?</option>
<option value="Your mother's first name?">Your mother's first name?</option>
<option value="The city name or town name of your birth?">The city name or town name of your birth?</option>
<option value="A four digit PIN (personal identification number)?">A four digit PIN (personal identification number)?</option>
<option value="What is your least favorite sports team?">What is your least favorite sports team?</option>
<option value="What is your mother's occupation?">What is your mother's occupation?</option>
<option value="What was your SAT score?">What was your SAT score?</option>
<option value="What is your favorite brand of candy?">What is your favorite brand of candy?</option>
<option value="What is your least favorite food?">What is your least favorite food?</option>
<option value="What is your least favorite beverage?">What is your least favorite beverage?</option>
<option value="What was your first pet's name?">What was your first pet's name?</option>
</select>
                </td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Answer1" class=bstd>Answer 1:</p></td>
                <td valign="middle"><input id="Answer1" type="password" name="answer1" value="${param.answer1}" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Question2" class=bstd>Question 2:</p></td>
                <td valign="middle">
<!--                
                <input id="Question2" type="text" name="question2" value="${param.question2}" style="width: 3.75in" class="std" autocomplete="off"></td>
-->
<select name="question2" id="Question2">
	<option value="">-- Please Select a Question --</option>
<option value="What is the last name of your favorite school teacher?">What is the last name of your favorite school teacher?</option>
<option value="What is the name of your favorite sports team?">What is the name of your favorite sports team?</option>
<option value="What is the name of your favorite singer or band?">What is the name of your favorite singer or band?</option>
<option value="What is the name of your favorite television series?">What is the name of your favorite television series?</option>
<option value="What is the last name of your favorite school teacher?">What is the last name of your favorite school teacher?</option>
<option value="What is the name of your favorite restaurant?">What is the name of your favorite restaurant?</option>
<option value="What is the name of your favorite movie?">What is the name of your favorite movie?</option>
<option value="What is the name of your favorite song?">What is the name of your favorite song?</option>
<option value="What is the furthest place to which you have traveled?">What is the furthest place to which you have traveled?</option>
<option value="What is the name of your favorite actor or actress?">What is the name of your favorite actor or actress?</option>
<option value="Who is your personal hero?">Who is your personal hero?</option>
<option value="What is your favorite hobby?">What is your favorite hobby?</option>
<option value="Your mother's first name?">Your mother's first name?</option>
<option value="The city name or town name of your birth?">The city name or town name of your birth?</option>
<option value="A four digit PIN (personal identification number)?">A four digit PIN (personal identification number)?</option>
<option value="What is your least favorite sports team?">What is your least favorite sports team?</option>
<option value="What is your mother's occupation?">What is your mother's occupation?</option>
<option value="What was your SAT score?">What was your SAT score?</option>
<option value="What is your favorite brand of candy?">What is your favorite brand of candy?</option>
<option value="What is your least favorite food?">What is your least favorite food?</option>
<option value="What is your least favorite beverage?">What is your least favorite beverage?</option>
<option value="What was your first pet's name?">What was your first pet's name?</option>
</select>
			</td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Answer2" class=bstd>Answer 2:</p></td>
                <td valign="middle"><input id="Answer2" type="password" name="answer2" value="${param.answer2}" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Question3" class=bstd>Question 3:</p></td>
                <td valign="middle">
<!--               
                <td valign="middle">
                <input id="Question3" type="text" name="question3" value="${param.question3}" style="width: 3.75in" class="std" autocomplete="off"></td>
-->
<select name="question3" id="Question3">
	<option value="">-- Please Select a Question --</option>
<option value="What is the last name of your favorite school teacher?">What is the last name of your favorite school teacher?</option>
<option value="What is the name of your favorite sports team?">What is the name of your favorite sports team?</option>
<option value="What is the name of your favorite singer or band?">What is the name of your favorite singer or band?</option>
<option value="What is the name of your favorite television series?">What is the name of your favorite television series?</option>
<option value="What is the last name of your favorite school teacher?">What is the last name of your favorite school teacher?</option>
<option value="What is the name of your favorite restaurant?">What is the name of your favorite restaurant?</option>
<option value="What is the name of your favorite movie?">What is the name of your favorite movie?</option>
<option value="What is the name of your favorite song?">What is the name of your favorite song?</option>
<option value="What is the furthest place to which you have traveled?">What is the furthest place to which you have traveled?</option>
<option value="What is the name of your favorite actor or actress?">What is the name of your favorite actor or actress?</option>
<option value="Who is your personal hero?">Who is your personal hero?</option>
<option value="What is your favorite hobby?">What is your favorite hobby?</option>
<option value="Your mother's first name?">Your mother's first name?</option>
<option value="The city name or town name of your birth?">The city name or town name of your birth?</option>
<option value="A four digit PIN (personal identification number)?">A four digit PIN (personal identification number)?</option>
<option value="What is your least favorite sports team?">What is your least favorite sports team?</option>
<option value="What is your mother's occupation?">What is your mother's occupation?</option>
<option value="What was your SAT score?">What was your SAT score?</option>
<option value="What is your favorite brand of candy?">What is your favorite brand of candy?</option>
<option value="What is your least favorite food?">What is your least favorite food?</option>
<option value="What is your least favorite beverage?">What is your least favorite beverage?</option>
<option value="What was your first pet's name?">What was your first pet's name?</option>
</select>
			</td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Answer3" class=bstd>Answer 3:</p></td>
                <td valign="middle"><input id="Answer3" type="password" name="answer3" value="${param.answer3}" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
                <td colspan="2" valign="middle"><p class="bstd" style="text-align: center; margin-top: 8pt; margin-bottom: 8pt" id="msg">Please provide questions and anwers that you can remember. Your questions can not be repeated (must be different).</p></td>
            </tr><tr>
                <td valign="bottom">
                <input type="submit" name="changePassword" value="Save" style="text-align: center" class="but2">
                <input type="submit" name="cancel" value="Cancel" style="text-align: center" class="but2">
                </td>
            </tr>
        	</table>
    	</form>
            
		<cadsrpasswordchangetags:footer />
	
    	</td></tr></table>

	</body>

</html>
