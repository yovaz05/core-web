#!/bin/sh

export JAVA_HOME="/usr/lib/jvm/java-7-oracle"
export PATH="$JAVA_HOME/bin:$PATH:$HOME/bin:/sbin:/usr/sbin"
export CATALINA_HOME="/home/daniel/tool/apache-tomcat-7.0.27/"
export BASEDIR="/home/daniel/tool/apache-tomcat-7.0.27/"
export CLASSPATH="$CATALINA_HOME/common/lib/servlet-api.jar:$CATALINA_HOME/jdbc/ojdbc14.jar:."

cd /home/daniel/tool/apache-tomcat-7.0.27/bin

echo .
echo ..
echo ...
echo ....
echo .....
./shutdown.sh 
rm -r ../work/*
rm -r ../logs/*
rm -r ../temp/*

# mata todos los procesos apache-tomcat
kill `ps -ef | grep apache-tomcat | grep -v grep | awk '{print $2}'`

echo .... waiting...
sleep 2
./startup.sh
sleep 2
echo .... ready.

