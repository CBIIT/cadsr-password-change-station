#~/bin/sh
echo "PWCS in the directory: " $PWD
echo "tag: " $tag
echo "BRANCH_OR_TAG: " $BRANCH_OR_TAG
git pull
if [ $tag != 'origin/master'  ] && [ $tag != 'master' ]; then
#  git checkout tags/$tag
#this is for branch checkout for now
	echo "checkout of" $tag
	git checkout $tag
fi
git pull

# Function to check if wildfly is up #
function wait_for_server() {
  until `/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:9990 ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do
    sleep 1
  done
}

echo "=> build application"

echo ant -file build.xml -Ddebug=$debug -DBRANCH_OR_TAG=${BRANCH_OR_TAG} -DlogLevel=DEBUG -Dtier=$tier -DsystemAccountName=${CADSR_DS_USER} -DsystemAccountPassword=${CADSR_DS_PSWD} -Dhost=$host -DconnectionUrl=${connectionUrl} project
ant -file build.xml -Ddebug=$debug -DBRANCH_OR_TAG=${BRANCH_OR_TAG} -DlogLevel=DEBUG -Dtier=$tier -DsystemAccountName=${CADSR_DS_USER} -DsystemAccountPassword=${CADSR_DS_PSWD} -Dhost=$host -DconnectionUrl=${connectionUrl} project

echo "=> starting wildfly in background"
/opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 &

echo "=> Waiting for the server to boot"
wait_for_server

echo "=> deploying modules"
cp dist/cadsrpasswordchange.war /local/content/cadsrpasswordchange/jboss
cp dist/bin/ojdbc7.jar /local/content/cadsrpasswordchange/bin
cp dist/bin/cadsrnotifypassword.jar dist/bin/commons-lang3-3.4.jar dist/bin/joda-time-2.9.jar dist/bin/log4j-1.2.13.jar dist/bin/mail.jar dist/bin/config.xml dist/bin/log4j.properties /local/content/cadsrpasswordchange/bin
cp dist/bin/ojdbc7.jar autorun11.sh /local/content/cadsrpasswordchange/bin
chmod 755 /local/content/cadsrpasswordchange/bin/autorun11.sh 

/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:9990 --file=dist/jboss/cadsrpasswordchange_modules.cli

echo "=> reloading wildfly"
/opt/wildfly/bin/jboss-cli.sh --connect command=:reload

echo "=> Waiting for the server to reload"
wait_for_server

echo "=> deploying"
/opt/wildfly/bin/jboss-cli.sh -c --controller=localhost:9990 --file=dist/jboss/cadsrpasswordchange_setup_deploy.cli

echo "=> shutting wildfly down"
/opt/wildfly/bin/jboss-cli.sh --connect command=:shutdown

echo "=> starting wildfly in foreground"
/opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0 
