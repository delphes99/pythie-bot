package fr.delphes.annotation.dynamicForm

@Target(AnnotationTarget.CLASS)
annotation class DynamicForm(
    val name: String,
)