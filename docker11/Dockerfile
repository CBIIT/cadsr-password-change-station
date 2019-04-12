from cbiit/centos7_base
# update packages and install packages for application #
RUN yum -y update && yum -y install git java-11-openjdk-devel sudo util-linux wget epel-release \ 
    && yum clean all

# set env variables
ENV PATH=/opt/apache-maven/bin:$PATH \
    JAVA_HOME=/usr/lib/jvm/java-11-openjdk \
    WILDFLY_VERSION=16.0.0.Final
    
# change to /tmp directory for code downloads #
WORKDIR /tmp
RUN wget https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz \
    && tar xvfz apache-maven-3.5.4-bin.tar.gz \
    && mv apache-maven-3.5.4 /opt \
    && ln -s /opt/apache-maven-3.5.4 /opt/apache-maven
RUN sudo groupadd -r wildfly && sudo useradd -r -g wildfly -d /opt/wildfly -s /sbin/nologin wildfly \ 
    && wget https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz -P /tmp \
    && tar xf /tmp/wildfly-$WILDFLY_VERSION.tar.gz -C /opt/ \
    && ln -s /opt/wildfly-$WILDFLY_VERSION /opt/wildfly \ 
    && chown -RH wildfly: /opt/wildfly
RUN wget http://archive.apache.org/dist/ant/binaries/apache-ant-1.9.9-bin.tar.gz \
    && tar xvfz apache-ant-1.9.9-bin.tar.gz \
    && mv apache-ant-1.9.9 /opt
    
ENV PATH=/opt/apache-maven/bin:/opt/apache-ant-1.9.9/bin:$PATH

# copy entrypoint to /usr/bin and chown to wildfly #
COPY run.sh /usr/bin/run.sh
RUN chown wildfly /usr/bin/run.sh && chmod 700 /usr/bin/run.sh

# create local content directories, change owner to wildfly #
RUN mkdir -p /local/content/cadsrpasswordchange/bin /local/content/cadsrpasswordchange/jboss\
    && chown -RH wildfly /local/content/cadsrpasswordchange

# make directory for pulling code from github and change owner to wildfly #
RUN mkdir /usr/src/cadsrpasswordchange && chown -RH wildfly: /usr/src/cadsrpasswordchange

# change user to wildfly #
USER wildfly

# replace jboss port 9990 with 19990 #
#RUN sed -i 's/9990/19990/g' /opt/wildfly/standalone/configuration/standalone.xml    

# change to source code workdir #
WORKDIR /usr/src/cadsrpasswordchange
RUN git clone -b pwcs-1.0.2-01-java11-20190411 https://github.com/CBIIT/cadsr-password-change-station
WORKDIR /usr/src/cadsrpasswordchange/cadsr-password-change-station

EXPOSE 8080
ENTRYPOINT /usr/bin/run.sh
