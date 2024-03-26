package furhatos.app.godice.util

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.cio.websocket.Frame
import java.time.Duration
import furhatos.event.Event
import furhatos.event.EventSystem
import furhatos.util.CommonUtils
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking

private val logger = CommonUtils.getLogger("GoDiceServer")

object GoDiceServer {

    val connections = mutableListOf<WebSocketSession>()

    val webServerPort = 5432

    fun start() {
        embeddedServer(Netty, webServerPort, module = router).start()
    }

    fun sendWebSocketMessage(text: String) {
        synchronized(connections) {
            connections.toList().forEach {
                try {
                    runBlocking {
                        it.outgoing.send(Frame.Text(text))
                    }
                } catch (_: Throwable) {
                    //println("Removing connection")
                    connections.remove(it)
                    logger.info("Try to send, failed and removed: " + it.hashCode())
                }
            }
        }
    }

    val router = applicationRouter {
        install(CORS) {
            anyHost()
        }
        install(WebSockets) {
            pingPeriod = Duration.ofSeconds(60) // Disabled (null) by default
            timeout = Duration.ofSeconds(15)
            maxFrameSize = Long.MAX_VALUE // Disabled (max value). The connection will be closed if surpassed this length.
            masking = false
        }
        routing {
            webSocket("/ws") {
                val connectionId = this.hashCode()
                try {
                    logger.info("New WebSocket connection $connectionId")
                    //logger.debug("Received WebSocket connection")
                    synchronized(connections) {
                        connections.add(this)
                    }
                    while (true) {
                        val frame = incoming.receive()
                        when (frame) {
                            is Frame.Text -> {
                                // We receieved a command from the web GUI
                                val parts = frame.readText().split(" ").map { it.trim() }.filterNot { it.isEmpty() }
                                val command = parts.getOrElse(0) {""}
                                val arg1 = parts.getOrElse(1) {""}
                                val arg2 = parts.getOrElse(2) {""}
                                val arg3 = parts.getOrElse(3) {""}
                                if (command == "event") {
                                    when (arg1) {
                                        "SenseDiceRolling" -> {
                                            EventSystem.send(SenseDiceRolling(arg2))
                                        }
                                        "SenseDiceStable" -> {
                                            EventSystem.send(SenseDiceStable(arg2, arg3.toInt()))
                                        }
                                        "SenseDiceFakeStable" -> {
                                            EventSystem.send(SenseDiceFakeStable(arg2, arg3.toInt()))
                                        }
                                        "SenseDiceMoveStable" -> {
                                            EventSystem.send(SenseDiceMoveStable(arg2, arg3.toInt()))
                                        }
                                        "SenseDiceBatteryLevel" -> {
                                            EventSystem.send(SenseDiceBatteryLevel(arg2, arg3.toInt()))
                                        }
                                    }
                                }
                            }

                            else -> {
                                // wow
                            }
                        }
                    }
                } catch (e: Throwable) {
                    //e.printStackTrace()
                } finally {
                    logger.info("WebSocket connection lost $connectionId")
                    synchronized(connections) {
                        connections.remove(this)
                    }
                }
            }
        }
    }

}

fun applicationRouter(builder: Application.()->Unit) = builder

class SenseDiceRolling(val diceId: String): Event()

class SenseDiceStable(val diceId: String, val value: Int): Event()

class SenseDiceFakeStable(val diceId: String, val value: Int): Event()

class SenseDiceMoveStable(val diceId: String, val value: Int): Event()

class SenseDiceBatteryLevel(val diceId: String, val level: Int): Event()