package fr.delphes.annotation.outgoingEvent.createBuilder

@Target(AnnotationTarget.FIELD)
annotation class FieldDescription(
    val description: String,
)
