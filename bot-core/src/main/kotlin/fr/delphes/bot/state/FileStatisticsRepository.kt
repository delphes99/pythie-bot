package fr.delphes.bot.state

import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FileStatisticsRepository : StatisticsRepository {
    //TODO global save file folder
    private val globalRepository = FileRepository(
        "A:\\pythiebot\\stats\\globalStats.json",
        initializer =  { Statistics() },
        serializer = { Json.encodeToString(it) },
        deserializer = { Json.decodeFromString(it) }
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
        "A:\\pythiebot\\stats\\stream_stats_$streamId.json",
        initializer = { StreamStatistics(streamId) },
        serializer = { Json.encodeToString(it) },
        deserializer = { Json.decodeFromString(it) }
    )
}