# Favourite recipes

## Installation
1. Install Java 17 - https://adoptium.net/installation/
2. Install PostgreSQL - https://www.postgresql.org/
  - Use docker-compose.yml, it contains PostgreSQL(5432) and Adminer(8282)
  - Manually download and install appropriate version
3. Clone repository - https://github.com/iSharipov/krusty-krab-recipes.git
4. Run Gradle Wrapper command:
```
./gradlew clean build
```
5. Go to:
```
/build/libs
```
6. Run:
```
java -jar .\krusty-krab-recipes-1.0-SNAPSHOT.jar
```

## Documentation
http://localhost:8080/swagger-ui/index.html

## Search operations
```
    CONTAINS, DOES_NOT_CONTAIN, EQUAL, NOT_EQUAL, BEGINS_WITH, DOES_NOT_BEGIN_WITH, ENDS_WITH,
    DOES_NOT_END_WITH, NUL, NOT_NULL, GREATER_THAN, GREATER_THAN_EQUAL, LESS_THAN, LESS_THAN_EQUAL,
    ANY, ALL;
    
    "cn", "nc", "eq", "ne", "bw", "bn", "ew",
            "en", "nu", "nn", "gt", "ge", "lt", "le"
```

## Customization
App supports PostgreSQL and MySQL
Add your own application.yml file to the same level with the jar file, so /build/libs and rewrite the necessary 
settings 
```
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/recipes
    driverClassName: com.mysql.jdbc.Driver
    username: ${username}
    password: ${password}
```