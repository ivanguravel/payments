FROM adoptopenjdk/openjdk8
COPY target/money-transfer-*.jar money-transfer.jar
EXPOSE 8888
CMD java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar money-transfer.jar