FROM eclipse-temurin:21-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app
COPY . .

# Build the application
RUN mvn clean package -DskipTests

# Create an entrypoint script
RUN echo '#!/bin/sh' > /app/entrypoint.sh && \
    echo 'if [ "$SEED_DATA" = "true" ] && [ ! -f "/app/.seeded" ]; then' >> /app/entrypoint.sh && \
    echo '    java -jar target/*.jar --seeder=user' >> /app/entrypoint.sh && \
    echo '    touch /app/.seeded' >> /app/entrypoint.sh && \
    echo 'fi' >> /app/entrypoint.sh && \
    echo 'java -jar target/*.jar' >> /app/entrypoint.sh && \
    chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"] 