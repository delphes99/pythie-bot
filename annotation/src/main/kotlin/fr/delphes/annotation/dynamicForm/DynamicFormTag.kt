package fr.delphes.annotation.dynamicForm

@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class DynamicFormTag(
    val tag: String,
)