package fr.delphes.connector.discord.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.DiscordConnector
import net.dv8tion.jda.api.EmbedBuilder

sealed class DiscordOutgoingEvent : OutgoingEvent {
    abstract suspend fun executeOnDiscord(client: DiscordConnector)
}

typealias ChannelId = Long

data class DiscordMessage(val text: String, val channel: ChannelId) : DiscordOutgoingEvent() {
    override suspend fun executeOnDiscord(discord: DiscordConnector) {
        discord.connected {
            val channel = this@connected.client.getTextChannelById(channel)!!
            channel.sendMessage(text).complete()
        }
    }
}

data class DiscordEmbeddedMessage(
    val title: String?,
    val url: String?,
    val imageUrl: String?,
    val channel: ChannelId,
    val authorName: String?,
    val authorUrl: String?,
    val authorIconUrl: String?,
    val fields: List<Pair<String, String>>
) : DiscordOutgoingEvent() {
    constructor(
        title: String? = null,
        url: String? = null,
        imageUrl: String? = null,
        channel: ChannelId,
        authorName: String? = null,
        authorUrl: String? = null,
        authorIconUrl: String? = null,
        vararg fields: Pair<String, String>
    ) : this(
        title,
        url,
        imageUrl,
        channel,
        authorName,
        authorUrl,
        authorIconUrl,
        listOf(*fields)
    )

    override suspend fun executeOnDiscord(discord: DiscordConnector) {
        discord.connected {
            val channel = this@connected.client.getTextChannelById(channel)!!

            val builder = EmbedBuilder()
            builder.setTitle(title)
            builder.setImage(imageUrl)
            builder.setDescription(authorUrl)
            builder.setAuthor(
                authorName,
                authorUrl,
                authorIconUrl
            )
            fields.forEach { field ->
                builder.addField(field.first, field.second, true)
            }

            channel.sendMessage(builder.build()).complete()
        }
    }
}