var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('getUsersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('deleteUserDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('getActiveOffersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('sellQueryDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('refreshOffersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('buyQueryDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('showInterestDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('confirmInterestDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('cancelInterestDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('cancelOfferDiv').style.visibility = connected ? 'visible' : 'hidden';
}

function connect() {
    stompClient = Stomp.client('ws://localhost:3000/index');
    stompClient.debug = null;
    console.log("Hello")
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        // Subscribe to the average price 
        stompClient.subscribe('/user/queue/reply', function(message){
            console.log(message)
        });
        stompClient.subscribe('/topic/average', function(message){
            console.log(message)
        });
        stompClient.subscribe('/user/queue/buyerInterest', function(message) {
            console.log(message)
        });
        stompClient.subscribe('/user/queue/sellerInterest', function(message) {
            console.log(message)
        })
        stompClient.subscribe('/user/queue/buyerFind', function(message) {
            console.log(message)
        });
        stompClient.subscribe('/user/queue/sellerCancel', function(message) {
            console.log(message)
        });
        stompClient.subscribe('/user/queue/sellerUpdate', function(message) {
            console.log(message)
        })
        stompClient.subscribe('/user/queue/error', function(message) {
            console.log(message)
        })
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function createUser() {
    var firstName = document.getElementById('firstName').value;
    var lastName = document.getElementById('lastName').value;
    var email = document.getElementById('email').value;
    var profilePic = document.getElementById('profilePic').value;
    stompClient.send("/swipr/create", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email, profilePicUrl: profilePic }));
}

function getUsers() {
    stompClient.send("/swipr/all", {}, "")
}

function deleteUser() {
    var firstName = document.getElementById('firstNameDelete').value;
    var lastName = document.getElementById('lastNameDelete').value;
    var email = document.getElementById('emailDelete').value;
    stompClient.send("/swipr/delete", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email }));
}

function showGreeting(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}

function addSellQuery() {
    var userId = document.getElementById('sellUserId').value;
    var startTime = document.getElementById('sellStartTime').value;
    var endTime = document.getElementById('sellEndTime').value;
    var price = document.getElementById('sellPrice').value;
    var diningHall = document.getElementById('sellDiningHall').value;
    var offerId = document.getElementById('sellOfferId').value;
    stompClient.send("/swipr/updateOffer", {}, JSON.stringify({'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall), 'offerId': parseInt(offerId)}))
}

function getActiveOffers() {
    stompClient.send("/swipr/getAllOffers")
}

function refreshOffers() {
    stompClient.send("/swipr/refreshOffers")
}

function postBuyQuery() {
    var userId = document.getElementById('buyUserId').value;
    var startTime = document.getElementById('buyStartTime').value;
    var endTime = document.getElementById('buyEndTime').value;
    var price = document.getElementById('buyPrice').value;
    var diningHall = document.getElementById('buyDiningHall').value;
    stompClient.send("/swipr/findOffers", {}, JSON.stringify({'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall)}))
}

function showInterest() {
    var buyerId = document.getElementById('interestUserId').value;
    var userId = document.getElementById('sellInterestUserId').value;
    var startTime = document.getElementById('sellInterestStartTime').value;
    var endTime = document.getElementById('sellInterestEndTime').value;
    var price = document.getElementById('sellInterestPrice').value;
    var diningHall = document.getElementById('sellInterestDiningHall').value;
    var offerId = document.getElementById('sellInterestOfferId').value;
    stompClient.send("/swipr/showInterest", {}, JSON.stringify({'buyerId': parseInt(buyerId), 'sellQuery': {'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall), 'offerId': parseInt(offerId)}}))
}

function cancelInterest() {
    var buyerId = document.getElementById('interestUserId').value;
    var userId = document.getElementById('sellInterestUserId').value;
    var startTime = document.getElementById('sellInterestStartTime').value;
    var endTime = document.getElementById('sellInterestEndTime').value;
    var price = document.getElementById('sellInterestPrice').value;
    var diningHall = document.getElementById('sellInterestDiningHall').value;
    var offerId = document.getElementById('sellInterestOfferId').value;
    stompClient.send("/swipr/cancelInterest", {}, JSON.stringify({'buyerId': parseInt(buyerId), 'sellQuery': {'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall), 'offerId': parseInt(offerId)}}))
}

function confirmInterest() {
    var userId = document.getElementById('confirmUserId').value;
    var firstName = document.getElementById('confirmFirstName').value;
    var lastName = document.getElementById('confirmLastName').value;
    var email = document.getElementById('confirmEmail').value;
    stompClient.send("/swipr/confirmInterest", {}, JSON.stringify({'id': parseInt(userId), 'firstName': firstName, 'lastName': lastName, 'email': email }))
}

function cancelOffer() {
    stompClient.send("/swipr/cancelOffer")
}