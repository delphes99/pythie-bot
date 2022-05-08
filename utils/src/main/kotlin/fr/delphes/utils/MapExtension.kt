package fr.delphes.utils

fun <K, V> Map<K, V?>.filterValuesNotNull(): Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    return this.filterValues { value -> value != null } as Map<K, V>
}