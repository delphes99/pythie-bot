package fr.delphes.connector.discord.outgoingEvent

import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.embed
import fr.delphes.connector.discord.DiscordConnector

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
            this.client.rest.channel.createMessage(Snowflake(channel)) {
                embed {
                    title = this@DiscordEmbeddedMessage.title
                    image = this@DiscordEmbeddedMessage.imageUrl
                    description = this@DiscordEmbeddedMessage.url
                    author {
                        name = this@DiscordEmbeddedMessage.authorName
                        url = this@DiscordEmbeddedMessage.authorUrl
                        icon = this@DiscordEmbeddedMessage.authorIconUrl
                    }
                    this@DiscordEmbeddedMessage.fields.forEach { field ->
                        field {
                            name = field.first
                            value = field.second
                        }
                    }
                }
            }
        }
    }
}