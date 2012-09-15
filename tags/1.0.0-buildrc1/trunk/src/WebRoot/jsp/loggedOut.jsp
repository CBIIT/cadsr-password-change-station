<%@ taglib uri="/WEB-INF/tld/cadsrpasswordchange.tld" prefix="cadsrpasswordchangetags" %>
<%@ taglib uri="/WEB-INF/tld/Owasp.CsrfGuard.tld" prefix="csrf" %>
<%@ page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.Constants" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title><%=Constants.RESET_TITLE %> - logged out</title>
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
	


		<center> You have been logged out (either at your request or due to an error).<p></center>
		<center><a target="_top" href="<%=Constants.LANDING_URL%>">Return to caDSR Password Change Station</a></center>
 
	  	<cadsrpasswordchangetags:footer />
  	
    	</td></tr></table>

	</body>

</html>
