package fr.delphes.twitch.api.video

import java.time.LocalDateTime

data class ChannelVideo(
    val title: String,
    val url: String,
    val createdAt: LocalDateTime,
    val description: String? = null,
)
