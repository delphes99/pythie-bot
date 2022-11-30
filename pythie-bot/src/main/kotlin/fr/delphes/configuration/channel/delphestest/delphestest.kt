package fr.delphes.configuration.channel

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.connector.discord.outgoingEvent.DiscordEmbeddedMessage
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.features.twitch.command.CustomCommand
import fr.delphes.features.twitch.commandList.CommandList
import fr.delphes.features.twitch.gameDescription.GameDescription
import fr.delphes.features.twitch.newFollow.NewFollow
import fr.delphes.features.twitch.statistics.Statistics
import fr.delphes.features.twitch.streamOffline.StreamOffline
import fr.delphes.features.twitch.streamOnline.StreamOnline
import fr.delphes.features.twitch.streamUpdate.StreamUpdate
import fr.delphes.twitch.TwitchChannel
import java.time.Duration

/**
 * Example for delphestest channel : https://www.twitch.tv/delphestest
 */
val channel = TwitchChannel("delphestest")
val delphestestFeatures = listOf(
    NewFollow(channel) { newFollow ->
        listOf(SendMessage("Merci du follow ${newFollow.follower.name}", channel))
    },
    StreamOffline(channel) { listOf(SendMessage("Le stream est fini, au revoir !", channel)) },
    StreamOnline(channel) {
        listOf(
            SendMessage("Le stream démarre, ravi de vous revoir !", channel),
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
    Statistics(channel),
    CommandList(
        channel,
        "!help"
    ) { commands ->
        listOf(
            SendMessage(
                "Liste des commandes : ${commands.joinToString(", ")}",
                channel
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
                },
                channel
            )
        )
    },
    GameDescription(
        channel,
        "!tufekoi",
        Games.SCIENCE_TECHNOLOGY to "development",
        Games.JUST_CHATTING to "just chatting"
    ),
)
val delphestestChannel = ChannelConfiguration.build("configuration-delphestest.properties") { properties ->
    ChannelConfiguration(
        TwitchChannel(properties.getProperty("channel.name")),
        emptyList()
    )
}
val delphestestCustomFeatures = listOf(
    CustomCommand(
        channel,
        "!test",
        cooldown = Duration.ofSeconds(10)
    ) {
        listOf(
            SendMessage("Compte de test opérationnel !", channel)
        )
    },
    CustomCommand(
        channel,
        "!ping"
    ) {
        listOf(SendMessage("ping ?", channel))
    },
)