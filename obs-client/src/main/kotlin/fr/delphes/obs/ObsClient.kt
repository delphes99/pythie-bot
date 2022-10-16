package fr.delphes.obs

import fr.delphes.obs.fromObs.EventPayload
import fr.delphes.obs.fromObs.FromOBSMessagePayload
import fr.delphes.obs.fromObs.Hello
import fr.delphes.obs.fromObs.Identified
import fr.delphes.obs.fromObs.RequestResponse
import fr.delphes.obs.fromObs.event.CurrentProgramSceneChanged
import fr.delphes.obs.fromObs.event.SceneItemEnableStateChanged
import fr.delphes.obs.fromObs.event.SceneItemSelected
import fr.delphes.obs.fromObs.event.SourceFilterEnableStateChanged
import fr.delphes.obs.message.Message
import fr.delphes.obs.toObs.IdentifyPayload
import fr.delphes.obs.toObs.RequestPayload
import fr.delphes.obs.toObs.ToObsMessageType
import fr.delphes.obs.toObs.request.RequestDataPayload
import fr.delphes.utils.toBase64
import fr.delphes.utils.toSha256
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.ClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging

class ObsClient(
    private val configuration: Configuration,
    private val listeners: ObsListener,
    private val serializer: Json
) {
    private val requestsToSend = Channel<RequestDataPayload>()
    private val scope = CoroutineScope(Dispatchers.Default)
    private var connected = false

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(serializer)
        }
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
        }
    }

    fun listen() {
        scope.launch {
            try {
                while (coroutineContext.isActive) {
                    connect()
                    LOGGER.info { "Restart connection" }
                }
            } catch (e: CancellationException) {
                LOGGER.info(e) { "Listen cancelled" }
            } catch (e: Exception) {
                LOGGER.error(e) { "OBS Error" }
                listeners.onError(e.message ?: "OBS Error")
            }
        }
    }

    suspend fun connect() {
        httpClient.ws(
            method = HttpMethod.Get,
            host = configuration.host,
            port = configuration.port,
        ) {
            launch {
                while (isActive) {
                    sendRequest(requestsToSend.receive())
                }
            }

            receiveFrames()
        }
    }

    fun disconnect() {
        scope.coroutineContext.cancel()
    }

    suspend fun sendRequest(request: RequestDataPayload) {
        LOGGER.debug { "Sending ${request.requestType}" }
        requestsToSend.send(request)
    }

    private suspend fun ClientWebSocketSession.sendRequest(request: RequestDataPayload) {
        val requestMessage = ToObsMessageType.buildMessageFrom(RequestPayload.toToObsMessagePayload(request, serializer), serializer)

        send(requestMessage.toFrame())
    }

    private suspend fun ClientWebSocketSession.receiveFrames() {
        var reconnect: Reconnect
        LOGGER.info { "Start receiving frames" }
        do {
            reconnect = try {
                handle(incoming.receive())
            } catch (e: ClosedReceiveChannelException) {
                connected = false
                Reconnect.RECONNECT
            } catch (e: Exception) {
                LOGGER.error(e) { "Error receiving frame" }
                Reconnect.CONTINUE
            }
        } while (isActive && reconnect != Reconnect.RECONNECT)
        LOGGER.info { "End listen" }
        listeners.onExit()
    }

    private suspend fun ClientWebSocketSession.handle(frame: Frame): Reconnect {
        when (frame) {
            is Frame.Text -> {
                val text = frame.readText()
                LOGGER.debug { "Message received : $text" }
                try {
                    when (val message = serializer.decodeFromString<FromOBSMessagePayload>(text)) {
                        is Hello -> {
                            LOGGER.debug { "└ Hello received" }
                            val authenticationString =
                                message.d.authentication?.let { configuration.password?.toAuthenticationString(it.salt, it.challenge) }
                            val identifyMessage = ToObsMessageType.buildMessageFrom(IdentifyPayload(authenticationString), serializer)

                            send(identifyMessage.toFrame())
                        }

                        is Identified -> {
                            LOGGER.info { "└ Identified" }
                            connected = true
                        }

                        is EventPayload -> {
                            LOGGER.debug { "└ Event received ${message.d.eventType} (Intent : ${message.d.eventIntent})" }
                            when (val eventPayload = message.d) {
                                is CurrentProgramSceneChanged -> {
                                    listeners.onSwitchScene(eventPayload)
                                }

                                is SourceFilterEnableStateChanged -> {
                                    listeners.onSourceFilterEnableStateChanged(eventPayload)
                                }

                                is SceneItemSelected,
                                is SceneItemEnableStateChanged, -> {
                                    //Nothing
                                }
                            }
                        }

                        is RequestResponse -> {
                            LOGGER.debug { "└ Request response received ${message.d.requestType}" }
                        }
                    }
                } catch (e: Exception) {
                    LOGGER.error(e) { "└ error when handling message" }
                }
            }

            is Frame.Close -> {
                LOGGER.error { "Obs closing connection : ${frame.readReason()}" }
            }

            else -> {
                LOGGER.info { "Unmanaged frame type ${frame.frameType.name}" }
            }
        }
        return Reconnect.CONTINUE
    }

    private fun Message.toFrame() = Frame.Text(serializer.encodeToString(this))

    private fun String.toAuthenticationString(salt: String?, challenge: String?): String {
        val secret = (this + salt).toSha256().toBase64()
        return (secret + challenge).toSha256().toBase64()
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }

    enum class Reconnect {
        RECONNECT, CONTINUE
    }
}
