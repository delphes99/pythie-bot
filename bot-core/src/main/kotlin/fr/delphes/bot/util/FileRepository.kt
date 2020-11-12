package fr.delphes.bot.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

open class FileRepository<T>(
    filePath: String,
    val serializer: (T) -> String,
    val deserializer: (String) -> T,
    val initializer: () -> T
) {
    private val file: File = File(filePath)

    fun save(state: T) {
        if (!file.isFile) {
            Files.createDirectories(Path.of(file.parent))
            file.createNewFile()
        }
        file.writeText(serializer(state))
    }

    fun load(): T {
        if (!file.isFile) {
            return initializer()
        }
        return deserializer(file.readText())
    }
}