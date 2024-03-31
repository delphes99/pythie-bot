package fr.delphes.dynamicForm

@Target(AnnotationTarget.FIELD)
annotation class FieldDescription(
    val description: String,
)
