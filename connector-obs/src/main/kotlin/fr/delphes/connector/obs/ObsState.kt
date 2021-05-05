package fr.delphes.connector.obs

import fr.delphes.obs.Configuration
import fr.delphes.obs.ObsClient
import fr.delphes.obs.ObsListener
import fr.delphes.obs.incomingEvent.SceneChanged
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
        suspend fun connect() {
            return coroutineScope {
                try {
                    val listeners = ObsListener(
                        onSwitchScene = {
                            connector.bot.handleIncomingEvent(SceneChanged(it.sceneName))
                        },
                        onError = {
                            LOGGER.error { "Obs client error" }
                            connector.state = Error(configuration, connector)
                        }
                    )
                    val client = ObsClient(configuration.toObsConfiguration(), listeners)
                    connector.state = Connected
                    launch {
                        client.listen()
                    }
                } catch (e: Exception) {
                    LOGGER.error(e) { "Connection failed" }
                    connector.state = Error(configuration, connector)
                }
            }
        }

        private fun ObsConfiguration.toObsConfiguration() = Configuration(url, password)
    }

    class Error(
        private val configuration: ObsConfiguration,
        private val connector: ObsConnector,
    ) : ObsState() {
        suspend fun connect() {
            connector.state = Configured(configuration, connector)
            connector.connect()
        }
    }

    object Connected : ObsState()

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}