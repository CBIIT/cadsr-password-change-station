<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>
<%
String dbuser=request.getParameter("dbuser");
String dbpwd=request.getParameter("dbpwd");
String name=request.getParameter("id");
String a1=request.getParameter("a1");
String a2=request.getParameter("a2");
String a3=request.getParameter("a3");
Class.forName("oracle.jdbc.driver.OracleDriver");
String jdbcurl = "jdbc:oracle:thin:@ncidb-dsr-d:1551:DSRDEV";
Properties info = new Properties();
info.put( "user", dbuser );
info.put( "password", dbpwd );
Connection con = DriverManager.getConnection(jdbcurl, info);
con.setAutoCommit(true);
Statement st=con.createStatement();
String sql = "INSERT INTO SBREXT.USER_SECURITY_QUESTIONS (ua_name,question1,answer1,question2,answer2,question3,answer3,date_modified) VALUES('"+name+"','test q 1','"+a1+"','test q 2','"+a2+"','test q 3','"+a3+"',sysdate)";
System.out.println("SQL executed = [" + sql +"]");
int i=st.executeUpdate(sql);
out.println(i + " data is inserted successfully");
out.println("SQL executed = [" + sql+"] rc2");
%>