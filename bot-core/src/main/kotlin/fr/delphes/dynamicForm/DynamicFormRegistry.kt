package fr.delphes.dynamicForm

class DynamicFormRegistry private constructor(
    val entries: List<DynamicFormRegistryEntry>,
) {
    fun find(name: String): DynamicFormRegistryEntry? {
        return entries.firstOrNull { it.name == name }
    }

    fun findByTag(tag: String): List<DynamicFormRegistryEntry> {
        return entries.filter { it.tags.contains(tag) }
    }

    companion object {
        fun empty() = DynamicFormRegistry(emptyList())

        fun of(entries: List<DynamicFormRegistryEntry>): DynamicFormRegistry {
            val duplicateNames = entries.groupBy { it.name }.filter { it.value.size > 1 }.keys
            if (duplicateNames.isNotEmpty()) {
                throw IllegalArgumentException("Duplicate form name: ${duplicateNames.joinToString()}")
            }

            return DynamicFormRegistry(entries)
        }

        fun of(vararg entries: DynamicFormRegistryEntry) = of(entries.toList())

        fun compose(registries: List<DynamicFormRegistry>) = of(registries.flatMap { it.entries })

        fun compose(vararg registries: DynamicFormRegistry) = compose(registries.toList())
    }
}