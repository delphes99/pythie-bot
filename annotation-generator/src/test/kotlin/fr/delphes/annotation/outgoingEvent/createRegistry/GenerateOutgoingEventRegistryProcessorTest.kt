package fr.delphes.annotation.outgoingEvent.createRegistry

import com.tschuchort.compiletesting.KotlinCompilation
import fr.delphes.annotation.getFieldValue
import fr.delphes.annotation.shouldCompileWithProvider
import fr.delphes.feature.OutgoingEventType
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class GenerateOutgoingEventRegistryProcessorTest : ShouldSpec({
    xshould("generate outgoing event new instance registration") {
        """
            import fr.delphes.annotation.outgoingEvent.CustomFieldType
            import fr.delphes.annotation.outgoingEvent.CustomFieldTypeMapper
            import fr.delphes.dynamicForm.FieldDescription
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
            this.generatedFiles.first { it.name == "TestOutgoingEventRegistry.kt" }
            val builderClass =
                classLoader.loadClass("fr.delphes.test.generated.outgoingEvent.TestOutgoingEventRegistry")
            val newInstance = builderClass
                .getConstructor()
                .newInstance()

            newInstance.getFieldValue("type") shouldBe OutgoingEventType("serializeName")
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    shouldCompileWithProvider(GenerateOutgoingEventRegistryProcessorProvider(), assertion)
}