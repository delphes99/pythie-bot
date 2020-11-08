package fr.delphes.feature

import fr.delphes.bot.util.FileRepository

open class FileStateRepository<T : State>(
    filePath: String,
    serializer: (T) -> String,
    deserializer: (String) -> T,
    initializer: () -> T
) : FileRepository<T>(
    filePath,
    serializer,
    deserializer,
    initializer
), StateRepository<T>