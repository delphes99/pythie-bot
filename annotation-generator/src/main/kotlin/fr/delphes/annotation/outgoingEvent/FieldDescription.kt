package fr.delphes.annotation.outgoingEvent

@Target(AnnotationTarget.FIELD)
annotation class FieldDescription(
    val description: String,
)
