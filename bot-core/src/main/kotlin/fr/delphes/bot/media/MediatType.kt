package fr.delphes.bot.media

enum class MediatType(private val typeCheck: (Media) -> Boolean) {
    SOUND({ media -> media.filename.endsWith(".mp3") });

    fun hasType(media: Media): Boolean {
        return this.typeCheck(media)
    }
}