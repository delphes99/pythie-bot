package fr.delphes.bot.state

import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.FileRepository
import fr.delphes.utils.serialization.Serializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

@Deprecated("twitch specific statistics")
class FileStatisticsRepository(
    private val configFilepath: String,
) : StatisticsRepository {
    private val globalRepository = FileRepository(
        "$configFilepath\\stats\\globalStats.json",
        initializer = { Statistics() },
        serializer = { Serializer.encodeToString(it) },
        deserializer = { Serializer.decodeFromString(it) }
    )

    override suspend fun globalStatistics(): Statistics {
        return globalRepository.load()
    }

    override suspend fun saveGlobal(statistics: Statistics) {
        globalRepository.save(statistics)
    }

    override suspend fun streamStatistics(currentStream: Stream): StreamStatistics {
        return buildStreamStatisticsRepository(currentStream.id).load()
    }

    override suspend fun saveStream(streamStatics: StreamStatistics) {
        buildStreamStatisticsRepository(streamStatics.streamId).save(streamStatics)
    }

    private fun buildStreamStatisticsRepository(streamId: String) = FileRepository(
        "$configFilepath\\stats\\stream_stats_$streamId.json",
        initializer = { StreamStatistics(streamId) },
        serializer = { Serializer.encodeToString(it) },
        deserializer = { Serializer.decodeFromString(it) }
    )
}