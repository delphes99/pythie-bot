package fr.delphes.feature

import fr.delphes.utils.FileRepository

@Deprecated("migrate to fr.delphes.state.State")
open class FileStateRepository<T : State>(
    filePath: String,
    serializer: (T) -> String,
    deserializer: (String) -> T,
    initializer: suspend () -> T,
) : FileRepository<T>(
    filePath,
    serializer,
    deserializer,
    initializer
), StateRepository<T>