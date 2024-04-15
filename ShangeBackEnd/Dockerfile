# Starting with this Linux server
FROM maven:3-eclipse-temurin-21

## Build the application
# Create a directory call /app
# go into the directory cd /app
WORKDIR /app

# everything after this is in /app
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# Build the application
RUN mvn package -Dmaven.test.skip=true

## Run the application
# Define environment variable 
ENV SPRING_DATASOOURCE_URL=jdbc:${MYSQL_URL}
ENV SPRING_REDIS_HOST=localhost
ENV SPRING_REDIS_PORT=6379
ENV SPRING_REDIS_USERNAME=NOT_SET
ENV SPRING_REDIS_PASSWORD=NOT_SET

# Expose the port
EXPOSE ${PORT}

# Run the program
ENTRYPOINT SERVER_PORT=${PORT} java -jar target/Shange-0.0.1-SNAPSHOT.jar

######


# FROM node:21 AS ng-builder

# RUN npm i -g @angular/cli

# WORKDIR /ngapp

# COPY ShangeFront/package*.json .
# COPY ShangeFront/angular.json .
# COPY ShangeFront/tsconfig.* .
# COPY ShangeFront/src src

# # RUN npm ci && ng build
# RUN npm ci --legacy-peer-deps && \
#     ng build

# # /ngapp/dist/frontend/browser/*

# # Starting with this Linux server
# FROM maven:3-eclipse-temurin-21 AS sb-builder

# ## Build the application
# # Create a directory call /sbapp
# # go into the directory cd /app
# WORKDIR /sbapp

# # everything after this is in /sbapp
# COPY ShangeBackEnd/mvnw .
# COPY ShangeBackEnd/mvnw.cmd .
# COPY ShangeBackEnd/pom.xml .
# COPY ShangeBackEnd/.mvn .mvn
# COPY ShangeBackEnd/src src
# COPY --from=ng-builder /ngapp/dist/shange-front/browser src/main/resources/static

# # Build the application
# RUN mvn package -Dmaven.test.skip=true

# FROM openjdk:21-jdk-bullseye

# WORKDIR /app 

# COPY --from=sb-builder /sbapp/target/Shange-0.0.1-SNAPSHOT.jar app.jar

# ## Run the application
# # Define environment variable 
# ENV PORT=8080
# # ENV S3_KEY_ACCESS=
# # ENV S3_KEY_SECRET=
# # ENV S3_BUCKET_ENDPOINT=
# # ENV S3_BUCKET_REGION=
# ENV SPRING_DATASOOURCE_URL=jdbc:${MYSQL_URL}
# ENV SPRING_REDIS_HOST=localhost
# ENV SPRING_REDIS_PORT=6379
# ENV SPRING_REDIS_USERNAME=NOT_SET
# ENV SPRING_REDIS_PASSWORD=NOT_SET

# # Expose the port
# EXPOSE ${PORT}

# # Run the program
# ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar



