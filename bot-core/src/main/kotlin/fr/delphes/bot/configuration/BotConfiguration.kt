package fr.delphes.bot.configuration

import java.io.File

class BotConfiguration(
    val configFilepath: String,
    val publicUrl: String,
) {
    fun pathOf(vararg folders: String): String {
        return listOf(
            configFilepath,
            *folders
        ).joinToString(separator = File.separator)
    }
}