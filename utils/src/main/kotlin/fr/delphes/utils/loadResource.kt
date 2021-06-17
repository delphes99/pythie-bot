package fr.delphes.utils

fun loadResourceAsText(path: String): String? {
    return LoadResource::class.java.getResource(path)?.readText()
}

private class LoadResource