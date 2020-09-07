#!/usr/bin/env bash

BIN_DIR=`dirname $0`

profiles=$2
PROJECT_DIR=$(cd $BIN_DIR/.. && pwd)
CONF_DIR=$PROJECT_DIR/conf
LIB_DIR=$PROJECT_DIR/lib
LOG_DIR=$PROJECT_DIR/logs

export PRO_PATH=$PROJECT_DIR
export ModuleName="framework-hbase-search"

packageName=${PROJECT_DIR##*/}

start() {
#	cd target
    echo "start ${ModuleName}..."

    if [ ! -d $LOG_DIR ]; then
        mkdir -p $LOG_DIR
        echo mkdir -p $LOG_DIR
    fi

    nohup java -Dloader.path=$LIB_DIR,$CONF_DIR \
    -jar $LIB_DIR/${packageName}.jar -XX:+PrintGCDateStamps -XX:+PrintGCDetails \
    --spring.profiles.active=${profiles} &> $LOG_DIR/${ModuleName}.log &
}
stop() {
    Pid=`ps -ef | grep ${LIB_DIR}/${packageName}.jar | grep -v grep | awk '{print $2}'`
    echo "stop ${ModuleName}..."
    if [ -n "${Pid}" ]
    then
        kill -9 $Pid
        sleep 10s
    fi
}

restart() {
    stop
    start
}

case "$1" in
	start|stop|restart)
  		case "$2" in
  		    dev|prod|test)
  		        $1
  		        ;;
  		    *)
  		        echo $"Usage: $0 {start|stop|restart} {dev|prod|test}"
  		        exit 2
  		esac
		;;
	*)
		echo $"Usage: $0 {start|stop|restart}"
		exit 1
esac