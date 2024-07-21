package fr.delphes.generation.dynamicForm.metada.type.duration

import com.squareup.kotlinpoet.asTypeName
import com.tschuchort.compiletesting.SourceFile
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.generation.assertCompileResolver
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import fr.delphes.utils.serialization.DurationSerializer
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.File
import java.time.Duration

class DurationMetadaExtractorTest : ShouldSpec({
    should("duration metadata") {
        SourceFile.fromPath(File("./src/test/kotlin/fr/delphes/generation/dynamicForm/metada/type/duration/DurationDynamicFormSource.kt"))
            .assertCompileResolver { resolver ->
                val metadataExtractor = MetadataExtractor()
                "fr.delphes.generation.dynamicForm.metada.type.duration.DurationDynamicFormSource.myField"
                    .let(resolver::getKSNameFromString)
                    .let(resolver::getPropertyDeclarationByName)
                    .shouldNotBeNull()
                    .let(metadataExtractor::getFieldMetaOf)
                    .shouldBeInstanceOf<FieldWithType>()
                    .apply {
                        description shouldBe "duration description"
                        defaultValue shouldBe "Duration.ZERO"
                        serializer shouldBe DurationSerializer::class
                        descriptionClass shouldBe DurationFieldDescriptor::class
                        fieldType shouldBe Duration::class.asTypeName()
                    }
            }
    }
})
