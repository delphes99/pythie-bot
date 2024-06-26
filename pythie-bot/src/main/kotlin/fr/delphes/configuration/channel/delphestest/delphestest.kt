package fr.delphes.configuration.channel.delphestest

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.configuration.channel.Games
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.state.CommandListState
import fr.delphes.features.twitch.command.CustomCommand
import fr.delphes.features.twitch.gameDescription.GameDescriptionFeature
import fr.delphes.features.twitch.newFollow.CustomNewFollow
import fr.delphes.features.twitch.streamOffline.CustomStreamOffline
import fr.delphes.features.twitch.streamOnline.CustomStreamOnline
import fr.delphes.features.twitch.streamUpdated.CustomStreamUpdated
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.secondsOf
import java.time.Duration

/**
 * Example for delphestest channel : https://www.twitch.tv/delphestest
 */
val channel = TwitchChannel("delphestest")
val delphestestChannel = ChannelConfiguration.build("configuration-delphestest.properties") { properties ->
    ChannelConfiguration(
        TwitchChannel(properties.getProperty("channel.name")),
        emptyList()
    )
}
val delphestestCustomFeatures = listOf<FeatureDefinition>(
    CustomCommand(
        channel,
        "!test",
        cooldown = Duration.ofSeconds(10)
    ) {
        listOf(
            SendMessage(channel, "Compte de test opérationnel !")
        )
    },
    CustomCommand(
        channel,
        "!ping"
    ) {
        listOf(SendMessage(channel, "ping ?"))
    },
    CustomCommand(
        channel,
        "!help",
        cooldown = secondsOf(10)
    ) {
        this.state(CommandListState.ID)?.getCommandsOf(channel)?.let { commands ->
            executeOutgoingEvent(
                SendMessage(
                    channel,
                    "Liste des commandes : ${commands.joinToString(", ")}"
                )
            )
        }
    },
    CustomNewFollow(
        channel
    ) {
        executeOutgoingEvent(SendMessage(channel, "Merci du follow ${event.data.follower.name}"))
    },
    CustomStreamOffline(channel) {
        executeOutgoingEvent(SendMessage(channel, "Le stream est fini, au revoir !"))
    },
    CustomStreamOnline(channel) {
        executeOutgoingEvent(
            SendMessage(channel, "Le stream démarre, ravi de vous revoir !")
        )
    },
    CustomStreamUpdated(
        channel
    ) {
        executeOutgoingEvent(
            SendMessage(
                channel,
                event.data.changes.joinToString(" | ") { change ->
                    when (change) {
                        is StreamChanges.Title -> {
                            "Nouveau titre : ${change.newTitle}"
                        }

                        is StreamChanges.Game -> {
                            "${change.oldGame?.label ?: "Sans catégorie"} ➡ ${change.newGame?.label ?: "Sans catégorie"}"
                        }
                    }
                }
            )
        )
    },
    GameDescriptionFeature(
        channel,
        "!tufekoi",
        Games.SCIENCE_TECHNOLOGY to "development",
        Games.JUST_CHATTING to "just chatting"
    ),
)