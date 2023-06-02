package fr.delphes.bot.connector.statistics

import fr.delphes.utils.FileRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class FileStatisticEventRepository(
    savePath: String,
    serializer: Json,
) : StatisticEventRepository {
    private val fileRepository = FileRepository<List<StatEvent<StatisticEventData>>>(
        savePath,
        serializer = { serializer.encodeToString(it) },
        deserializer = { serializer.decodeFromString(it) },
        initializer = { emptyList() }
    )

    override suspend fun save(event: StatEvent<StatisticEventData>) {
        val currentValue = fileRepository.load()
        fileRepository.save(currentValue.plus(event))
    }
}
