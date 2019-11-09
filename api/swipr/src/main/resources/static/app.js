var stompClient = null;

function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('getUsersDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('deleteUserDiv').style.visibility = connected ? 'visible' : 'hidden';
}

function connect() {
    stompClient = Stomp.client('ws://localhost:3000/index');
    stompClient.debug = null;
    console.log("Hello")
    stompClient.connect({}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/reply', function(greeting){
            console.log(greeting)
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    var firstName = document.getElementById('firstName').value;
    var lastName = document.getElementById('lastName').value;
    var email = document.getElementById('email').value;
    stompClient.send("/swipr/create", {}, JSON.stringify({ 'firstName': firstName, 'lastName': lastName, 'email': email }));
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
