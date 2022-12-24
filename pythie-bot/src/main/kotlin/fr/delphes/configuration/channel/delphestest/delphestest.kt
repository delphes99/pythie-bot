package fr.delphes.configuration.channel.delphestest

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.configuration.channel.Games
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.state.CommandListState
import fr.delphes.features.twitch.command.CustomCommand
import fr.delphes.features.twitch.gameDescription.GameDescription
import fr.delphes.features.twitch.newFollow.CustomNewFollow
import fr.delphes.features.twitch.statistics.Statistics
import fr.delphes.features.twitch.streamOffline.CustomStreamOffline
import fr.delphes.features.twitch.streamOnline.CustomStreamOnline
import fr.delphes.features.twitch.streamUpdated.CustomStreamUpdated
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.secondsOf
import java.time.Duration

/**
 * Example for delphestest channel : https://www.twitch.tv/delphestest
 */
val channel = TwitchChannel("delphestest")
val delphestestFeatures = listOf(
    Statistics(channel),
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
    CustomCommand(
        channel,
        "!help",
        cooldown = secondsOf(10)
    ) {
        this.state(CommandListState.ID)?.getCommandsOf(channel)?.let { commands ->
            executeOutgoingEvent(
                SendMessage(
                    "Liste des commandes : ${commands.joinToString(", ")}",
                    channel
                )
            )
        }
    },
    CustomNewFollow(
        channel
    ) {
        executeOutgoingEvent(SendMessage("Merci du follow ${event.follower.name}", channel))
    },
    CustomStreamOffline(channel) {
        executeOutgoingEvent(SendMessage("Le stream est fini, au revoir !", channel))
    },
    CustomStreamOnline(channel) {
        executeOutgoingEvent(
            SendMessage("Le stream démarre, ravi de vous revoir !", channel)
        )
    },
    CustomStreamUpdated(
        channel
    ) {
        executeOutgoingEvent(
            SendMessage(
                event.changes.joinToString(" | ") { change ->
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
)