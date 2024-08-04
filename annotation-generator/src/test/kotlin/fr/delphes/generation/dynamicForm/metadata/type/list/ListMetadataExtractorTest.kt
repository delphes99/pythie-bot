package fr.delphes.generation.dynamicForm.metadata.type.list

import com.tschuchort.compiletesting.SourceFile
import fr.delphes.dynamicForm.descriptor.FormListFieldDescriptor
import fr.delphes.generation.assertCompileResolver
import fr.delphes.generation.dynamicForm.metadata.FieldWithFormList
import fr.delphes.generation.dynamicForm.metadata.MetadataExtractor
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.io.File

class ListMetadataExtractorTest : ShouldSpec({
    should("map metadata") {
        SourceFile.fromPath(File("./src/test/kotlin/fr/delphes/generation/dynamicForm/metadata/type/list/ListDynamicFormSource.kt"))
            .assertCompileResolver { resolver ->
                val metadataExtractor = MetadataExtractor()
                resolver.getPropertyDeclarationByName(resolver.getKSNameFromString("fr.delphes.generation.dynamicForm.metadata.type.list.ListDynamicFormSource.myField"))
                    .shouldNotBeNull()
                    .let(metadataExtractor::getFieldMetaOf)
                    .shouldBeInstanceOf<FieldWithFormList>()
                    .apply {
                        description shouldBe "description"
                        defaultValue shouldBe "emptyList()"
                        serializer.shouldBeNull()
                        descriptionClass shouldBe FormListFieldDescriptor::class
                        fieldType.toString() shouldBe "kotlin.collections.List<fr.delphes.dynamicForm.DynamicFormDTO<@kotlinx.serialization.Contextual kotlin.Any>>"
                        dynamicFormFamily shouldBe "innerFormFamily"
                        formSuperClass.declaration.qualifiedName?.asString() shouldBe ListInnerFormSource::class.qualifiedName
                    }
            }
    }
})
