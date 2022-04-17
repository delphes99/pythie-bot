package fr.delphes.bot.media

import fr.delphes.bot.Bot
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

class MediasService(
    private val bot: Bot
) {
    fun path(): String {
        return "${bot.configFilepath}${File.separator}medias"
    }

    fun list(): List<Media> {
        val path = Path(path())

        path.createDirectories()

        return path
            .listDirectoryEntries()
            .map { entry -> entry.fileName.name }
            .map(::Media)
    }
}