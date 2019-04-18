#!/bin/bash

echo -e "\nExecuting Auto Run for Password Expiration Notification Tool"

DATE=`date +%Y%m%d`

BASE_DIR=/local/content/cadsrpasswordchange/bin

export BASE_DIR

JAVA_PARMS='-Xms512m -Xmx2048m -XX:MaxMetaspaceSize=512m'

export JAVA_PARMS 

echo "Executing job as `id`"
echo "Executing on `date`"

#find $BASE_DIR/../reports -mtime +20 -exec rm {} \;

CP=.
for x in $BASE_DIR/*.jar
do

CP=$CP:$x

done

CP=$CP:$BASE_DIR/log4j.properties

echo $CP
export CP

echo $JAVA_HOME/bin/java -client $JAVA_PARMS -classpath $BASE_DIR:$CP gov.nih.nci.cadsr.cadsrpasswordchange.core.NotifyPassword $BASE_DIR/config.xml

$JAVA_HOME/bin/java -client $JAVA_PARMS -classpath $BASE_DIR:$CP gov.nih.nci.cadsr.cadsrpasswordchange.core.NotifyPassword $BASE_DIR/config.xml