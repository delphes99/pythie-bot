package fr.delphes.annotation.serialization

import kotlin.reflect.KClass

class SerializerModule(
    val annotationClass: KClass<out Any>,
    val parentClassName: KClass<out Any>,
)