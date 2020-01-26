# Password Manager in Spring Boot

[![Build Status](https://jenkins.rcomanne.nl/buildStatus/icon?job=password-manager%2Fmaster)](https://jenkins.rcomanne.nl/job/password-manager/job/master/)

## Description
This is a little project that I'm doing to create my own password generator and manager.  
Right now, it does not do a lot, as authentication is not yet really implemented. But it should be able to:  
1. Register and login with mail/password.  
2. Upload and retrieve passwords from the API and eventually also a GUI.  

## Documentation  
Eventually this project will get a Swagger UI for REST documentation.

## Build & Run  
### Pre reqs  
This service uses MongoDB as a database, so you will have to set up a MongoDB locally, or a remote connection to setup to.  
To install MongoDB visit: [install MongoDB](https://www.mongodb.com/what-is-mongodb)

### Configuration
I like to run my services locally with the 'dev' profile active, so I can put my development credentials in there.  
This file (src/main/resources/application-dev.yaml) is in the .gitignore to prevent leaking of credentials.
For now, the required values to be filled are:
```yaml
jwt.secret: your-jwt-secret
```

### Run
This is a simple Spring Boot application, so you can run this from your favorite IDE, or from the terminal with `mvn spring-boot:run`

### Testing  
There are not a lot of tests yet - only on the validation of some DTO's.

### Consume the service
The flow should be as follows:
- Register an account:
```bash
curl -X POST \
  http://localhost:8080/user/register \
  -H 'Content-Type: application/json' \
  -d '{
	"username": "username",
	"password": "password",
	"mail": "m@a.il"
}'
```
- Login with your account to get a JWT token
```bash
curl -X POST \
  http://localhost:8080/user/login \
  -H 'Content-Type: application/json' \
  -d '{
	"mail": "m@a.il",
	"password": "password"
}'
``` 
- Connect to the endpoint that you want with the retrieved bearer token
```bash
curl -X GET \
  http://localhost:8080/hello \
  -H 'Authorization: Bearer <YOUR_RETRIEVED_BEARER_TOKEN>'
```