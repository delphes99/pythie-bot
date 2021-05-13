package fr.delphes.connector.obs

import fr.delphes.bot.Bot
import fr.delphes.bot.connector.Connector
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.endpoints.ObsModule
import fr.delphes.connector.obs.outgoingEvent.ObsOutgoingEvent
import fr.delphes.utils.exhaustive
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
        when (val oldState = state) {
            is ObsState.Configured -> oldState.connect()
            is ObsState.Error -> oldState.connect()
            ObsState.Unconfigured,
            is ObsState.Connected -> Unit //Nothing
        }.exhaustive()
    }

    override suspend fun execute(event: OutgoingEvent) {
        if(event is ObsOutgoingEvent) {
            event.executeOnObs(this)
        }
    }

    suspend fun connected(doStuff: suspend ObsState.Connected.() -> Unit) {
        val currentState = state
        if(currentState is ObsState.Connected) {
            currentState.doStuff()
        }
    }

    suspend fun configure(configuration: ObsConfiguration) {
        repository.save(configuration)

        state = ObsState.Unconfigured.configure(configuration, this)
    }
}