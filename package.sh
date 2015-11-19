#!/bin/bash
#set -x

export JAVA_HOME="D:\Program Files\Java\jdk1.8.0_51"
ENV=$1
PROJECT_NAME="kuaizhan-form"

mvn clean  

#mvn -U clean  package -P ${ENV} -Dmaven.test.skip=true &> /dev/null
mvn -U clean  package -P ${ENV} -Dmaven.test.skip=true

if [ ! -f target/${PROJECT_NAME}.tar.gz ];then
    echo "====================================================================================="
    echo "==>Sorry,Compile Failed!"
    echo "====================================================================================="
    exit 1;
fi

echo "================================="
echo "==>OK,Compile Successful."
echo "================================="
echo

###############