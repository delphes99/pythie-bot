package fr.delphes.configuration.channel

import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.command.Command
import fr.delphes.feature.commandList.CommandList
import fr.delphes.feature.newFollow.NewFollow
import fr.delphes.feature.statistics.Statistics
import fr.delphes.feature.streamOffline.StreamOffline
import fr.delphes.feature.streamOnline.StreamOnline
import fr.delphes.feature.streamUpdate.StreamUpdate
import fr.delphes.feature.gameDescription.GameDescription
import java.time.Duration

/**
 * Example for delphestest channel : https://www.twitch.tv/delphestest
 */
val delphestestChannel = ChannelConfiguration.build("configuration-delphestest.properties") { properties ->
    ChannelConfiguration(
        properties.getProperty("channel.name"),
        properties.getProperty("account.oAuth"),
        Command(
            "!test",
            cooldown = Duration.ofSeconds(10),
            responses = listOf(
                SendMessage("Compte de test opérationnel !")
            )
        ),
        NewFollow { newFollow ->
            listOf(SendMessage("Merci du follow ${newFollow.follower.name}"))
        },
        StreamOffline { listOf(SendMessage("Le stream est fini, au revoir !")) },
        StreamOnline { listOf(SendMessage("Le stream démarre, ravi de vous revoir !")) },
        Statistics(),
        CommandList(
            "!help"
        ) { commands ->
            listOf(
                SendMessage(
                    "Liste des commandes : ${commands.joinToString(", ")}"
                )
            )
        },
        StreamUpdate { changes ->
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
            "!tufekoi",
            Games.SCIENCE_TECHNOLOGY to "development",
            Games.JUST_CHATTING to "just chatting"
        )
    )
}