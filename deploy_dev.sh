set -x

export JAVA_HOME="D:\Program Files\Java\jdk1.8.0_51"
ENV=$1
PROJECT_NAME="kuaizhan-form"

sh ./package.sh t1

scp target/${PROJECT_NAME}.tar.gz root@192.168.44.210:/opt/webapps/
ssh root@192.168.44.210 rm -rfv /opt/webapps/${PROJECT_NAME}
ssh root@192.168.44.210 mkdir -pv /opt/webapps/${PROJECT_NAME}
ssh root@192.168.44.210 tar -zxf /opt/webapps/${PROJECT_NAME}.tar.gz -C /opt/webapps/${PROJECT_NAME}
ssh root@192.168.44.210 sh /opt/webapps/${PROJECT_NAME}/shell/restart.sh
