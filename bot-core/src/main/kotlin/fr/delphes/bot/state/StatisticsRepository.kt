package fr.delphes.bot.state

import fr.delphes.twitch.api.streams.Stream

interface StatisticsRepository {
    suspend fun globalStatistics(): Statistics

    suspend fun streamStatistics(currentStream: Stream): StreamStatistics

    suspend fun saveGlobal(statistics: Statistics)

    suspend fun saveStream(streamStatics: StreamStatistics)
}