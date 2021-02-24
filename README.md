Spring Boot Coding Dojo
---
## Usage
### Running the executable JAR

`mvn package` creates an executable jar that may be launched directly

```
$ java -jar -Dspring.profiles.active=(local/prod) target/coding-dojo-spring-boot-0.0.1-SNAPSHOT.jar
```
Once started you can access the APIs on port 8080, e.g.
```
$ curl http://localhost:8080/weather?city=Amsterdam
``` 

## REST APIs Endpoints

### Find weather information
```
Get /weather/?city={cityname}
Content-Type: application/json
```

### Get information about system health, configurations, etc.
```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/env
http://localhost:8080/actuator/info
http://localhost:8080/actuator/metrics
```
### To view Swagger 3 API docs
Run the server and browse to 
```
localhost:8080/swagger-ui.html
```

### List of changes done

* Created standard packages to improve project structure to conform with best practices
* Created service layer
* Updated request mapping in controller
* Renamed WeatherEntity
* Created a separate response object to return from controller instead returning entity object
* Changes to use constructor injection instead of field injection
* Removed wildcard imports
* Externalized api url and app id properties instead of hardcoding values in code
* Added tests
* Added validations and error handling
* Added log statements and other minor improvements
* Enabled actuator endpoints
* Added db properties
* Removed unnecessary use of annotations from properties
* Removed duplicate use of annotations from getters and setters
* Improved naming of response classes and fields
* Added OpenApi 3.0 based documentation using swagger
* Added spring security layer
---

Welcome to the Spring Boot Coding Dojo!

### Introduction

This is a simple application that requests its data from [OpenWeather](https://openweathermap.org/) and stores the result in a database. The current implementation has quite a few problems making it a non-production ready product.

### The task

As the new engineer leading this project, your first task is to make it production-grade, feel free to refactor any piece
necessary to achieve the goal.

### How to deliver the code

Please send an email containing your solution with a link to a public repository.

>**DO NOT create a Pull Request with your solution** 

### Footnote
It's possible to generate the API key going to the [OpenWeather Sign up](https://openweathermap.org/appid) page.
