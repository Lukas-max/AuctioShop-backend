FROM openjdk:11
ADD ShopBackend.jar ShopBackend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ShopBackend.jar"]
