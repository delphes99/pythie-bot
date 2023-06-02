package fr.delphes.bot.statistics

interface StatisticEventRepository {
    suspend fun save(event: StatisticEvent<out StatisticData>)
}