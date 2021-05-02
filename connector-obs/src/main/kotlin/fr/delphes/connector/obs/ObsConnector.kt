package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.endpoints.ObsModule
import io.ktor.application.Application
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
class ObsConnector(
    val bot: Bot,
    override val configFilepath: String,
) : Connector {
    private val repository = ObsConfigurationRepository("${configFilepath}\\obs\\configuration.json")
    var state: ObsState = ObsState.Unconfigured

    init {
        runBlocking {
            configure(repository.load())
        }
    }

    override fun internalEndpoints(application: Application) {
        application.ObsModule(this)
    }

    override fun publicEndpoints(application: Application) {}

    override suspend fun connect() {
        val newState = when(val oldState = state) {
            ObsState.Unconfigured -> oldState
            is ObsState.Configured -> oldState.connect()
            is ObsState.Error -> oldState
            is ObsState.Connected -> oldState
        }

        state = newState
    }

    override suspend fun execute(event: OutgoingEvent) {}

    suspend fun configure(configuration: ObsConfiguration) {
        repository.save(configuration)

        state = ObsState.Unconfigured.configure(configuration, this)
    }
}