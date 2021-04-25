package fr.delphes.connector.obs

import mu.KotlinLogging

sealed class ObsState {
    object Unconfigured : ObsState() {
        fun configure(configuration: ObsConfiguration, connector: ObsConnector): Configured {
            return Configured(configuration, connector)
        }
    }

    class Configured(
        private val configuration: ObsConfiguration,
        private val connector: ObsConnector
    ) : ObsState() {
        fun connect(): ObsState {
            return try {
                //TODO

                Connected()
            } catch (e: Exception) {
                LOGGER.error(e) { "Discord connection failed" }
                Error
            }
        }
    }

    object Error : ObsState()

    class Connected() : ObsState()

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}