package furhatos.app.dartanjang.utils

import furhatos.event.Event
import furhatos.event.EventSystem
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.LinkedHashSet

class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }
    val name = "user${lastId.getAndIncrement()}"
}

object DieConnector {
    fun start() {
        embeddedServer(Netty, port = 5432) {
            install(WebSockets) {
                pingPeriod = Duration.ofSeconds(60)
                timeout = Duration.ofSeconds(15)
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }
            routing {
                val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
                webSocket("/ws") {
                    val thisConnection = Connection(this)
                    println("Adding connection $thisConnection")
                    connections += thisConnection
                    EventSystem.send(SenseDiceConnected())
                    try {
                        while (true) {
                            val frame = incoming.receive()
                            when (frame) {
                                is Frame.Text -> {
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
                                    // do nothing
                                }
                            }
                        }
                    } catch (e: Throwable) {
                        println(e.localizedMessage)
                    } finally {
                        println("Removing connection $thisConnection")
                        connections -= thisConnection
                        EventSystem.send(SenseDiceDisconnected())
                    }
                }
            }
        }.start()
    }
}


class SenseDiceRolling(val diceId: String): Event()

class SenseDiceStable(val diceId: String, val value: Int): Event()

class SenseDiceFakeStable(val diceId: String, val value: Int): Event()

class SenseDiceMoveStable(val diceId: String, val value: Int): Event()

class SenseDiceBatteryLevel(val diceId: String, val level: Int): Event()

class SenseDiceConnected(): Event()

class SenseDiceDisconnected(): Event()