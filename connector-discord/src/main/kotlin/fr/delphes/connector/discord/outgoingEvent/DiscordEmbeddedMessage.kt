package fr.delphes.connector.discord.outgoingEvent

import fr.delphes.connector.discord.DiscordConnector
import net.dv8tion.jda.api.EmbedBuilder

data class DiscordEmbeddedMessage(
    val title: String?,
    val url: String?,
    val imageUrl: String?,
    val channel: ChannelId,
    val authorName: String?,
    val authorUrl: String?,
    val authorIconUrl: String?,
    val fields: List<Pair<String, String>>
) : DiscordOutgoingEvent {
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

    override suspend fun executeOnDiscord(client: DiscordConnector) {
        client.connected {
            val channel = this@connected.client.getTextChannelById(channel)!!

            val builder = EmbedBuilder()
            builder.setTitle(title)
            builder.setImage(imageUrl)
            builder.setDescription(url)
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