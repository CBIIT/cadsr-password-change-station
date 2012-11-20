:set JAVA_HOME=C:\jdk1.5.0_22
set JAVA_HOME=C:\jdk1.6.0_34
set ANT_HOME=C:\apache-ant-1.7.1
set PATH=%ANT_HOME%\bin;%JAVA_HOME%\bin;%PATH%

:ant project -DlogLevel=DEBUG -DconnectionUrl="jdbc:oracle:thin:@ncidb-dsr-d:1551:DSRDEV" -DsystemAccountName=cadsrpasswordchange -DsystemAccountPassword=cadsrpasswordchange

:ant project -DlogLevel=DEBUG -DconnectionUrl="jdbc:oracle:thin:@137.187.181.4:1551:DSRDEV" -DsystemAccountName=TANJ -DsystemAccountPassword=TANJ

ant project -DlogLevel=DEBUG -DconnectionUrl="jdbc:oracle:thin:@137.187.181.4:1551:DSRDEV" -DsystemAccountName=cadsrpasswordchange -DsystemAccountPassword=cadsrpasswordchange

:SELECT USERNAME, PROFILE, ACCOUNT_STATUS FROM DBA_USERS where username in ('TANJ', 'SBREXT', 'cadsrpasswordchange');