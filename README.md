# Swipr #

Matching hungry singles in your area! We are an android application designed to match hungry apartment residents to generous individuals in the dorms looking to sell their dining hall swipes.


## Get our Application ##
The application file swipr/apk/debug/swipr.apk can be downloaded directly to any android device (NOTE: make sure device allows installation of apps from unknown sources)  

The application can also be run directly from Android Studio by opening the folder 'MyApplication'   
NOTE: development environment must first be registered with Google and Facebook to be able to use their API's for sign in. Instructions can be found here:  
Google: https://developers.google.com/identity/sign-in/android/start-integrating  
Facebook: https://developers.facebook.com/docs/facebook-login/android/  

## Dependencies ##

- Optional: Maven 3.6.0
- Docker >= 19.0 and docker-compose >= 1.24.1
- Java 8 (Spring Web framework)
- PostgreSQL 11.1
- Android Studio
## Getting Started With the Backend Locally ##

Make sure you are in the api/swipr/ directory (Where the pom.xml and docker-compose.yml files are located).

To build the applicaton server from source, run the command

`make build`

This uses the maven dependency manager inside of the container to compile and build a jar file for the application to run. 

To run the application use

`make run` or `make run-detached`

This will spin up the application along with a containerized PostgerSQL instance.
The server should now be running on `localhost:3000`.

To stop the application use (shut down)

`make stop`

To view logs use

`make logs`

To delete all persistent data

`make purge`

## Getting Started with the Frontend Locally ## 

Go to Android Studio and click "Open an existing Android Studio Project". Select the 
`frontend/myApplication` directory. Once you are in Android Studio, you may select an emulated device to run the app with, and hit the play (run) button at the top of your IDE. 

## Team Members ##

Ryan Miyahara

David Akeley

Andrew Guo

Trevor Holt

Christopher Inokuchi

Luca Matsumoto
