:set JAVA_HOME=C:\jdk1.5.0_22
set JAVA_HOME=C:\jdk1.6.0_34


del /q C:\jboss-4.0.5.GA\server\default\deploy\*.war

del /q C:\jboss-4.0.5.GA\server\default\work

del /q C:\jboss-4.0.5.GA\server\default\tmp

:copy "c:\Workspaces\Eclipse 3.7 Java EE\cadsrpasswordchange\dist\cadsrpasswordchange.war" c:\jboss-4.0.5.GA\server\default\deploy\

copy "C:\Workspaces\demo\cadsrpasswordchange\dist\cadsrpasswordchange.war" c:\jboss-4.0.5.GA\server\default\deploy\

:pause

C:\jboss-4.0.5.GA\bin\run.bat