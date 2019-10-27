.SILENT:

# build the app, compile dependencies etc. 
build:
	docker-compose build

# Deletes volumes/persistent data
purge:
	docker-compose rm -v

# run the application
run:
	docker-compose up

# run the application in detached mode so the app runs in the background
run-detached:
	docker-compose up -d

# Shut down the application
stop:
	docker-compose down 

# view application logs
logs:
	docker-compose logs


