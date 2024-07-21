package fr.delphes.generation.dynamicForm.metada.type.string

import com.squareup.kotlinpoet.asTypeName
import com.tschuchort.compiletesting.SourceFile
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.generation.assertCompileResolver
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.File

class StringMetadaExtractorTest : ShouldSpec({
    should("string metadata") {
        SourceFile.fromPath(File("./src/test/kotlin/fr/delphes/generation/dynamicForm/metada/type/string/StringDynamicFormSource.kt"))
            .assertCompileResolver { resolver ->
                val metadataExtractor = MetadataExtractor()
                "fr.delphes.generation.dynamicForm.metada.type.string.StringDynamicFormSource.myField"
                    .let(resolver::getKSNameFromString)
                    .let(resolver::getPropertyDeclarationByName)
                    .shouldNotBeNull()
                    .let(metadataExtractor::getFieldMetaOf)
                    .shouldBeInstanceOf<FieldWithType>()
                    .apply {
                        description shouldBe "description"
                        defaultValue shouldBe "\"\""
                        serializer.shouldBeNull()
                        descriptionClass shouldBe StringFieldDescriptor::class
                        fieldType shouldBe String::class.asTypeName()
                    }
            }
    }
})