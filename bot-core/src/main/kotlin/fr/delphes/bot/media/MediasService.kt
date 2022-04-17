package fr.delphes.bot.media

import fr.delphes.bot.Bot
import java.io.File.separator
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.writeBytes

class MediasService(
    private val bot: Bot
) {
    fun path(): String {
        return "${bot.configFilepath}${separator}medias"
    }

    fun list(): List<Media> {
        val path = Path(path())

        path.createDirectories()

        return path
            .listDirectoryEntries()
            .map { entry -> entry.fileName.name }
            .map(::Media)
    }

    fun upload(fileName: String, file: ByteArray) {
        Path("${path()}$separator$fileName").writeBytes(file)
    }
}