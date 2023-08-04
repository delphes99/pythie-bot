package fr.delphes.annotation.outgoingEvent.builder

@Target(AnnotationTarget.FIELD)
annotation class FieldDescription(
    val description: String,
)
