package fr.delphes.annotation.outgoingEvent

@Target(AnnotationTarget.CLASS)
annotation class RegisterOutgoingEvent(
    val serializeName: String,
)
