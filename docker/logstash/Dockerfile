FROM docker.elastic.co/logstash/logstash:7.4.2
USER root
RUN yum install -y wget && yum install -y sudo && yum install -y ntpdate && mkdir driver &&\
cd driver &&\ 
wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-8.0.18.tar.gz -O jdbc_driver &&\
tar -xvzf jdbc_driver --strip-components 1 &&\
mv mysql-connector-java-8.0.18.jar /usr/share/logstash/logstash-core/lib/jars/