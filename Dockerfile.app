FROM alpine:edge

MAINTAINER Dmitry Melnichenko <dmytro.i.am@gmail.com>

RUN apk --update add openjdk8-jre-base && \
    rm -rf /var/cache/apk/*

COPY target/scala-2.11/theapp-assembly-0.1-SNAPSHOT.jar /

EXPOSE 8443

ENTRYPOINT ["/usr/bin/java", "-jar", "/theapp-assembly-0.1-SNAPSHOT.jar"]
