FROM maven:3.8.5-openjdk-17-slim as maven-jdk
ENV MAVEN_CONFIG=~/.m2

FROM maven-jdk as app-artifact
RUN mkdir -p /usr/local/content/app
ADD ./ /usr/local/content/app
WORKDIR /usr/local/content/app
RUN  mvn package -Dmaven.test.skip

FROM openjdk:17-slim-bullseye
RUN apt-get update && \
    apt-get install -yq tzdata && \
    echo '$TZ' > /etc/timezone && \
    ln -fs /usr/share/zoneinfo/$TZ /etc/localtime && \
	dpkg-reconfigure -f noninteractive tzdata
EXPOSE 8080
COPY --from=app-artifact /usr/local/content/app/target/*.jar springboot-webflux-api.jar
ENTRYPOINT ["java","-jar","/springboot-webflux-api.jar"]
