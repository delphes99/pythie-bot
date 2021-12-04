package fr.delphes.obs

import fr.delphes.obs.event.EventType
import fr.delphes.obs.event.SourceFilterVisibilityChanged
import fr.delphes.obs.event.SwitchScenes
import fr.delphes.obs.request.Authenticate
import fr.delphes.obs.request.AuthenticateResponse
import fr.delphes.obs.request.GetAuthRequired
import fr.delphes.obs.request.GetAuthRequiredResponse
import fr.delphes.obs.request.ReceivedMessage
import fr.delphes.obs.request.Request
import fr.delphes.obs.request.Response
import fr.delphes.obs.request.ResponseStatus
import fr.delphes.obs.request.SetSceneItemPropertiesResponse
import fr.delphes.obs.request.SetSourceFilterVisibilityResponse
import fr.delphes.utils.exhaustive
import fr.delphes.utils.serialization.Serializer
import fr.delphes.utils.toBase64
import fr.delphes.utils.toSha256
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.websocket.ClientWebSocketSession
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import mu.KotlinLogging
import kotlin.reflect.KClass

class ObsClient(
    private val configuration: Configuration,
    private val listeners: ObsListener
) {
    private val typeForMessage = mutableMapOf<String, KClass<*>>()
    private val requestsToSend = Channel<Request>()
    private val scope = CoroutineScope(Dispatchers.Default)

    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Serializer)
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
            authenticate()

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

    private suspend fun ClientWebSocketSession.authenticate() {
        sendRequest(GetAuthRequired())
    }

    suspend fun sendRequest(request: Request) {
        requestsToSend.send(request)
    }

    private suspend inline fun <reified T : Request> ClientWebSocketSession.sendRequest(request: T) {
        typeForMessage[request.messageId] = request.responseType
        send(Frame.Text(Serializer.encodeToString(request)))
    }

    private suspend fun ClientWebSocketSession.receiveFrames() {
        var reconnect: Reconnect
        LOGGER.info { "Start receiving frames" }
        do {
            reconnect = try {
                handle(incoming.receive())
            } catch (e: ClosedReceiveChannelException) {
                Reconnect.RECONNECT
            } catch (e: Exception) {
                LOGGER.error(e) { "Error receiving frame" }
                Reconnect.CONTINUE
            }
        } while (isActive && reconnect != Reconnect.RECONNECT)
        LOGGER.info { "End listen" }
    }

    @OptIn(InternalSerializationApi::class)
    private suspend fun ClientWebSocketSession.handle(frame: Frame): Reconnect {
        when (frame) {
            is Frame.Text -> {
                val text = frame.readText()
                try {
                    val receivedMessage = Serializer.decodeFromString<ReceivedMessage>(text)
                    when {
                        receivedMessage.isRequestResponse() -> {
                            val payload = parseResponse(text)
                            when (payload) {
                                is GetAuthRequiredResponse -> {
                                    if (payload.authRequired) {
                                        LOGGER.debug { "OBS required credentials : try to authenticate" }
                                        val secret = (configuration.password + payload.salt).toSha256().toBase64()
                                        val authResponse = (secret + payload.challenge).toSha256().toBase64()

                                        sendRequest(Authenticate(authResponse))
                                    }
                                    Unit
                                }
                                is AuthenticateResponse -> {
                                    if (payload.status == ResponseStatus.error) {
                                        listeners.onError("OBS authentication failed : ${payload.error}")
                                    }
                                    handleError(payload)
                                }
                                is SetSceneItemPropertiesResponse -> handleError(payload)
                                is SetSourceFilterVisibilityResponse -> handleError(payload)
                            }.exhaustive()
                        }
                        receivedMessage.event != null -> {
                            EventType.deserialize(receivedMessage.event, text)?.let { event ->
                                when (event) {
                                    is SwitchScenes -> listeners.onSwitchScene(event)
                                    is SourceFilterVisibilityChanged -> listeners.onSourceFilterVisibilityChanged(event)
                                }.exhaustive()
                            }
                            LOGGER.info { "Event received : ${receivedMessage.event}" }
                        }
                        else -> {
                            LOGGER.warn { "Unknown message received" }
                        }
                    }
                } catch (e: Exception) {
                    LOGGER.error(e) { "Error while parsing frame : $text" }
                }
            }
            else -> {
                LOGGER.info { "Unmanaged frame type ${frame.frameType.name}" }
            }
        }
        return Reconnect.CONTINUE
    }

    private inline fun <reified T : Response> handleError(payload: T) {
        if (payload.status == ResponseStatus.error) {
            LOGGER.error { "Request error [${T::class.simpleName}] : ${payload.error}" }
        } else {
            LOGGER.info { "connected" }
        }
    }

    @OptIn(InternalSerializationApi::class)
    private fun parseResponse(text: String): Response {
        val messageId = Serializer.decodeFromString<ReceivedMessage>(text).messageId

        return Serializer.decodeFromString(typeForMessage[messageId]!!.serializer(), text) as Response
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }

    enum class Reconnect {
        RECONNECT, CONTINUE
    }
}
