function webSocketError() {
    document.getElementById('connectedStatus').style.display = "none"
    document.getElementById('disconnectedStatus').style.display = "inline"
}

function connectWebSocket() {
    setupWebSocket(document.getElementById('ipaddress').value)
}

function setupWebSocket(ip) {
    webSocket = new WebSocket("ws://" + ip + ":5432/ws");

    webSocket.onerror = function () {
        console.log("Websocket Error")
    };

    webSocket.onopen = function () {
        console.log("Websocket connected to skill")
        document.getElementById('disconnectedStatus').style.display = "none"
        document.getElementById('connectedStatus').style.display = "inline"
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

function skillEvent(message) {
    if (webSocket != undefined) {
        webSocket.send("event " + message)
    }
}
