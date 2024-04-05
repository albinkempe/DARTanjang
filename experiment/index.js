const button = document.getElementById("button")
const msg = document.getElementById("msg")
button.addEventListener("click", changeColor)
button.addEventListener("click", handleClick)

function changeColor() {
    button.style.backgroundColor = "rgb(38, 45, 50)";
    msg.style.visibility = "visible"

  setTimeout(function() {
    button.style.backgroundColor = "";
    msg.style.visibility = "";
  }, 1000);
}

function connectToSkill() {
    setupWebSocket(document.getElementById("ipadress").value)
    document.getElementById("connectionHandler").style.visibility = "hidden"
}

function webSocketError() {
    document.getElementById('status').style.backgroundColor = "gray"
}

function setupWebSocket(ip) {
    webSocket = new WebSocket("ws://"+ip+":5433/ws");

    webSocket.onerror = function () {
        console.log("Websocket Error")
        document.getElementById('status').style.backgroundColor = "red"
    };

    webSocket.onopen = function () {
        console.log("Websocket connected to skill")
        document.getElementById('status').style.backgroundColor = "green"
        webSocket.send("event ButtonConnected")
    };

    webSocket.onclose = function (evt) {
        console.log("Websocket connection closed")
        webSocketError()
    };

    webSocket.onmessage = function (event) {
        const message = event.data.toString()
        console.log(message)
        parts = message.split(' ')
        command = parts[0]
        if (command == "update") {
            //loadTemplate()
        }
    };
}

var webSocket

function handleClick() {
    if (webSocket != undefined) {
        webSocket.send("event ButtonPressed")
    }
}

//setupWebSocket()