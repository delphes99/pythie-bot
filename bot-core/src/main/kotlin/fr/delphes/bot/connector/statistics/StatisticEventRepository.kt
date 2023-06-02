package fr.delphes.bot.connector.statistics

interface StatisticEventRepository {
    suspend fun save(event: StatEvent<StatisticEventData>)
}