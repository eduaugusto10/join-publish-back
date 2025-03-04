# Etapa de build
FROM maven:3.9-amazoncorretto-21 AS build
WORKDIR /app

# Copia arquivos necessários e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte e compila a aplicação
COPY src ./src
RUN mvn package -DskipTests

# Etapa de execução (imagem menor e sem Maven)
FROM amazoncorretto:21
WORKDIR /app

# Copia o jar gerado na etapa de build
COPY --from=build /app/target/join-0.0.1-SNAPSHOT.jar /app/join-back.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/join-back.jar"]
