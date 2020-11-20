package fr.delphes.utils

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

open class FileRepository<T>(
    filePath: String,
    val serializer: (T) -> String,
    val deserializer: (String) -> T,
    val initializer: suspend () -> T
): Repository<T> {
    private val file: File = File(filePath)

    override suspend fun save(item: T) {
        if (!file.isFile) {
            Files.createDirectories(Path.of(file.parent))
            file.createNewFile()
        }
        file.writeText(serializer(item))
    }

    override suspend fun load(): T {
        if (!file.isFile) {
            return initializer()
        }
        return deserializer(file.readText())
    }
}