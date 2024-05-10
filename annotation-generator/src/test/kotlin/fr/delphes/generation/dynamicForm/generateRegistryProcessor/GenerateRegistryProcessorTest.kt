package fr.delphes.generation.dynamicForm.generateRegistryProcessor

import com.tschuchort.compiletesting.KotlinCompilation
import fr.delphes.dynamicForm.DynamicFormRegistry
import fr.delphes.dynamicForm.DynamicFormType
import fr.delphes.generation.dynamicForm.generateFormProcessor.GenerateDynamicFormProcessorProvider
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
            import fr.delphes.annotation.dynamicForm.DynamicFormTag
            import fr.delphes.annotation.dynamicForm.FieldDescription
            
            @DynamicForm("my-form")
            @DynamicFormTag("tag1")
            @DynamicFormTag("tag2")
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
                it.entries.first() should {
                    it.type shouldBe DynamicFormType("my-form")
                    it.tags.shouldContainExactlyInAnyOrder("tag1", "tag2")
                    it.clazz.simpleName shouldBe "MyForm"
                    it.emptyForm().javaClass.simpleName shouldBe "MyFormDynamicForm"
                }
            }
        }
    }
    should("register tag from interface") {
        """
            import fr.delphes.annotation.dynamicForm.DynamicForm
            import fr.delphes.annotation.dynamicForm.DynamicFormTag
            import fr.delphes.annotation.dynamicForm.FieldDescription
            
            @DynamicFormTag("tag1")
            @DynamicFormTag("tag2")
            interface FormInterface
            
            @DynamicForm("my-form")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
            ) : FormInterface
        """.shouldCompileWith {
            val registry = classLoader.loadGlobalVariable(
                "fr.delphes.test.generated.dynamicForm",
                "testDynamicFormRegistry",
            )

            registry.shouldBeInstanceOf<DynamicFormRegistry>().should {
                it.entries.size shouldBe 1
                it.entries.first() should {
                    it.type shouldBe DynamicFormType("my-form")
                    it.tags.shouldContainExactlyInAnyOrder("tag1", "tag2")
                    it.clazz.simpleName shouldBe "MyForm"
                    it.emptyForm().javaClass.simpleName shouldBe "MyFormDynamicForm"
                }
            }
        }
    }
    should("register tag from multiple interface layer") {
        """
            import fr.delphes.annotation.dynamicForm.DynamicForm
            import fr.delphes.annotation.dynamicForm.DynamicFormTag
            import fr.delphes.annotation.dynamicForm.FieldDescription
            
            @DynamicFormTag("tag1")
            @DynamicFormTag("tag2")
            interface FormInterface
            
            interface IntermediateFormInterface : FormInterface
            
            @DynamicForm("my-form")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
            ) : IntermediateFormInterface
        """.shouldCompileWith {
            val registry = classLoader.loadGlobalVariable(
                "fr.delphes.test.generated.dynamicForm",
                "testDynamicFormRegistry",
            )

            registry.shouldBeInstanceOf<DynamicFormRegistry>().should {
                it.entries.size shouldBe 1
                it.entries.first() should {
                    it.type shouldBe DynamicFormType("my-form")
                    it.tags.shouldContainExactlyInAnyOrder("tag1", "tag2")
                    it.clazz.simpleName shouldBe "MyForm"
                    it.emptyForm().javaClass.simpleName shouldBe "MyFormDynamicForm"
                }
            }
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    shouldCompileWithProvider(
        "MyForm.kt",
        listOf(
            GenerateDynamicFormProcessorProvider(),
            GenerateRegistryProcessorProvider()
        ),
        assertion
    )
}