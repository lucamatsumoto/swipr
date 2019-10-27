.SILENT:

# create a jar file to be executed in Docker container/command line
package:
	mvn clean package -DskipTests

build:
	mvn clean install -DskipTests

run:
	mvn spring-boot:run

# build the jar and docker image
docker-build:
	mvn clean package -DskipTests
	docker-compose build

# runs the application in detached mode
docker-run:
	docker-compose up
	# if you wanna remove the volumes (persistent data)
	# docker-compose rm -v

# shut down docker instance
docker-down:
	docker-compose down 

# see container logs
docker-log:
	docker-compose logs
