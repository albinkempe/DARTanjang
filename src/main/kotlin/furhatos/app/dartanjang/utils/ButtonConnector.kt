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

object ButtonConnector {
    fun start() {
        embeddedServer(Netty, port = 5433) {
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
                    try {
                        while (true) {
                            val frame = incoming.receive()
                            when (frame) {
                                is Frame.Text -> {
                                    val parts = frame.readText().split(" ").map { it.trim() }.filterNot { it.isEmpty() }
                                    val command = parts.getOrElse(0) {""}
                                    val arg1 = parts.getOrElse(1) {""}
                                    if (command == "event") {
                                        when (arg1) {
                                            "ButtonConnected" -> {
                                                println("Button connected!")
                                                EventSystem.send(ButtonConnected())
                                            }
                                            "ButtonPressed" -> {
                                                println("Button pressed!")
                                                EventSystem.send(ButtonPressed())
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
                    }
                }
            }
        }.start()
    }
}

class ButtonPressed(): Event()

class ButtonConnected(): Event()