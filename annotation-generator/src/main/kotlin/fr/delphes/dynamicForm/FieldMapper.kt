package fr.delphes.dynamicForm

import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class FieldMapper(
    val mapperClass: KClass<out FieldDescriptorMapper<*>>,
)