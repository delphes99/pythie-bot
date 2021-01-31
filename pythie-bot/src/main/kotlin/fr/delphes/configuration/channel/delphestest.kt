package fr.delphes.configuration.channel

import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.discord.outgoingEvent.DiscordEmbeddedMessage
import fr.delphes.feature.statistics.Statistics
import fr.delphes.features.twitch.command.Command
import fr.delphes.features.twitch.commandList.CommandList
import fr.delphes.features.twitch.gameDescription.GameDescription
import fr.delphes.features.twitch.newFollow.NewFollow
import fr.delphes.features.twitch.streamOffline.StreamOffline
import fr.delphes.features.twitch.streamOnline.StreamOnline
import fr.delphes.features.twitch.streamUpdate.StreamUpdate
import java.time.Duration

/**
 * Example for delphestest channel : https://www.twitch.tv/delphestest
 */
val channel = "delphestest"
val delphestestChannel = ChannelConfiguration.build("configuration-delphestest.properties") { properties ->
    ChannelConfiguration(
        properties.getProperty("channel.name"),
        properties.getProperty("account.oAuth"),
        emptyList(),
        Command(
            channel,
            "!test",
            cooldown = Duration.ofSeconds(10),
            responses = listOf(
                SendMessage("Compte de test opérationnel !")
            )
        ),
        NewFollow(channel) { newFollow ->
            listOf(SendMessage("Merci du follow ${newFollow.follower.name}"))
        },
        StreamOffline(channel) { listOf(SendMessage("Le stream est fini, au revoir !")) },
        StreamOnline(channel) {
            listOf(
                SendMessage("Le stream démarre, ravi de vous revoir !"),
                DiscordEmbeddedMessage(
                    it.title,
                    "https://www.twitch.tv/delphestest",
                    it.thumbnailUrl.withResolution(320, 160),
                    789537633487159396,
                    "delphestest",
                    "https://www.twitch.tv/delphestest",
                    "https://static-cdn.jtvnw.net/user-default-pictures-uv/ebe4cd89-b4f4-4cd9-adac-2f30151b4209-profile_image-300x300.png",
                    "Catégorie" to it.game.label
                )
            )
        },
        Statistics(),
        CommandList(
            channel,
            "!help"
        ) { commands ->
            listOf(
                SendMessage(
                    "Liste des commandes : ${commands.joinToString(", ")}"
                )
            )
        },
        StreamUpdate(channel) { changes ->
            listOf(
                SendMessage(
                    changes.joinToString(" | ") { change ->
                        when (change) {
                            is StreamChanges.Title -> {
                                "Nouveau titre : ${change.newTitle}"
                            }
                            is StreamChanges.Game -> {
                                "${change.oldGame.label} ➡ ${change.newGame.label}"
                            }
                        }
                    }
                )
            )
        },
        GameDescription(
            channel,
            "!tufekoi",
            Games.SCIENCE_TECHNOLOGY to "development",
            Games.JUST_CHATTING to "just chatting"
        ),
        Command(
            channel,
            "!ping",
            responses = listOf(SendMessage("pong"))
        )
    )
}