FROM adoptopenjdk/openjdk8
COPY target/payments.jar payments.jar
EXPOSE 8888
CMD java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar payments.jar
