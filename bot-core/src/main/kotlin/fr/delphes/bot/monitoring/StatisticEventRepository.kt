package fr.delphes.bot.monitoring

interface StatisticEventRepository {
    suspend fun save(event: StatisticEvent<out StatisticData>)

    suspend fun getEvents(): List<StatisticEvent<out StatisticData>>
}