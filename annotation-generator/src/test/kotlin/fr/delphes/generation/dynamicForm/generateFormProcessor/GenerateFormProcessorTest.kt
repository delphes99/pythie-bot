package fr.delphes.generation.dynamicForm.generateFormProcessor

import com.tschuchort.compiletesting.KotlinCompilation
import fr.delphes.annotation.dynamicForm.RegisterDynamicFormDto
import fr.delphes.dynamicForm.DynamicFormDTO
import fr.delphes.dynamicForm.DynamicFormDescription
import fr.delphes.dynamicForm.DynamicFormType
import fr.delphes.dynamicForm.descriptor.StringFieldDescriptor
import fr.delphes.generation.shouldCompileWithProvider
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class GenerateBuilderProcessorTest : ShouldSpec({
    should("form must have at least one field with description") {
        """
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("my-form")
            class MyForm
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain "MyForm must have at least one field with description"
        }
    }
    should("all fields of form must have a description") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("my-form")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
                val myField2: String,
                val myField3: String,
            )
        """.shouldCompileWith {
            exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
            messages shouldContain """MyForm : missing description on fields : ["myField2", "myField3"]"""
        }
    }
    should("generate form with all fields") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("my-form")
            class MyForm(
                @FieldDescription("string description")
                val stringField: String,
                @FieldDescription("another string description")
                val anotherStringField: String,
            )
        """.shouldCompileWith {
            classLoader.loadClass("fr.delphes.test.generated.dynamicForm.MyFormDynamicForm")
                .declaredFields
                .should { fields ->
                    fields.first { it.name == "stringField" }.should { field ->
                        field.type shouldBe String::class.java
                    }
                    fields.first { it.name == "anotherStringField" }.should { field ->
                        field.type shouldBe String::class.java
                    }
                }
        }
    }
    should("generate form with serialize infos") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("serializeName")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
                @FieldDescription("second description")
                val myField2: String,
            )
        """.shouldCompileWith {
            val loadedClass = classLoader.loadClass("fr.delphes.test.generated.dynamicForm.MyFormDynamicForm")
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
                        annotations.firstOrNull { it is RegisterDynamicFormDto }.shouldNotBeNull()
                    }
                }
        }
    }
    should("should have DynamicFormDTO interface") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("serializeName")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
                @FieldDescription("second description")
                val myField2: String,
            )
        """.shouldCompileWith {
            val loadedClass = classLoader.loadClass("fr.delphes.test.generated.dynamicForm.MyFormDynamicForm")
            loadedClass.interfaces.firstOrNull { it == DynamicFormDTO::class.java }
                .shouldNotBeNull()
        }
    }
    xshould("dynamic form have 'type' defined in annotation") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("my-form")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
            )
        """.shouldCompileWith {
            val loadedClass = classLoader.loadClass("fr.delphes.test.generated.dynamicForm.MyFormDynamicForm")
            loadedClass.getDeclaredConstructor().newInstance()

            //TODO access to type field value
        }
    }
    should("description method") {
        """
            import fr.delphes.annotation.dynamicForm.FieldDescription
            import fr.delphes.annotation.dynamicForm.DynamicForm

            @DynamicForm("serializeName")
            class MyForm(
                @FieldDescription("description")
                val myField: String,
                @FieldDescription("second description")
                val myField2: String,
            )
        """.shouldCompileWith {
            val loadClass = classLoader.loadClass("fr.delphes.test.generated.dynamicForm.MyFormDynamicForm")
            val newInstance = loadClass
                .getConstructor(String::class.java, String::class.java)
                .newInstance("value", "second value")
            loadClass.getMethod("description").invoke(newInstance) shouldBe DynamicFormDescription(
                DynamicFormType("serializeName"),
                StringFieldDescriptor("myField", "description", "value"),
                StringFieldDescriptor("myField2", "second description", "second value"),
            )
        }
    }
})

private fun String.shouldCompileWith(
    assertion: KotlinCompilation.Result.() -> Unit,
) {
    shouldCompileWithProvider("MyForm.kt", GenerateDynamicFormProcessorProvider(), assertion)
}