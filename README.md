# Image Uploader - Telepítési és Indítási Útmutató

Ez a fájl részletes útmutatót nyújt a Spring Boot alkalmazás Docker konténerben történő telepítéséhez és futtatásához
PostgreSQL adatbázissal. Az alábbi lépések követésével telepítheted és indíthatod az alkalmazást.

## 1. Előfeltételek

Győződj meg arról, hogy a következő eszközök telepítve vannak a gépeden:

- **Docker**: [Docker Telepítési Útmutató](https://docs.docker.com/get-docker/)
- **Docker Compose**: [Docker Compose Telepítési Útmutató](https://docs.docker.com/compose/install/)

## 2. Projekt Struktúra

A projekt gyökérmappájának felépítése:

```bash
project-root/
│
├── infra/
│   └── docker-compose.yml
│
├── src/
│   └── main/
│       └── java/... (alkalmazás forráskód)
│
├── Dockerfile
├── README.md
├── pom.xml (Maven projektfájl)
└── mvnw, mvnw.cmd (Maven Wrapper, ha használod)
```

## 3. Dockerfile

A projekt gyökerében található **Dockerfile** az alkalmazás buildeléséhez és futtatásához használja az Amazon Corretto
17-et:

```dockerfile
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
```

## 4. Docker Compose fájl

Az **infra** mappában található **docker-compose.yml** fájl definiálja a PostgreSQL adatbázis és az alkalmazás
konténereinek konfigurációját:

```yaml
version: '3.9'

services:
  db:
    image: postgres:17
    restart: always
    environment:
      POSTGRES_USER: user12345
      POSTGRES_PASSWORD: password12345
      POSTGRES_DB: image_upload
    ports:
      - "8081:5432"

  app:
    build: ..
    depends_on:
      - db
    environment:
      DB_URL: jdbc:postgresql://db:5432/image_upload
      DB_USER: user12345
      DB_PASS: password12345
      SPRING_PROFILES_ACTIVE: imgscalr
    ports:
      - "8080:8080"
```

## 5. Spring Boot Konfiguráció (application.yml)

Az `application.yml` fájlban az adatbázis beállításait a Docker Compose-ban megadott környezeti változókkal lehet
konfigurálni:

```yaml
spring:
  application:
    name: File Uploader
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:8081/image_upload}
    username: ${DB_USER:user12345}
    password: ${DB_PASS:password12345}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: create
    open-in-view: false
```

## 6. Konténerek Indítása és Építése

1. Nyisd meg a terminált a projekt gyökerében.
2. Futtasd az alábbi parancsot a konténerek létrehozásához és elindításához az **infra** mappában található Docker
   Compose fájl alapján:

   ```bash
   docker-compose -f infra/docker-compose.yml up --build
   ```

Ez a parancs:

- Felépíti az alkalmazás Docker képét.
- Elindítja a PostgreSQL adatbázis és az alkalmazás konténereit.

## 7. Alkalmazás Elérése

Az alkalmazás elérhető lesz a `http://localhost:8080` címen. Az adatbázis kívülről a `localhost:8081` porton érhető el,
míg az alkalmazás a `db:5432` címre hivatkozik a belső hálózaton keresztül.

## 8. Konténerek Leállítása

Ha le szeretnéd állítani a konténereket, futtasd az alábbi parancsot:

```bash
docker-compose -f infra/docker-compose.yml down
```
