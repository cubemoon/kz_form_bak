set -x

export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_05.jdk/Contents/Home"
ENV=$1
PROJECT_NAME="kuaizhan-form"

sh ./package.sh live

scp target/${PROJECT_NAME}.tar.gz root@192.168.105.136:/opt/webapps/
ssh root@192.168.105.136 mkdir -pv /opt/webapps/${PROJECT_NAME}
ssh root@192.168.105.136 tar -zxf /opt/webapps/${PROJECT_NAME}.tar.gz -C /opt/webapps/${PROJECT_NAME}
ssh root@192.168.105.136 sh /opt/webapps/${PROJECT_NAME}/shell/restart.sh
