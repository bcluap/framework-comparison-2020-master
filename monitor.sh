#!/bin/bash

CONCURRENCY=$1
SLEEP=$2
HEAPS=$3
HEAPX=$4
JVM=$5
MODE=$6


. ~/.sdkman/bin/sdkman-init.sh
sdk use java 14.0.2.${JVM}-adpt

if [ "$MODE" = "EL" ]; then
    TS="MANUAL"
    PTH="hello2"
elif [ "$MODE" = "DE" ]; then
    TS="MANUAL"
    PTH="hello"
else
    TS="IO"
    PTH="hello"
fi

java -XX:NativeMemoryTracking=summary -Dmicronaut.server.thread-selection=$TS -Xms$HEAPS -Xmx$HEAPX -jar ./micronaut-example/target/micronaut-example-0.1.jar &
java -XX:NativeMemoryTracking=summary -Xms$HEAPS -Xmx$HEAPX -jar ./quarkus-example/target/quarkus-app/quarkus-run.jar &

sleep 2

MPID=$(jps|grep micro| awk '{print $1}')
QPID=$(jps|grep quark| awk '{print $1}')

function shutdown {
    kill -9 $MPID $QPID
}
trap shutdown SIGHUP SIGINT SIGTERM SIGABRT SIGUSR1 SIGUSR2 SIGQUIT


watch "echo \"\n\n\nLoad test has concurrency of $CONCURRENCY and $SLEEP ms thread blocking. Thread mode: $MODE\n\n\n\"; \
echo \"Micronaut:\"; \
ab -k -c $CONCURRENCY -t 3 -n 10000000 http://localhost:8081/hello/paul?sleep=$SLEEP 2> @1|grep Requests; \
ps -o rss,nlwp,args -p $MPID; \
echo \"Heap/Class/Thread/Code/Other memory:\"; \
jcmd $MPID VM.native_memory|egrep 'Heap|Class|Code|Thread|Other' |grep committed|cut -d'=' -f3 |  tr -d ')'; \
echo \"\n\nQuarkus:\"; \
ab -k -c $CONCURRENCY -t 3 -n 10000000 http://localhost:8080/$PTH/paul?sleep=$SLEEP 2> @1|grep Requests; \
ps -o rss,nlwp,args -p $QPID; \
echo \"Heap/Class/Thread/Code/Other memory:\"; \
jcmd $QPID VM.native_memory|egrep 'Heap|Class|Code|Thread|Other' |grep committed|cut -d'=' -f3 |  tr -d ')'"



