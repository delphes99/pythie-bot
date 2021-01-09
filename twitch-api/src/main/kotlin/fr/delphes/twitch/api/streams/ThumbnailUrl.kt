package fr.delphes.twitch.api.streams

data class ThumbnailUrl(private val twitchUrl: String) {
    fun withResolution(width: Int, height: Int): String {
        return twitchUrl
            .replace("{width}", width.toString(), false)
            .replace("{height}", height.toString(), false)
    }
}