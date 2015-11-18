#!/bin/bash
set -x
JAVA_HOME="/opt/apps/jdk1.8"
PATH=$JAVA_HOME/bin:$PATH
DEPLOY_HOME="/opt/webapps/kuaizhan-form"
MAIN_CLASS="com.kuaizhan.form.FormServer"
LOG_FILE="/opt/logs/kuaizhan/kuaizhan-form-web.log"
mkdir -pv "/opt/logs/kuaizhan/"

JVM_ARGS="-Xms2048m -Xmx2048m -XX:NewSize=512m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=78  -XX:ThreadStackSize=1024"

SERVER_PID=$(ps -eo pid,command | grep "java.\+${MAIN_CLASS}" | grep -v grep | grep -o "^\s*[0-9]\+")

while [ -n "${SERVER_PID}" ]
do
	echo "current Server PID is ${SERVER_PID}"
	kill ${SERVER_PID}
	sleep 0.5
	SERVER_PID=$(ps -eo pid,command | grep "java.\+${MAIN_CLASS}" | grep -v grep | grep -o "^\s*[0-9]\+")
done

echo "Server ${SERVER_PID} is killed!"

cd ${DEPLOY_HOME}
nohup java -cp "lib/*":classes ${JVM_ARGS} ${MAIN_CLASS} 2>&1 >/dev/null &
tail -f ${LOG_FILE}

