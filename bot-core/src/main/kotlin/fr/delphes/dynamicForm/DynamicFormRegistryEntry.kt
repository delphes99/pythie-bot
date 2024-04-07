package fr.delphes.dynamicForm

import kotlin.reflect.KClass

data class DynamicFormRegistryEntry(
    val name: String,
    val clazz: KClass<out Any>,
    val tags: List<String>,
) {
    companion object {
        fun of(
            name: String,
            clazz: KClass<out Any>,
            tags: List<String>,
        ) = DynamicFormRegistryEntry(name, clazz, tags)

        fun of(
            name: String,
            clazz: KClass<out Any>,
            vararg tags: String,
        ) = of(name, clazz, tags.toList())
    }
}