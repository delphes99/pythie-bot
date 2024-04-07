package fr.delphes.annotation.dynamicForm

@Target(AnnotationTarget.FIELD)
annotation class FieldDescription(
    val description: String,
)
