package fr.delphes.generation.outgoingEvent.generateBuilderProcessor

import com.tschuchort.compiletesting.KotlinCompilation
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEventBuilder
import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.dynamicForm.descriptor.DurationFieldDescriptor
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.generation.getFieldValue
import fr.delphes.generation.outgoingEvent.CustomFieldType
import fr.delphes.generation.shouldCompileWithProvider
import fr.delphes.state.StateProvider
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.mockk
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Duration

class GenerateBuilderProcessorTest : ShouldSpec({
    should("outgoing event must have field named 'type'") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                val type: String,
            ) : OutgoingEvent
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "MyEvent must have a field named 'type'"
        }
    }
    //TODO verify all parents
    xshould("outgoing event should have outgoing event interface") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
        
            @RegisterOutgoingEvent("serializeName")
            class MyEvent
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "MyEvent must implement OutgoingEvent"
        }
    }
    should("outgoing event must have at least one field with description") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent : OutgoingEvent
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "MyEvent must have at least one field with description"
        }
    }
    should("outgoing event must have all fields with description") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: String,
                val myField2: String,
            ) : OutgoingEvent
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "MyEvent must have all fields with description"
        }
    }
    //TODO
    xshould("outgoing event interface is know if inherited") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.dynamicForm.FieldDescription

            interface MyOutgoingEventInterface : OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: String,
            ) : MyOutgoingEventInterface
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.OK
        }
    }
    should("generate builder with all fields") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("duration description")
                val durationField: Duration,
            ) : OutgoingEvent
        """.shouldCompileWith {
            classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
                .declaredFields
                .map { it.name to it.type } shouldBe listOf(
                "stringField" to String::class.java,
                "durationField" to Duration::class.java,
            )
        }
    }
    should("generate builder with serialize infos") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: String,
                @FieldDescription("second description")
                val myField2: String,
            ) : OutgoingEvent
        """.shouldCompileWith {
            val loadedClass = classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
            loadedClass
                .annotations.should { annotations ->
                    withClue("should have serializable annotation") {
                        annotations.firstOrNull { it is Serializable }.shouldNotBeNull()
                    }
                    withClue("should have serial name annotation") {
                        annotations.firstOrNull { it is SerialName }
                            ?.let { it as SerialName }?.value shouldBe "serializeName"
                    }
                    withClue("should have register polymorphic annotation") {
                        annotations.firstOrNull { it is RegisterOutgoingEventBuilder }
                            ?.let { it as RegisterOutgoingEventBuilder }.shouldNotBeNull()
                    }
                }
            withClue("should have OutgoingEventBuilder interface") {
                loadedClass.interfaces.firstOrNull { it == OutgoingEventBuilder::class.java }
                    .shouldNotBeNull()
            }
        }
    }
    //TODO : call suspend function from java classloader
    xshould("generate builder with description method") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("duration description")
                val durationField: Duration,
            ) : OutgoingEvent
        """.shouldCompileWith {
            val loadClass = classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
            val newInstance = loadClass
                .getConstructor(String::class.java, Duration::class.java)
                .newInstance("value", Duration.ofSeconds(42))
            loadClass.getMethod("description").invoke(newInstance) shouldBe OutgoingEventBuilderDescription(
                OutgoingEventType("serializeName"),
                StringFieldDescriptor("stringField", "string description", "value"),
                DurationFieldDescriptor("durationField", "duration description", Duration.ofSeconds(42)),
            )
        }
    }
    should("custom mapper should be provided for custom fields") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.generation.outgoingEvent.CustomFieldType
            import fr.delphes.generation.outgoingEvent.CustomFieldTypeMapper

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("custom description")
                val customField: CustomFieldType,
            ) : OutgoingEvent
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "Field [customField] : Unknown type and no mapper"
        }
    }
    should("custom fields should be string in builder") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.FieldMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.generation.outgoingEvent.CustomFieldType
            import fr.delphes.generation.outgoingEvent.CustomFieldTypeMapper

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("custom description")
                @FieldMapper(CustomFieldTypeMapper::class)
                val customField: CustomFieldType,
            ) : OutgoingEvent
        """.shouldCompileWith {
            classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
                .declaredFields
                .firstOrNull { it.name == "customField" }
                ?.type shouldBe String::class.java
        }
    }
    should("should ignore fields not in primary constructor") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.generation.outgoingEvent.CustomFieldType
            import fr.delphes.generation.outgoingEvent.CustomFieldTypeMapper

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
            ) : OutgoingEvent {
                val fieldToIgnore: String = ""
            }
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.OK
        }
    }
    should("mapper on generic type should override generic type (field must be string)") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.FieldMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import fr.delphes.generation.outgoingEvent.CustomFieldType
            import fr.delphes.generation.outgoingEvent.DurationFixedTypeMapper
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldMapper(DurationFixedTypeMapper::class)
                @FieldDescription("duration description")
                val durationField: Duration,
            ) : OutgoingEvent
        """.shouldCompileWith {
            classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
                .declaredFields
                .firstOrNull { it.name == "durationField" }
                ?.type shouldBe String::class.java
        }
    }
    //TODO : call suspend function from java classloader
    xshould("generate builder with build method which build event") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("duration description")
                val durationField: Duration,
            ) : OutgoingEvent
        """.shouldCompileWith {
            val builderClass = classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
            val newInstance = builderClass
                .getConstructor(String::class.java, Duration::class.java)
                .newInstance("value", Duration.ofSeconds(42))

            builderClass.getMethod("build", StateProvider::class.java).invoke(newInstance, mockk<StateProvider>())
                .should { buildEvent ->
                    buildEvent.getFieldValue("durationField") shouldBe Duration.ofSeconds(42)
                    buildEvent.getFieldValue("stringField") shouldBe "value"
                }
        }
    }
    //TODO : call suspend function from java classloader
    xshould("generate builder with build method which build event with custom type mapping") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.CustomFieldType
            import fr.delphes.annotation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.annotation.outgoingEvent.createBuilder.FieldMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("custom description")
                @FieldMapper(CustomFieldTypeMapper::class)
                val customField: CustomFieldType,
            ) : OutgoingEvent
        """.shouldCompileWith {
            val builderClass = classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
            val newInstance = builderClass
                .getConstructor(String::class.java)
                .newInstance("custom value")

            builderClass.getMethod("build", StateProvider::class.java).invoke(newInstance, mockk<StateProvider>())
                .should { buildEvent ->
                    buildEvent.getFieldValue("customField") shouldBe CustomFieldType("custom value")
                }
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    shouldCompileWithProvider("MyEvent.kt", GenerateOutgoingEventBuilderModuleProcessorProvider(), assertion)
}