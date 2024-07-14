package fr.delphes.feature

import fr.delphes.dynamicForm.DynamicFormDTO
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.rework.feature.Feature
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.rework.feature.FeatureId
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class FileFeatureConfigurationRepository(
    private val path: Path,
    private val serializer: Json,
    private val dynamicFormRegistry: DynamicFormRegistry,
) : FeatureConfigurationRepository {
    constructor(
        path: String,
        serializer: Json,
        dynamicFormRegistry: DynamicFormRegistry,
    ) : this(Path.of(path), serializer, dynamicFormRegistry)

    init {
        path.toFile().mkdirs()
    }

    override suspend fun save(item: List<Feature>) {
        item.forEach { configuration ->

            Files.writeString(
                path.resolve("$FILENAME_PREFIX${configuration.id.value}.json"),
                serializer.encodeToString(configuration.toSaveFile())
            )
        }
    }

    private fun Feature.toSaveFile(): FeatureSaveFile {
        val dto = dynamicFormRegistry.transform(this.definition) ?: error("Feature definition not found")
        return FeatureSaveFile(
            this.id,
            dto
        )
    }

    override suspend fun upsert(configuration: Feature) {
        val newFeatures = load()
            .filter { it.id != configuration.id }
            .plus(configuration)

        save(newFeatures)
    }

    override suspend fun load(): List<Feature> {
        return Files.list(path)
            .filter(::isFeatureSaveFile)
            .map(Files::readString)
            .map<FeatureSaveFile>(serializer::decodeFromString)
            .map(FeatureSaveFile::toFeature)
            .toList()
    }

    @Serializable
    private class FeatureSaveFile(
        val id: FeatureId,
        val configuration: DynamicFormDTO<FeatureDefinition>,
    ) {
        fun toFeature(): Feature {
            return Feature(
                id,
                configuration.build()
            )
        }
    }

    companion object {
        private const val FILENAME_PREFIX = "feature-"

        private fun isFeatureSaveFile(path: Path) = path.fileName.toString().startsWith(FILENAME_PREFIX)
    }
}

