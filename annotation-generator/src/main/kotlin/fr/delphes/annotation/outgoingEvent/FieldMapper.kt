package fr.delphes.annotation.outgoingEvent

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class FieldMapper(
    val mapperClass: KClass<out FieldDescriptorMapper<*>>,
)