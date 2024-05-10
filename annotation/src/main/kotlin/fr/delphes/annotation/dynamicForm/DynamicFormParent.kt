package fr.delphes.annotation.dynamicForm

@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class DynamicFormParent(
    val family: DynamicFormFamily,
)