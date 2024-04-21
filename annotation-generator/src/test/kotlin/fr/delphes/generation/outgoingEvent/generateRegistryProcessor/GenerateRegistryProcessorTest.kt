package fr.delphes.generation.outgoingEvent.generateRegistryProcessor

import com.tschuchort.compiletesting.KotlinCompilation
import fr.delphes.bot.event.outgoing.OutgoingEventRegistry
import fr.delphes.feature.OutgoingEventType
import fr.delphes.generation.loadGlobalVariable
import fr.delphes.generation.outgoingEvent.generateBuilderProcessor.GenerateOutgoingEventBuilderModuleProcessorProvider
import fr.delphes.generation.shouldCompileWithProvider
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.types.shouldBeInstanceOf

class GenerateRegistryProcessorTest : ShouldSpec({
    should("generate outgoing event new instance registration") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
            import fr.delphes.bot.event.outgoing.OutgoingEvent
            import java.time.Duration

            @RegisterOutgoingEvent("serializeName")
            class MyEvent(
                @FieldDescription("custom description")
                val customField: String,
            ) : OutgoingEvent
        """.shouldCompileWith {
            val registry = classLoader.loadGlobalVariable(
                "fr.delphes.test.generated.outgoingEvent",
                "testOutgoingEventRegistry",
            )

            registry
                .shouldBeInstanceOf<OutgoingEventRegistry>()
                .should {
                    it.types()
                        .shouldContainExactlyInAnyOrder(OutgoingEventType("serializeName"))
                }
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    shouldCompileWithProvider(
        "MyEvent.kt",
        listOf(
            GenerateOutgoingEventBuilderModuleProcessorProvider(),
            GenerateOutgoingEventRegistryProcessorProvider(),
        ),
        assertion
    )
}