package fr.delphes.generation.dynamicForm.metadata

import fr.delphes.generation.assertCompileResolver
import fr.delphes.generation.dynamicForm.metada.FieldWithMapper
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import fr.delphes.generation.utils.CompilationCheckException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf

class FieldMetadataTest : ShouldSpec({
    should("retrieve mapper informations") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.FieldMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.generation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                @FieldMapper(CustomFieldTypeMapper::class)
                val myField: String,
            ) : OutgoingEvent
        """.assertCompileResolver {
            val metadataExtractor = MetadataExtractor()
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                .shouldNotBeNull()
                .let(metadataExtractor::getFieldMetaOf)
                .shouldBeInstanceOf<FieldWithMapper>()
                .apply {
                    this.mapperClass.toString() shouldBe "CustomFieldTypeMapper"
                }
        }
    }
    should("unable to extract informations from unknown type") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.test.UnknownType

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: UnknownType,
            ) : OutgoingEvent
        """.assertCompileResolver {
            val metadataExtractor = MetadataExtractor()
            shouldThrow<CompilationCheckException> {
                it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                    .shouldNotBeNull()
                    .let(metadataExtractor::getFieldMetaOf)
            }
                .shouldHaveMessage("Field [myField] : Unknown type and no mapper")
        }
    }
})

