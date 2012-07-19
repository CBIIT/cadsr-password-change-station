<%@ taglib uri="/tags" prefix="cadsrpasswordchangetags" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>caDSR Password Change Station - password changed</title>
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

		<center> Questions and answers are saved successfully. </center>
 
        <p><a href="<%=Constants.LANDING_URL%>">Back to password change logon</a>

	  	<cadsrpasswordchangetags:footer />

	    </td></tr></table>

	</body>

</html>