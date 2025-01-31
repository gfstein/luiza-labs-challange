# Utiliza a imagem base do OpenJDK 21
FROM openjdk:21-jdk

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo JAR gerado para o container
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar
# Se usar Gradle: COPY build/libs/seu-aplicativo.jar app.jar

# Define um comando para rodar o JAR
ENTRYPOINT ["java", "-jar", "app.jar"]

# Adiciona a porta que sua aplicação está expondo (se aplicável, por exemplo, 8080)
EXPOSE 8080