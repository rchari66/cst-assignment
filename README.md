# cst-assignment

To Run:

Pre-requisite
* Make sure JIRA service is running and env 'JIRA_BASE_URL' is set.
* Make sure Amazon SQS service is running and env 'QUEUE_URL' is set

## Step 1 : Run JIRA and Amazon SQS
Note: I have used mock_jira_service.go for Jira service. Use below command to run mock jira service.

By default mock_jira_service.go runs on port: 4553. you can change it on line#29.
```
$ go run mock_jira_service.go
# In mock_jira_service.go you can edit line#21 to customize jira service's response
```

Run amazon sqs locally
```
$ docker run -d -p 9324:9324 vsouza/sqs-local
```
Note: If you intend to run Amazon's SQS service; please change value of 
property `spring.profiles.active` from `dev` to `prod` in `application.properties` file. 

Also make sure AWS creadentials are configured appropricately for the cst to service to be able to contact amazon sqs service.

## Step 2 : Set environment variables and run cst jar

cd to project directory & run 
```
$ export JIRA_BASE_URL=http://localhost:4553
$ export QUEUE_URL=http://localhost:9324

# use either of below cmds to run jar
$ java -jar target_jar/cst-0.0.1-SNAPSHOT.jar
 (or)
$ mvn clean install  && java -jar target/cst-0.0.1-SNAPSHOT.jar
```

## Step 3 : Test
Hit CST's end-point:
```
# The below curtl should publish an sqs message to amazon sqs(local)
$ curl -I http://localhost:8080/api/issue/sum?query=type=Bug&name=testing
# HTTP/1.1 200 OK
# ...
```

Test amazon sqs(applied to only when run amazon sqs locally) for messages:
```
# open testsqs(in cst-assigment) project in any IDE and run Test.java class
```
Note: For testing purposes I have hardcoded the queue url in Test.java file @line#60. You can customise it as per your requirements.



### TODO-Enhancements/improvements:
* Create Docker for the service
* Health check has to check health of it's dependent services(like Jira and sqs)
* Add localization for messages
* Improve exception handling(Intercept exceptions and send custom messages to client)
* Add addition mock tests to test jira & sqs services
