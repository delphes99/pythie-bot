package fr.delphes.bot.monitoring

import fr.delphes.bot.configuration.BotConfiguration
import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json

class StatisticService(
    configuration: BotConfiguration,
    serializer: Json,
    private val clock: Clock = SystemClock,
) {
    private val repository = FileStatisticEventRepository(
        configuration.pathOf("statistics", "statistics.json"),
        serializer
    )

    suspend fun handle(event: IncomingEventWrapper<*>) {
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