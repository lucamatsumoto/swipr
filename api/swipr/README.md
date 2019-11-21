# Backend for Swipr #

## Tech Stack ##
The backend for Swipr is written in Java using the Spring web framework and maven dependency/package manager. We have also containerized the application with docker and docker-compose to ensure uniform version management and ease development process.

## Dependencies ##

Please make sure you have the following before you get started.
- Optional: Maven 3.6.0
- Docker >= 19.0 (Might be difficult for Windows users but there is a way I believe) and docker-compose >= 1.24.1
- Java 8 

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

## Endpoint Documentation ##

The endpoints can be separated into User logic and Offer logic. All objects are returned as strings. You may need to parse the JSON String returned depending on what the backend sends. 

### User Logic ###

#### Objects ####
Here are the primary objects that you will need to know (for JSON).

##### User #####
```json
{
    "firstName": "string", 
    "lastName": "string", 
    "email": "string",
    "venmo": "will be added when user hits update venmo"
}
```

##### Buyer #####
```json
{
    "firstName": "string", 
    "lastName": "string", 
    "email": "string",
    "venmo": "will be added when user hits update venmo"
}
```
##### Seller #####
```json
{
    "firstName": "string", 
    "lastName": "string", 
    "email": "string",
    "venmo": "will be added when user hits update venmo"
}
```

##### BuyQuery #####
```json
{
    "usedIr": "int", 
    "timeRangeStart": "long", 
    "timeRangeEnd": "long",
    "priceCents": "long", 
    "diningHallBitfield": "long"
}
```

##### SellQuery #####
```json
{
    "userId": "int", 
    "timeRangeStart": "long", 
    "timeRangeEnd": "long",
    "priceCents": "long", 
    "diningHallBitfield": "long", 
    "offerId": "long"
}
```

##### Interest ######
```json
{
    "buyerId": "int", 
    "sellQuery": {
        "userId": "int", 
        "timeRangeStart": "long", 
        "timeRangeEnd": "long",
        "priceCents": "long", 
        "diningHallBitfield": "long", 
        "offerId": "long"
    }
}
```

#### Topic `/user/queue/reply` ####
This topic is used for handling all user logic such as creating, updating, and deleting from the database.

#### Endpoints related to `/user/queue/reply` ####
`/swipr/create`: expects a `User` and will return the corresponding `User` object that was created/found in the database.

`/swipr/updateVenmo`: expects a `User` object and will return the corresponding `User` object that was created/found in the database, including the venmo account that was added.

`/swipr/delete`: expects a `User` object and will return a message stating that the user was successfully deleted (String).

#### Topic `/user/queue/error` ####
This topic is used for any error messages that occur from backend transactions.
All users must be subscribed to this topic and the frontend must handle all errors coming from this topic.

#### Topic `/topic/average #### 
This topic is used for calculating the daily average price of a swipe. All users (buyers and sellers) must subscribe to this topic.

#### Endpoints related to `/topic/average` ####
`/swipr/averageOffer`: Used to get the daily average price (as a long) of a swipe. Only calculated when a seller has confirmed the buyer's interest. You may never need to actually call this endpoint.

#### Topic `/user/queue/buyerFind` ####
This topic is used for receiving messages about matched Offers. 

#### Endpoints related to `/user/queue/buyerFind` ####
`/swipr/findOffers`: This expects a `BuyQuery` object to be passed in. The timestamps will be represented in Epoch seconds. The diningHallBitfields are `BPLATE = 1, COVEL = 2, DE_NEVE = 4, FEAST = 8`. The userId is the ID of the buyer returned when the user was created/logged in. This method returns to the buyer a list of matching offers in the following format: `[SellQuery1, SellQuery2, ...]`. Please use this to display a list of offers that were matched to the buyer's parameters.

`/swipr/refreshOffers`: This endpoint is for manual refresh of offers from the buyer dashboard. It expects and returns the exact same thing that the `/swipr/findOffers` endpoint returns.

#### Topic `/user/queue/buyerInterest` ####
This topic is for receiving a confirmation message of a completed transaction between the seller and buyer. 


#### Note for Buyers ####
All buyers should subscribe to the buyerInterest and buyerFind topics when the buyer tab is opened. If the seller tab is opened after, make sure to unsubscribe. 

#### Topic `/user/queue/sellerUpdate` ####
This topic is used for confirming whether a seller was successfully able to create or update an offer. 

#### Endpoints related to `/user/queue/sellerUpdate` ####
`/swipr/updateOffer`: This endpoint is used for creating or updating a seller's offer. It expects a `SellQuery` object. The OfferID will later be created on the backend but for now, we will need to send an offer ID. This will return an `offer successfully updated` string. 

#### Topic `/user/queue/sellerInterest` ####
This topic is used for getting a list of potential buyers. 

#### Endpoints related to `/user/queue/sellerInterest ####
`/swipr/showInterest`: This endpoint is used for indicating interest in a specific offer. It expects an `Interest` object and returns a list of `Buyer`'s to the corresponding seller ID attached to the offer. The seller will receive this list of buyers and be notified of any additional bids.

`/swipr/cancelInterest`: This endpoint is used for canceling interesting in a specific offer. It also expects an `Interest` object and returns the updated list of `Buyer`'s to the seller.

`/swipr/confirmInterest`: Confirms that the seller is interested in the buyer's bid. Clears the list of potential buyers attached to that seller and returns an empty list of `Buyer`'s to the seller. Additionally, sends a `confirmed` message to the `/user/queue/buyerInterest` topic. 

#### WIP ####
Cancelling Offers, I'm here functionality, Confirm interest still need to be tested. 