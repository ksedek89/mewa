FROM eclipse-temurin:17-jre-ubi9-minimal
WORKDIR /app
ARG JAR_NAME
ARG JAVA_OPTIONS

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["sh", "-c", "java $JAVA_OPTIONS -jar app.jar"]

COPY docker-entrypoint.sh /docker-entrypoint.sh
COPY $JAR_NAME /app/app.jar

ARG VERSION
LABEL version="${VERSION}"
ENV APP_VERSION ${VERSION}
