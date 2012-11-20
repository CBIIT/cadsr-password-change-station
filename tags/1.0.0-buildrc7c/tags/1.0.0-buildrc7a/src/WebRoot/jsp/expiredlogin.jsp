<%@page import="java.sql.*"%>
<%@page import="javax.sql.*"%>
<%@page import="java.util.*"%>
<%@page import="gov.nih.nci.cadsr.cadsrpasswordchange.core.*"%>
<%
String user=request.getParameter("user");
String pwd=request.getParameter("pwd");
Class.forName("oracle.jdbc.driver.OracleDriver");
String jdbcurl = "jdbc:oracle:thin:@ncidb-dsr-d:1551:DSRDEV";
Properties info = new Properties();
info.put( "user", user );
info.put( "password", pwd );
Connection con = null;
try {
	//con = DriverManager.getConnection(jdbcurl, info);	//direct jdbc
		DataSource ds = ConnectionUtil.getDS(PasswordChangeDAO._jndiSystem);
		out.println("got DataSource for " + PasswordChangeDAO._jndiSystem);    	
//        conn = ds.getConnection();
		out.println("got connection from jboss pool [" + PasswordChangeDAO._jndiSystem + "]");
//        con = ds.getConnection(PropertyHelper.getDatabaseUserID(), PropertyHelper.getDatabasePassword());
        con = ds.getConnection(user, pwd);
	
	out.println("login successfully [rc3]");
} catch(Exception e) {
	e.printStackTrace();
	out.println("not able to login: exception = " + e.toString());
} finally {
	if(con != null) {
	  con.close();
	  con = null;
	}
}
%>