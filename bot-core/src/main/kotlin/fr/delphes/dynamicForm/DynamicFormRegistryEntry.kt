package fr.delphes.dynamicForm

import kotlin.reflect.KClass

class DynamicFormRegistryEntry<T : Any>(
    val type: DynamicFormType,
    val clazz: KClass<T>,
    val tags: List<String>,
    private val buildNewInstance: () -> DynamicFormDTO<T>,
    private val mapInstance: (T, DynamicFormRegistry) -> DynamicFormDTO<T>,
) {
    fun emptyForm(): DynamicFormDTO<T> = buildNewInstance()

    fun map(form: T, registry: DynamicFormRegistry): DynamicFormDTO<T> = mapInstance(form, registry)

    companion object {
        fun <T : Any> of(
            name: DynamicFormType,
            clazz: KClass<T>,
            tags: List<String>,
            buildNewInstance: () -> DynamicFormDTO<T>,
            mapInstance: (T, DynamicFormRegistry) -> DynamicFormDTO<T>,
        ) = DynamicFormRegistryEntry(name, clazz, tags, buildNewInstance, mapInstance)
    }
}