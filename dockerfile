FROM amazoncorretto:17

RUN yum install -y tar gzip

WORKDIR /app

COPY mvnw /app/mvnw
COPY .mvn /app/.mvn
COPY pom.xml /app
COPY src /app/src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/image-uploader-0.0.1-SNAPSHOT.jar"]