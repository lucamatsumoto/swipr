# Backend for Swipr #

## Tech Stack ##
The backend for Swipr is written in Java using the Spring web framework and maven dependency/package manager. We have also containerized the application to ensure uniform version management and ease development process.

## Dependencies ##

- Maven 3.6.0
- Optional: Docker >= 19.0
- Java 8 (Most well documented/industry standard)

## Getting Started ##

We have created a Makefile to simplify the process of building and running our app.

To build the app:

`mvn clean install`

To run the app:

`mvn spring-boot:run`

To create a jar file in the targets directory

`mvn clean package`

If you have Docker on your system, 

Build the app

`make docker-build`

Run the app

`make docker-run`

See the docker logs

`make docker-log`

## Notes for Backend Developers ##

`Application.java` is our main file, we run our backend from this file. You probably won't need to touch it.

`src/main/java/com/swipr`is the directory that contains our code logic. Long and tedious, I know, but Java/Spring is stupid like this sometimes and requires this path form.

`src/main/java/com/swipr/controllers` is the directory where we handle all of the requests. This is essentially the directory where we are going to write our endpoints and handle the corresponding logic. (GET, POST, PUT, DELETE requests)

`src/main/java/com/swipr/models` is the directory where we store all of our object models. Some examples for our case could be a Buyer Object or a Seller Object.

`src/main/java/com/swipr/exceptions` is where we store potential custom exceptions. Might not be needed if we choose not to use it.

`src/main/java/com/swipr/utils` is where any helper classes/functions will go.

`src/main/java/com/swipr/service` is pretty much where the business logic goes. Things like DB transactions (if we need them).

Feel free to create directories as necessary. This is just a standard organization for Spring Boot apps.

For now, I have set up two controllers that simply returns a string when we hit that endpoint. Start the application and run `curl localhost:5000/buyer/test` or `curl localhost:5000/seller/test` from your command line or open up `localhost:5000/buyer/test` to see the response. 

