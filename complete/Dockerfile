FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY build/libs/micronaut-cache-hazelcast-groovy-*-all.jar micronaut-cache-hazelcast-groovy.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar micronaut-cache-hazelcast-groovy.jar