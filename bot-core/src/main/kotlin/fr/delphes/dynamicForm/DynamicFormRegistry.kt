package fr.delphes.dynamicForm

class DynamicFormRegistry private constructor(
    val entries: List<DynamicFormRegistryEntry<*>>,
) {
    fun find(name: DynamicFormType): DynamicFormRegistryEntry<*>? {
        return entries.firstOrNull { it.type == name }
    }

    fun findByTag(tag: String): List<DynamicFormRegistryEntry<*>> {
        return entries.filter { it.tags.contains(tag) }
    }

    fun <T : Any> transform(item: T): DynamicFormDTO<T>? {
        return findByClass(item)
            ?.map(item)
    }

    fun <T : Any> findByClass(item: T) = entries
        .filter { entry -> entry.clazz.isInstance(item) }
        .map { entry -> entry as DynamicFormRegistryEntry<T> }
        .firstOrNull()

    fun newInstanceOf(type: DynamicFormType): Any? {
        return find(type)?.emptyForm()?.build()
    }

    companion object {
        fun empty() = DynamicFormRegistry(emptyList())

        fun of(entries: List<DynamicFormRegistryEntry<*>>): DynamicFormRegistry {
            val duplicateNames = entries.groupBy { it.type }.filter { it.value.size > 1 }.keys
            if (duplicateNames.isNotEmpty()) {
                throw IllegalArgumentException("Duplicate form name: ${duplicateNames.joinToString { it.id }}")
            }

            return DynamicFormRegistry(entries)
        }

        fun of(vararg entries: DynamicFormRegistryEntry<*>) = of(entries.toList())

        fun compose(registries: List<DynamicFormRegistry>) = of(registries.flatMap { it.entries })

        fun compose(vararg registries: DynamicFormRegistry) = compose(registries.toList())
    }
}