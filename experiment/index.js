const button = document.getElementById("button")
const info = document.getElementById("info")
const hint = document.getElementById("hint")

button.addEventListener("click", handleClick)

var gameState
var playerClicks
var clickable = false

function greenClick() {
    button.style.backgroundColor = "rgb(0, 183, 51)";

  setTimeout(function() {
    button.style.backgroundColor = "";
    clickable = true
  }, 1000);
}

function redClick() {
    button.style.backgroundColor = "rgb(210,0,0)";
    button.innerHTML = "GAME OVER"

  setTimeout(function() {
    button.style.backgroundColor = "";
    clickable = true

    if(gameState == 0) {
        info.innerHTML = "FINAL ROUND"
        hint.style.visibility = "visible"
        gameState = 1
        button.innerHTML = "PRESS"
    } else {
        location.reload()
    }
  }, 4000);
}

function connectToSkill() {
    setupWebSocket(document.getElementById("ipadress").value)
    document.getElementById("connectionHandler").style.visibility = "hidden"
    gameState = 0 // 0 if trial, 1 if game
    playerClicks = 0
    clickable = true
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
    if(clickable) {
        clickable = false
        if (gameState == 0) {
            // Trial
            let r = Math.floor(Math.random() * 3)
    
            if (webSocket != undefined) {
                if (r == 0 && playerClicks > 1) {
                    redClick()
                    webSocket.send("event TrialGameOver")
                    playerClicks = 0
                } else {
                    greenClick()
                    webSocket.send("event TrialButtonPressed")
                    playerClicks += 1
                }
            }
        } else {
            // Real Game
            if (webSocket != undefined) {
                if (playerClicks >= 8) {
                    redClick()
                    webSocket.send("event GameOver")
                } else {
                    greenClick()
                    webSocket.send("event ButtonPressed")
                    playerClicks += 1
                }
            }
        }
    }
}