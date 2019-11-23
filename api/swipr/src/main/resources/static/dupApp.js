var stompClient = null;
var dupStompClient = null;

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
    document.getElementById('requestAverageDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('cancelOfferDiv').style.visibility = connected ? 'visible' : 'hidden';
}

function dupSetConnected(connected) {
    document.getElementById('dupConnect').disabled = connected;
    document.getElementById('dupDisconnect').disabled = !connected;
    document.getElementById('dupConversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupGetUsersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupDeleteUserDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupGetActiveOffersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupSellQueryDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupRefreshOffersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupBuyQueryDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupShowInterestDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupConfirmInterestDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupCancelInterestDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupRequestAverageDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('dupCancelOfferDiv').style.visibility = connected ? 'visible' : 'hidden';
}

function connect() {
    stompClient = Stomp.client('ws://localhost:3000/index');
    stompClient.debug = null;
    console.log("Hello")
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/reply', function(message) {
            console.log(message)
            showOutput(message)
        });
        stompClient.subscribe('/queue/buyerFind', function(message) {
            console.log(message)
            showOutput(message)
        });
        stompClient.subscribe('/topic/average', function(message) {
            console.log(message)
            showOutput(message)
        });
        stompClient.subscribe('/user/queue/buyerInterest', function(message) {
            console.log(message)
            showOutput(message)
        });
        stompClient.subscribe('/user/queue/sellerInterest', function(message) {
            console.log(message)
            showOutput(message)
        })
        stompClient.subscribe('/user/queue/buyerFind', function(message) {
            console.log(message)
            showOutput(message)
        });
        stompClient.subscribe('/user/queue/sellerCancel', function(message) {
            console.log(message)
            showOutput(message)
        });
        stompClient.subscribe('/user/queue/sellerUpdate', function(message) {
            console.log(message)
            showOutput(message)
        })
        stompClient.subscribe('/user/queue/error', function(message) {
            console.log(message)
            showOutput(message)
        })
    });
}

function dupConnect() {
    dupStompClient = Stomp.client('ws://localhost:3000/index');
    dupStompClient.debug = null;
    console.log("Hello")
    dupStompClient.connect({}, function(frame) {
        dupSetConnected(true);
        console.log('Connected: ' + frame);
        dupStompClient.subscribe('/user/queue/reply', function(message) {
            console.log(message)
            dupShowOutput(message)
        });
        dupStompClient.subscribe('/queue/buyerFind', function(message) {
            console.log(message)
            dupShowOutput(message)
        });
        dupStompClient.subscribe('/topic/average', function(message) {
            console.log(message)
            dupShowOutput(message)
        });
        dupStompClient.subscribe('/user/queue/buyerInterest', function(message) {
            console.log(message)
            dupShowOutput(message)
        });
        dupStompClient.subscribe('/user/queue/sellerInterest', function(message) {
            console.log(message)
            dupShowOutput(message)
        })
        dupStompClient.subscribe('/user/queue/buyerFind', function(message) {
            console.log(message)
            dupShowOutput(message)
        });
        dupStompClient.subscribe('/user/queue/sellerCancel', function(message) {
            console.log(message)
            dupShowOutput(message)
        });
        dupStompClient.subscribe('/user/queue/sellerUpdate', function(message) {
            console.log(message)
            dupShowOutput(message)
        })
        dupStompClient.subscribe('/user/queue/error', function(message) {
            console.log(message)
            dupShowOutput(message)
        })
    });
}

oldLastChild = null;

function showOutput(message) {
    last = document.getElementById("lastOutput");
    if (oldLastChild != null) {
        last.removeChild(oldLastChild);
    }
    oldLastChild = document.createTextNode(message);
    last.appendChild(oldLastChild);
    log = document.getElementById("logOutput");
    log.appendChild(document.createTextNode(message));
}

dupOldLastChild = null;

function dupShowOutput(message) {
    last = document.getElementById("dupLastOutput");
    if (dupOldLastChild != null) {
        last.removeChild(dupOldLastChild);
    }
    dupOldLastChild = document.createTextNode(message);
    last.appendChild(dupOldLastChild);
    log = document.getElementById("dupLogOutput");
    log.appendChild(document.createTextNode(message));
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function dupDisconnect() {
    if (dupStompClient != null) {
        dupStompClient.disconnect();
    }
    dupSetConnected(false);
    console.log("Disconnected");
}

function createUser() {
    var firstName = document.getElementById('firstName').value;
    var lastName = document.getElementById('lastName').value;
    var email = document.getElementById('email').value;
    var profilePic = document.getElementById('profilePic').value;
    stompClient.send("/swipr/create", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email, profilePicUrl: profilePic }));
}

function dupCreateUser() {
    var firstName = document.getElementById('dupFirstName').value;
    var lastName = document.getElementById('dupLastName').value;
    var email = document.getElementById('dupEmail').value;
    var profilePic = document.getElementById('dupProfilePic').value;
    dupStompClient.send("/swipr/create", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email, profilePicUrl: profilePic }));
}

function getUsers() {
    stompClient.send("/swipr/all", {}, "")
}

function dupGetUsers() {
    dupStompClient.send("/swipr/all", {}, "")
}

