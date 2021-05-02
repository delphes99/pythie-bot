package fr.delphes.connector.obs

import fr.delphes.obs.Configuration
import fr.delphes.obs.ObsClient
import fr.delphes.obs.incomingEvent.SceneChanged
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import mu.KotlinLogging

@InternalSerializationApi
sealed class ObsState {
    object Unconfigured : ObsState() {
        fun configure(configuration: ObsConfiguration, connector: ObsConnector): Configured {
            return Configured(configuration, connector)
        }
    }

    class Configured(
        private val configuration: ObsConfiguration,
        private val connector: ObsConnector,
    ) : ObsState() {
        suspend fun connect(): ObsState {
            return coroutineScope {
                 try {
                    val client = ObsClient(configuration.toObsConfiguration()) {
                        //TODO non blocking
                        runBlocking {
                            connector.bot.handleIncomingEvent(SceneChanged(it.sceneName))
                        }
                    }
                    launch {
                        client.listen()
                    }
                    Connected()
                } catch (e: Exception) {
                    LOGGER.error(e) { "Connection failed" }
                    Error
                }
            }
        }

        private fun ObsConfiguration.toObsConfiguration() = Configuration(url, password)
    }

    object Error : ObsState()

    class Connected() : ObsState()

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}