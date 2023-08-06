package fr.delphes.annotation.serialization

import kotlin.reflect.KClass

class RegisterPolymorphic(
    val annotationClass: KClass<out Any>,
    val parentClassName: KClass<out Any>,
)