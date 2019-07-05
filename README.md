[![Build Status](https://travis-ci.com/awaniak/ADSDemoProject.svg?branch=master)](https://travis-ci.com/awaniak/ADSDemoProject)

[![codecov](https://codecov.io/gh/awaniak/ADSDemoProject/branch/master/graphs/badge.svg)](https://codecov.io/gh/awaniak/ADSDemoProject)

Demo spring boot application

## How to run


Run docker database: 
```
docker run --name my-sql-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=user_test -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=db -p 3306:3306 -d mysql:latest
```
Install project
```
mvn install
```
Run project
```
java -jar target/ADSDemoProject-0.0.1-SNAPSHOT.jar
```

## Rest documentation
Rest documentation avaliable after runinig the project

http://localhost:8080/swagger-ui.html#/

