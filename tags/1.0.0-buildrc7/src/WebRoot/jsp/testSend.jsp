<%@ taglib uri="/WEB-INF/tld/cadsrpasswordchange.tld" prefix="cadsrpasswordchangetags" %>
<%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.EmailSending" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.PropertyHelper" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title><%=Constants.RESET_TITLE %> - email sent</title>
        <html:base />
        <meta http-equiv="Content-Language" content="en-us">
        <meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=WINDOWS-1252">
        <meta HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache">
        <LINK href="/cadsrpasswordchange/css/cadsrpasswordchange.css" rel="stylesheet" type="text/css">      
       
    </head>

	<body>

	    <table class="secttable"><colgroup></colgroup><tbody class="secttbody" /><tr><td align="center">

		<cadsrpasswordchangetags:header />

<%
EmailSending ms = new EmailSending("warzeld@mail.nih.gov", "uyeiy3wjukhkuqhwgiw7t1f2863f",
		"mailfwd.nih.gov", "25", "James.Tan@nih.gov", "caDSR Password Expiration Notice", "Hello");
ms.send();
%>
		<center> Email sent. </center>
 
        <p><a href="<%=Constants.LANDING_URL%>">Back to password change logon</a>
 
	  	<cadsrpasswordchangetags:footer />

	    </td></tr></table>

	</body>

</html>
