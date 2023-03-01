package fr.delphes.utils

fun <K, V> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    return this.filterValues { value -> value != null } as Map<K, V>
}

fun <K, V> MutableMap<K, V>.removeAll(predicate: (K) -> Boolean) {
    val keysToRemove = this.keys.filter(predicate)

    keysToRemove.forEach { key ->
        this.remove(key)
    }
}

fun <K : Any, V : Any> MutableMap<K, V>.addNonPresents(keysToAdd: Collection<K>, addNonPresent: (K) -> V) {
    keysToAdd
        .filterNot { key -> this.keys.contains(key) }
        .map { key -> key to addNonPresent(key) }
        .forEach { (key, value) -> this[key] = value }
}