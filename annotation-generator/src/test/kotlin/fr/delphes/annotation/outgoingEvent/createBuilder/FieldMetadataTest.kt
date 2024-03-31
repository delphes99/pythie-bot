package fr.delphes.annotation.outgoingEvent.createBuilder

import com.squareup.kotlinpoet.asTypeName
import fr.delphes.annotation.assertCompileResolver
import fr.delphes.dynamicForm.FieldWithMapper
import fr.delphes.dynamicForm.FieldWithType
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.dynamicForm.descriptor.MapFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.dynamicForm.getFieldMeta
import fr.delphes.generation.CompilationCheckException
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
            import fr.delphes.dynamicForm.FieldDescription
            import fr.delphes.dynamicForm.FieldMapper
            import fr.delphes.annotation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                @FieldMapper(CustomFieldTypeMapper::class)
                val myField: String,
            ) : OutgoingEvent
        """.assertCompileResolver {
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                .shouldNotBeNull()
                .getFieldMeta()
                .shouldBeInstanceOf<FieldWithMapper>()
                .apply {
                    this.mapperClass.toString() shouldBe "CustomFieldTypeMapper"
                }
        }
    }
    should("string metadata") {
        """
            import fr.delphes.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: String,
            ) : OutgoingEvent
        """.assertCompileResolver {
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                .shouldNotBeNull()
                .getFieldMeta()
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
            import fr.delphes.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("duration description")
                val myField: Duration,
            ) : OutgoingEvent
        """.assertCompileResolver {
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                .shouldNotBeNull()
                .getFieldMeta()
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
            import fr.delphes.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: Map<String, String>,
            ) : OutgoingEvent
        """.assertCompileResolver {
            it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                .shouldNotBeNull()
                .getFieldMeta()
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
            import fr.delphes.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.test.UnknownType

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: UnknownType,
            ) : OutgoingEvent
        """.assertCompileResolver {
            shouldThrow<CompilationCheckException> {
                it.getPropertyDeclarationByName(it.getKSNameFromString("MyEvent.myField"))
                    .shouldNotBeNull()
                    .getFieldMeta()
            }
                .shouldHaveMessage("Field [myField] : Unknown type and no mapper")
        }
    }
})

