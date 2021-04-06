# Schedule Sharing Backend
Schedule Sharing Spring RESTful API based on Spring, Spring Boot, JPA Hibernate ORM with MySQL, Spring HATEOAS, Spring REST DOCS, JWT, AWS EC2

## Requirements

1. Java - OpenJDK 11.x.x
2. Spring - 2.4.x
3. MySQL - 8.0.xx
4. ORM - JPA Hibernate

## Steps to Setup

**1. Repository 클론**

```bash
git clone https://github.com/schedule-sharing/schedule-sharing-backend
```

**2. MySQL 데이터베이스 생성**
```bash
create database schedule_sharing
```

**3. MySQL username과 password 변경**
+ `src/main/resources/application.yml` 파일 열기
+ `spring.datasource.username` and `spring.datasource.password` 본인의 MySQL 설정에 맞게 변경

**4. Build and Run**
```bash
./gradlew build
cd build/libs
java -jar schedule-0.0.1-SNAPSHOT.jar
```

http://localhost:8080 으로 접속하여 애플리케이션이 실행되는 것을 확인할 수 있습니다.

## REST API 보기
REST API 문서를 보고 싶으면 http://localhost:8080/docs/index.html 에서 확인할 수 있습니다.
POSTMAN이나 다른 클라이언트를 통해 테스트해볼 수 있습니다.
