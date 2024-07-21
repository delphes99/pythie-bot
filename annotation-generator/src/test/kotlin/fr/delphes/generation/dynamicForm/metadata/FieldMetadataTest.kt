package fr.delphes.generation.dynamicForm.metadata

import com.squareup.kotlinpoet.asTypeName
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.dynamicForm.descriptor.MapFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.generation.assertCompileResolver
import fr.delphes.generation.dynamicForm.metada.FieldWithMapper
import fr.delphes.generation.dynamicForm.metada.FieldWithType
import fr.delphes.generation.dynamicForm.metada.MetadataExtractor
import fr.delphes.generation.utils.CompilationCheckException
import fr.delphes.utils.serialization.DurationSerializer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf
import java.time.Duration

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
    should("string metadata") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: String,
            ) : OutgoingEvent
        """.assertCompileResolver {
            val metadataExtractor = MetadataExtractor()
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
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
    should("duration metadata") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("duration description")
                val myField: Duration,
            ) : OutgoingEvent
        """.assertCompileResolver {
            val metadataExtractor = MetadataExtractor()
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
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
    should("map metadata") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: Map<String, String>,
            ) : OutgoingEvent
        """.assertCompileResolver {
            val metadataExtractor = MetadataExtractor()
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
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