function deleteUser() {
    var firstName = document.getElementById('firstNameDelete').value;
    var lastName = document.getElementById('lastNameDelete').value;
    var email = document.getElementById('emailDelete').value;
    stompClient.send("/swipr/delete", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email }));
}

function dupDeleteUser() {
    var firstName = document.getElementById('dupFirstNameDelete').value;
    var lastName = document.getElementById('dupLastNameDelete').value;
    var email = document.getElementById('dupEmailDelete').value;
    dupStompClient.send("/swipr/delete", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email }));
}

function showGreeting(message) {
    var response = document.getElementById('response');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.appendChild(document.createTextNode(message));
    response.appendChild(p);
}

function dupShowGreeting(message) {
    var response = document.getElementById('dupResponse');
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

function dupAddSellQuery() {
    var userId = document.getElementById('dupSellUserId').value;
    var startTime = document.getElementById('dupSellStartTime').value;
    var endTime = document.getElementById('dupSellEndTime').value;
    var price = document.getElementById('dupSellPrice').value;
    var diningHall = document.getElementById('dupSellDiningHall').value;
    var offerId = document.getElementById('dupSellOfferId').value;
    dupStompClient.send("/swipr/updateOffer", {}, JSON.stringify({'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall), 'offerId': parseInt(offerId)}))
}

function getActiveOffers() {
    stompClient.send("/swipr/getAllOffers")
}

function dupGetActiveOffers() {
    dupStompClient.send("/swipr/getAllOffers")
}

function refreshOffers() {
    stompClient.send("/swipr/refreshOffers")
}

function dupRefreshOffers() {
    dupStompClient.send("/swipr/refreshOffers")
}

function requestAverage() {
    stompClient.send("/swipr/averageOffer")
}

function dupRequestAverage() {
    dupStompClient.send("/swipr/averageOffer")
}

function postBuyQuery() {
    var userId = document.getElementById('buyUserId').value;
    var startTime = document.getElementById('buyStartTime').value;
    var endTime = document.getElementById('buyEndTime').value;
    var price = document.getElementById('buyPrice').value;
    var diningHall = document.getElementById('buyDiningHall').value;
    stompClient.send("/swipr/findOffers", {}, JSON.stringify({'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall)}))
}

function dupPostBuyQuery() {
    var userId = document.getElementById('dupBuyUserId').value;
    var startTime = document.getElementById('dupBuyStartTime').value;
    var endTime = document.getElementById('dupBuyEndTime').value;
    var price = document.getElementById('dupBuyPrice').value;
    var diningHall = document.getElementById('dupBuyDiningHall').value;
    dupStompClient.send("/swipr/findOffers", {}, JSON.stringify({'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall)}))
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

function dupShowInterest() {
    var buyerId = document.getElementById('dupInterestUserId').value;
    var userId = document.getElementById('dupSellInterestUserId').value;
    var startTime = document.getElementById('dupSellInterestStartTime').value;
    var endTime = document.getElementById('dupSellInterestEndTime').value;
    var price = document.getElementById('dupSellInterestPrice').value;
    var diningHall = document.getElementById('dupSellInterestDiningHall').value;
    var offerId = document.getElementById('dupSellInterestOfferId').value;
    dupStompClient.send("/swipr/showInterest", {}, JSON.stringify({'buyerId': parseInt(buyerId), 'sellQuery': {'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall), 'offerId': parseInt(offerId)}}))
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

function dupCancelInterest() {
    var buyerId = document.getElementById('dupInterestUserId').value;
    var userId = document.getElementById('dupSellInterestUserId').value;
    var startTime = document.getElementById('dupSellInterestStartTime').value;
    var endTime = document.getElementById('dupSellInterestEndTime').value;
    var price = document.getElementById('dupSellInterestPrice').value;
    var diningHall = document.getElementById('dupSellInterestDiningHall').value;
    var offerId = document.getElementById('dupSellInterestOfferId').value;
    dupStompClient.send("/swipr/cancelInterest", {}, JSON.stringify({'buyerId': parseInt(buyerId), 'sellQuery': {'userId': parseInt(userId), 'timeRangeStart': parseInt(startTime), 'timeRangeEnd': parseInt(endTime), 'priceCents': parseInt(price), 'diningHallBitfield': parseInt(diningHall), 'offerId': parseInt(offerId)}}))
}

function confirmInterest() {
    var userId = document.getElementById('confirmUserId').value;
    var firstName = document.getElementById('confirmFirstName').value;
    var lastName = document.getElementById('confirmLastName').value;
    var email = document.getElementById('confirmEmail').value;
    stompClient.send("/swipr/confirmInterest", {}, JSON.stringify({'id': parseInt(userId), 'firstName': firstName, 'lastName': lastName, 'email': email }))
}

function dupConfirmInterest() {
    var userId = document.getElementById('dupConfirmUserId').value;
    var firstName = document.getElementById('dupConfirmFirstName').value;
    var lastName = document.getElementById('dupConfirmLastName').value;
    var email = document.getElementById('dupConfirmEmail').value;
    dupStompClient.send("/swipr/confirmInterest", {}, JSON.stringify({'id': parseInt(userId), 'firstName': firstName, 'lastName': lastName, 'email': email }))
}

function cancelOffer() {
    stompClient.send("/swipr/cancelOffer")
}

function dupCancelOffer() {
    dupStompClient.send("/swipr/cancelOffer")
}
