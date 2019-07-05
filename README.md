Docs: http://localhost:8080/swagger-ui.html#/

Docker database: 
`
docker run --name my-sql-db -e MYSQL_ROOT_PASSWORD=password -e MYSQL_USER=user_test -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=db -p 3306:3306 -d mysql:latest
`