<%@ page isELIgnored ="false" %>
<!--
%@taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
-->

<%@ taglib uri="/tags" prefix="cadsrpasswordchangetags" %>
<%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants" %>
<%@ page import="java.util.*" %>

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
<%
if(request.getSession() != null) {
	List questionsList = (ArrayList)request.getSession().getAttribute("questionsList");
	if(questionsList != null) {
	String selectedQuestion1 = (String)request.getSession().getAttribute("selectedQuestion1");
	//questionsList
	for (Iterator j = questionsList.iterator(); j.hasNext(); ) {
		String question = (String)j.next();
%>	
        <option value="<%=question%>" <%=question.equals(selectedQuestion1) ? "selected" : ""%>><%=question%></option>
<%
	}
	}
}
%>
</select>    
                </td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Answer1" class=bstd>Answer 1:</p></td>
                <td valign="middle"><input id="Answer1" type="text" name="answer1" value="${param.answer1}" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Question2" class=bstd>Question 2:</p></td>
                <td valign="middle">
<!--                
                <input id="Question2" type="text" name="question2" value="${param.question2}" style="width: 3.75in" class="std" autocomplete="off"></td>
<select name="question2" id="Question2">
	<option value="">-- Please Select a Question --</option>
    <c:forEach items="${questionsList}" var="question">
        <option value="${question}" ${question == selectedQuestion2 ? 'selected' : ''}>${question}</option>
    </c:forEach>
</select>
-->
<select name="question2" id="Question2">
	<option value="">-- Please Select a Question --</option>
<%    
if(request.getSession() != null) {
	List questionsList2 = (ArrayList)request.getSession().getAttribute("questionsList");
	if(questionsList2 != null) {	
	String selectedQuestion2 = (String)request.getSession().getAttribute("selectedQuestion2");
	//questionsList
	for (Iterator j = questionsList2.iterator(); j.hasNext(); ) {
		String question = (String)j.next();
%>	
        <option value="<%=question%>" <%=question.equals(selectedQuestion2) ? "selected" : ""%>><%=question%></option>
<%
	}
	}
}
%>
</select>    
			</td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Answer2" class=bstd>Answer 2:</p></td>
                <td valign="middle"><input id="Answer2" type="text" name="answer2" value="${param.answer2}" style="width: 3.75in" class="std" autocomplete="off"></td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Question3" class=bstd>Question 3:</p></td>
                <td valign="middle">
<!--               
                <td valign="middle">
                <input id="Question3" type="text" name="question3" value="${param.question3}" style="width: 3.75in" class="std" autocomplete="off"></td>
<select name="question3" id="Question3">
	<option value="">-- Please Select a Question --</option>
    <c:forEach items="${questionsList}" var="question">
        <option value="${question}" ${question == selectedQuestion3 ? 'selected' : ''}>${question}</option>
    </c:forEach>
</select>
-->
<select name="question3" id="Question3">
	<option value="">-- Please Select a Question --</option>
<%
if(request.getSession() != null) {
	List questionsList3 = (ArrayList)request.getSession().getAttribute("questionsList");
	if(questionsList3 != null) {	
	String selectedQuestion3 = (String)request.getSession().getAttribute("selectedQuestion3");
	//questionsList
	for (Iterator j = questionsList3.iterator(); j.hasNext(); ) {
		String question = (String)j.next();
%>	
        <option value="<%=question%>" <%=question.equals(selectedQuestion3) ? "selected" : ""%>><%=question%></option>
<%
	}
	}
}
%>
</select>    
			</td>
            </tr><tr>
            <tr>
                <td valign="middle"><label for="Answer3" class=bstd>Answer 3:</p></td>
                <td valign="middle"><input id="Answer3" type="text" name="answer3" value="${param.answer3}" style="width: 3.75in" class="std" autocomplete="off"></td>
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