package fr.delphes.generation.dynamicForm.metada.type.map

import com.tschuchort.compiletesting.SourceFile
import fr.delphes.dynamicForm.descriptor.MapFieldDescriptor
import fr.delphes.generation.assertCompileResolver
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.File

class MapMetadataExtractorTest : ShouldSpec({
    should("map metadata") {
        SourceFile.fromPath(File("src/test/kotlin/fr/delphes/generation/dynamicForm/metada/type/map/MapDynamicFormSource.kt"))
            .assertCompileResolver { resolver ->
                val metadataExtractor = MetadataExtractor()
                "fr.delphes.generation.dynamicForm.metada.type.map.MapDynamicFormSource.myField"
                    .let(resolver::getKSNameFromString)
                    .let(resolver::getPropertyDeclarationByName)
                    .shouldNotBeNull()
                    .let(metadataExtractor::getFieldMetaOf)
                    .shouldBeInstanceOf<FieldWithType>()
                    .apply {
                        description shouldBe "description"
                        defaultValue shouldBe "emptyMap()"
                        serializer.shouldBeNull()
                        descriptionClass shouldBe MapFieldDescriptor::class
                        fieldType.toString() shouldBe "kotlin.collections.Map<kotlin.String, kotlin.String>"
                    }
            }
    }
})
