package fr.delphes.feature

import fr.delphes.utils.Repository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class FeatureConfigurationRepository(
    private val path: Path,
    private val serializer: Json,
) : Repository<List<FeatureConfiguration>> {
    constructor(
        path: String,
        serializer: Json,
    ) : this(Path.of(path), serializer)

    init {
        path.toFile().mkdirs()
    }

    override suspend fun save(item: List<FeatureConfiguration>) {
        item.forEach { configuration ->
            Files.writeString(
                path.resolve("$FILENAME_PREFIX${configuration.id.value}.json"),
                serializer.encodeToString(configuration),
            )
        }
    }

    suspend fun upsert(configuration: FeatureConfiguration) {
        val newFeatures = load()
            .filter { it.id != configuration.id }
            .plus(configuration)

        save(newFeatures)
    }

    override suspend fun load(): List<FeatureConfiguration> {
        return Files.list(path)
            .filter(::isFeatureSaveFile)
            .map(Files::readString)
            .map<FeatureConfiguration>(serializer::decodeFromString)
            .toList()
    }

    companion object {
        private const val FILENAME_PREFIX = "feature-"

        private fun isFeatureSaveFile(path: Path) = path.fileName.toString().startsWith(FILENAME_PREFIX)
    }
}
