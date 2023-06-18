package fr.delphes.annotation.outgoingEvent

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class GenerateOutgoingEventBuilderModuleProcessorTest : ShouldSpec({
    should("outgoing event should have outgoing event interface") {
        val source = SourceFile.kotlin(
            "MyEvent.kt", """import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent

                @RegisterOutgoingEvent
                class MyEvent
            """
        )
        KotlinCompilation()
            .apply {
                sources = listOf(source)
                symbolProcessorProviders = listOf(GenerateOutgoingEventBuilderModuleProcessorProvider())
                inheritClassPath = true
            }.compile().apply {
                exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
                messages shouldContain "MyEvent must implement OutgoingEvent"
            }
    }
    should("outgoing event must have at least one field with description") {
        val source = SourceFile.kotlin(
            "MyEvent.kt", """import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
                import fr.delphes.bot.event.outgoing.OutgoingEvent

                @RegisterOutgoingEvent
                class MyEvent : OutgoingEvent
            """
        )
        KotlinCompilation()
            .apply {
                sources = listOf(source)
                symbolProcessorProviders = listOf(GenerateOutgoingEventBuilderModuleProcessorProvider())
                inheritClassPath = true
            }.compile().apply {
                exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
                messages shouldContain "MyEvent must have at least on field with description"
            }
    }
})
