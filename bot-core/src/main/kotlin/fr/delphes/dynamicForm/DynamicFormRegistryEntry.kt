package fr.delphes.dynamicForm

import kotlin.reflect.KClass

data class DynamicFormRegistryEntry(
    val name: String,
    val clazz: KClass<out Any>,
    val tags: List<String>,
)