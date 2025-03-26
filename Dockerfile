FROM amazoncorretto:17-alpine
COPY build/libs/discount-service.jar .
ENTRYPOINT ["java", "-jar", "discount-service.jar"]