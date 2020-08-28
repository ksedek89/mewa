FROM openjdk:11
WORKDIR /app
ARG JAR_NAME
ARG JAVA_OPTIONS

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["sh", "-c", "java $JAVA_OPTIONS -jar app.jar"]

COPY docker-entrypoint.sh /docker-entrypoint.sh
COPY $JAR_NAME /app/app.jar

ARG VERSION
LABEL version="${VERSION}"
