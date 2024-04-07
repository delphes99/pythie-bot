package fr.delphes.annotation.dynamicForm

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
annotation class FieldMapper(
    val mapperClass: KClass<out FieldDescriptorMapper<*>>,
)