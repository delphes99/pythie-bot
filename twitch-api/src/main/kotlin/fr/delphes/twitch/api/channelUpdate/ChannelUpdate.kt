package fr.delphes.twitch.api.channelUpdate

data class ChannelUpdate(
    val title: String,
    val language: String,
    val categoryId: String,
    val categoryName: String
)