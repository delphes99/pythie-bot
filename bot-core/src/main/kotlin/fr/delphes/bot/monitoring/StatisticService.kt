package fr.delphes.bot.monitoring

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import kotlinx.serialization.json.Json
import mu.KotlinLogging

class StatisticService(
    configuration: BotConfiguration,
    serializer: Json,
    private val clock: Clock = SystemClock,
) {
    private val repository = FileStatisticEventRepository(
        configuration.pathOf("statistics", "statistics.json"),
        serializer
    )

    suspend fun handle(event: IncomingEvent) {
        try {
            val eventData = StatisticEvent(clock.now(), StatisticIncomingEventData(event))
            repository.save(eventData)
        } catch (e: Exception) {
            LOGGER.error(e) { "Error while saving incoming event for statistics" }
        }
    }

    suspend fun getEvents(): List<StatisticEvent<out StatisticData>> {
        return repository.getEvents()
    }

    companion object {
        private val LOGGER = KotlinLogging.logger {}
    }
}