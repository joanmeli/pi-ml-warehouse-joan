FROM openjdk:11
MAINTAINER group9
VOLUME /tmp
EXPOSE 8083
ADD target/pi-ml-warehouse-0.0.1-SNAPSHOT.jar pi-ml-warehouse.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/pi-ml-warehouse.jar"]
