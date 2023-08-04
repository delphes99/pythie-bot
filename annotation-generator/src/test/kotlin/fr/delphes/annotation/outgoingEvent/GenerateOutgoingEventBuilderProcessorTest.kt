package fr.delphes.annotation.outgoingEvent

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspArgs
import com.tschuchort.compiletesting.kspWithCompilation
import com.tschuchort.compiletesting.symbolProcessorProviders
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.feature.descriptor.DurationFeatureDescriptor
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Duration

class GenerateOutgoingEventBuilderProcessorTest : ShouldSpec({
    should("outgoing event should have outgoing event interface") {
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
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
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
    should("outgoing event interface is know if inherited") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
            import fr.delphes.bot.event.outgoing.OutgoingEvent

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
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
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
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("description")
                val myField: String,
                @FieldDescription("second description")
                val myField2: String,
            ) : OutgoingEvent
        """.shouldCompileWith {
            classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.MyEventBuilder")
                .annotations.should { annotations ->
                    withClue("should have serializable annotation") {
                        annotations.firstOrNull { it is Serializable }.shouldNotBeNull()
                    }
                    withClue("should have serial name annotation") {
                        annotations.firstOrNull { it is SerialName }
                            ?.let { it as SerialName }?.value shouldBe "serializeName"
                    }
                }
        }
    }
    should("generate builder with description method") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
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
                StringFeatureDescriptor("stringField", "string description", "value"),
                DurationFeatureDescriptor("durationField", "duration description", Duration.ofSeconds(42)),
            )
        }
    }
    should("custom mapper should be provided for custom fields") {
        """
            import fr.delphes.annotation.outgoingEvent.CustomFieldType
            import fr.delphes.annotation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
            import fr.delphes.bot.event.outgoing.OutgoingEvent

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("custom description")
                val customField: CustomFieldType,
            ) : OutgoingEvent
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "customField must have a mapper"
        }
    }
    should("custom fields should be string in builder") {
        """
            import fr.delphes.annotation.outgoingEvent.CustomFieldType
            import fr.delphes.annotation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription
            import fr.delphes.annotation.outgoingEvent.FieldMapper
            import fr.delphes.bot.event.outgoing.OutgoingEvent

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
    should("mapper on generic type should override generic type (field must be string)") {
        """
            import fr.delphes.annotation.outgoingEvent.CustomFieldType
            import fr.delphes.annotation.outgoingEvent.DurationFixedTypeMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription
            import fr.delphes.annotation.outgoingEvent.FieldMapper
            import fr.delphes.bot.event.outgoing.OutgoingEvent
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
    should("generate builder with build method which build event") {
        """
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription                
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

            builderClass.getMethod("build").invoke(newInstance).should { buildEvent ->
                buildEvent.getFieldValue("durationField") shouldBe Duration.ofSeconds(42)
                buildEvent.getFieldValue("stringField") shouldBe "value"
            }
        }
    }
    should("generate builder with build method which build event with custom type mapping") {
        """
            import fr.delphes.annotation.outgoingEvent.CustomFieldType
            import fr.delphes.annotation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.annotation.outgoingEvent.FieldDescription       
            import fr.delphes.annotation.outgoingEvent.FieldMapper         
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

            builderClass.getMethod("build").invoke(newInstance).should { buildEvent ->
                buildEvent.getFieldValue("customField") shouldBe CustomFieldType("custom value")
            }
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    val source = SourceFile.kotlin(
        "MyEvent.kt", this
    )
    KotlinCompilation()
        .apply {
            sources = listOf(source)
            symbolProcessorProviders = listOf(GenerateOutgoingEventBuilderModuleProcessorProvider())
            inheritClassPath = true
            kspArgs = mutableMapOf("module-name" to "test")
            kspWithCompilation = true
        }.compile().apply(assertion)
}