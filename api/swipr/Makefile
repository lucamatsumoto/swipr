.SILENT:

# create a jar file to be executed in Docker container/command line
package:
	mvn clean package

build:
	mvn clean install

run:
	mvn spring-boot:run

# build the jar and docker image
docker-build:
	mvn clean package
	docker build -t swipr .

# runs the application in detached mode
docker-run:
	docker run -d -p 5000:8080 swipr

# see container logs
docker-log:
	docker logs swipr
