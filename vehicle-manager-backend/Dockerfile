# Etapa de build
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

# Etapa de runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Estrutura padrão do quarkus-app (JVM)
COPY --from=build /workspace/target/quarkus-app/lib/ /app/quarkus-app/lib/
COPY --from=build /workspace/target/quarkus-app/*.jar /app/quarkus-app/
COPY --from=build /workspace/target/quarkus-app/app/ /app/quarkus-app/app/
COPY --from=build /workspace/target/quarkus-app/quarkus/ /app/quarkus-app/quarkus/

EXPOSE 8080
ENV JAVA_OPTS="-Xms256m -Xmx512m"

CMD ["java","-jar","/app/quarkus-app/quarkus-run.jar"]
