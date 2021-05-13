package fr.delphes.connector.obs

import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.obs.Configuration
import fr.delphes.obs.ObsClient
import fr.delphes.obs.ObsListener
import fr.delphes.connector.obs.incomingEvent.SceneChanged
import fr.delphes.connector.obs.incomingEvent.SourceFilterActivated
import fr.delphes.connector.obs.incomingEvent.SourceFilterDeactivated
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
                    //TODO move listener build
                    val listeners = ObsListener(
                        onSwitchScene = {
                            connector.bot.handleIncomingEvent(SceneChanged(it.sceneName))
                        },
                        onSourceFilterVisibilityChanged = {
                            val filter = SourceFilter(it.sourceName, it.filterName)
                            val event = if(it.filterEnabled) {
                                SourceFilterActivated(filter)
                            } else {
                                SourceFilterDeactivated(filter)
                            }
                            connector.bot.handleIncomingEvent(event)
                        },
                        onError = {
                            LOGGER.error { "Obs client error" }
                            connector.state = Error(configuration, connector)
                        }
                    )
                    val client = ObsClient(configuration.toObsConfiguration(), listeners)
                    connector.state = Connected(client)
                    launch {
                        client.listen()
                    }
                } catch (e: Exception) {
                    LOGGER.error(e) { "Connection failed" }
                    connector.state = Error(configuration, connector)
                }
            }
        }

        private fun ObsConfiguration.toObsConfiguration() = Configuration(host, port, password)
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

    class Connected(
        val client: ObsClient
    ) : ObsState()

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}