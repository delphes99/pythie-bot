package fr.delphes.feature

import java.io.File

class FileStateManager<T : State>(
    filePath: String,
    val serializer: (T) -> String,
    val deserializer: (String) -> T,
    val initializer: () -> T
) : StateManager<T> {
    private val file: File = File(filePath)

    override fun save(state: T) {
        if (!file.isFile) {
            file.createNewFile()
        }
        file.writeText(serializer(state))
    }

    override fun load(): T {
        if (!file.isFile) {
            return initializer()
        }
        return deserializer(file.readText())
    }
}