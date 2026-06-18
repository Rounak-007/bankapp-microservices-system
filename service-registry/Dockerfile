# Use JDK 17 runtime
FROM eclipse-temurin:17-jre-alpine

# Set the working directory
WORKDIR /app

# Copy the jar from target to the container
# Using a specific destination name ensures it matches your ENTRYPOINT exactly
COPY target/*.jar /app/app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]