package fr.delphes.utils

inline fun <T, R> List<T>.flatMapNotNull(transform: (T) -> Iterable<R>?): List<R> {
    return this.flatMap { transform(it) ?: emptyList() }
}