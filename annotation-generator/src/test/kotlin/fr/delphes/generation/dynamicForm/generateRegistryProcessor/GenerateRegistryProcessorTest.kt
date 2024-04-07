package fr.delphes.generation.dynamicForm.generateRegistryProcessor

import com.tschuchort.compiletesting.KotlinCompilation
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.generation.loadGlobalVariable
import fr.delphes.generation.shouldCompileWithProvider
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class GenerateRegistryProcessorTest : ShouldSpec({
    should("generate dynamic form registry") {
        """
            import fr.delphes.annotation.dynamicForm.DynamicForm
            import fr.delphes.annotation.dynamicForm.FieldDescription
            
            @DynamicForm("my-form", ["tag1", "tag2"])
            class MyForm(
                @FieldDescription("description")
                val myField: String,
            )
        """.shouldCompileWith {
            val registry = classLoader.loadGlobalVariable(
                "fr.delphes.test.generated.dynamicForm",
                "testDynamicFormRegistry",
            )

            registry.shouldBeInstanceOf<DynamicFormRegistry>().should {
                it.entries.size shouldBe 1
                it.entries[0].name shouldBe "my-form"
                it.entries[0].tags.shouldContainExactlyInAnyOrder("tag1", "tag2")
                it.entries[0].clazz.simpleName shouldBe "MyForm"
            }
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    shouldCompileWithProvider(GenerateRegistryProcessorProvider(), assertion)
}