# Backend for Swipr #

## Tech Stack ##
The backend for Swipr is written in Java using the Spring web framework and maven dependency/package manager. We have also containerized the application to ensure uniform version management and ease development process.

## Dependencies ##

Please make sure you have the following before you get started.
- Optional: Maven 3.6.0
- Docker >= 19.0 (Might be difficult for Windows users but there is a way I believe) and docker-compose >= 1.24.1
- Java 8 (Most well documented/industry standard)

## Getting Started ##
We are heavily relying on Docker and docker-compose to build and run our application. This allows us to reduce the number of dependencies we download, create database/multiple instances of applications simultaneously, and smoothen development. 

To build the applicaton use

`make build`

This uses the maven dependency manager inside of the container to compile and build a jar file for the application to run. Whenever you want to add a library/dependency or change some code, go update the `pom.xml` file and run this command.

To run the application use

`make run` or `make run-detached`

This will spin up the application along with the database it is using.

To stop the application use (shut down)

`make stop`

To view logs use

`make logs`

To delete all persistent data

`make purge`


## Notes for Backend Developers ##

`Application.java` is our main file, we run our backend from this file. You probably won't need to touch it.

`src/main/java/com/swipr`is the directory that contains our code logic. Long and tedious, I know, but Java/Spring is stupid like this sometimes and requires this path form.

`src/main/java/com/swipr/controllers` is the directory where we handle all of the requests. This is essentially the directory where we are going to write our endpoints and handle the corresponding logic. (GET, POST, PUT, DELETE requests)

`src/main/java/com/swipr/models` is the directory where we store all of our object models. Some examples for our case could be a Buyer Object or a Seller Object.

`src/main/java/com/swipr/exceptions` is where we store potential custom exceptions. Might not be needed if we choose not to use it.

`src/main/java/com/swipr/utils` is where any helper classes/functions will go.

`src/main/java/com/swipr/repository` is where the DB transaction logic goes.

`src/main/java/com/swipr/auth` is where the Google/Facebook authentication logic goes.

Feel free to create directories as necessary. This is just a standard organization for Spring Boot apps.

