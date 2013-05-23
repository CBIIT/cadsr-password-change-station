This file created just to test initial repository access for now. 

To test with p6spy, build and run like the following:

./build_with_dev_properties.sh

cp dist/cadsrpasswordchange.war /Applications/jboss-5.1.0.GA/server/default/deploy/ 

cp src/conf/spy.properties /Applications/jboss-5.1.0.GA/server/default/conf

/Applications/jboss-5.1.0.GA/bin/run.sh

****** Prei-requisites ******

p6spy.jar need to be copied into the JBoss's common/lib directory and spy.properties has to be in the classpath (in the case of JBoss 5.1.0 GA, it has to be in server/default/conf).

To confirm that p6spy is loaded correctly, during JBoss startup, one should see the following on the console:

14:19:32,450 ERROR [STDERR] Warning: driver oracle.jdbc.driver.OracleDriver is a real driver in spy.properties, but it has been loaded before p6spy.  p6spy will not wrap these connections.  Either prevent the driver from loading, or try setting 'deregisterdrivers' to true in spy.properties

The spy.log file will be created under the current directory, cadsrpasswordchange/.
