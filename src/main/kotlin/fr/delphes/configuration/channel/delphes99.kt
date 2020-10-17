package fr.delphes.configuration.channel

import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.command.Command
import fr.delphes.feature.newFollow.NewFollowFeature
import fr.delphes.feature.newSub.NewSubFeature
import fr.delphes.feature.statistics.Statistics
import fr.delphes.feature.streamOffline.StreamOfflineFeature
import fr.delphes.feature.streamOnline.StreamOnlineFeature
import fr.delphes.feature.voth.FileVOTHStateRepository
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHConfiguration
import fr.delphes.bot.time.prettyPrint
import fr.delphes.feature.commandList.CommandList
import fr.delphes.feature.streamUpdate.StreamUpdateFeature
import java.time.Duration

/**
 * Example for delphes99 channel : https://www.twitch.tv/delphes99
 */
val delphes99Channel = ChannelConfiguration.build("configuration-delphes99.properties") { properties ->
    ChannelConfiguration(
        properties.getProperty("channel.name"),
        properties.getProperty("account.oAuth"),
        VOTH(
            VOTHConfiguration(
                featureId = properties.getProperty("voth.featureID"),
                newVipAnnouncer = { announce ->
                    listOf(
                        SendMessage(
                            listOfNotNull(
                                "@${announce.rewardRedemption.user.name} est le nouveau VIP.",
                                announce.oldVOTH?.let { oldVOTH -> " \r\nRIP @${oldVOTH.user}, ancien VIP depuis ${announce.durationOfReign?.prettyPrint()} !" }
                            ).joinToString(" ")
                        )
                    )
                },
                statsCommand = "!vothstats",
                statsResponseEvents = { stats ->
                    listOf(
                        SendMessage(
                            "Temps passé en tant que VOTH : ${stats.totalTime.prettyPrint()} --- " +
                                    "Nombre de victoires : ${stats.numberOfReigns} --- " +
                                    "Total de points dépensés : ${stats.totalCost}"
                        )
                    )
                }
            ),
            stateRepository = FileVOTHStateRepository(
                "A:\\pythiebot\\vothstate.json"
            )
        ),
        Command(
            "!bot",
            cooldown = Duration.ofMinutes(2),
            responses = listOf(
                SendMessage("C'est moi : https://github.com/delphes99/pythie-bot")
            )
        ),
        Command(
            "!discord",
            cooldown = Duration.ofSeconds(10),
            responses = listOf(
                SendMessage("https://discord.com/invite/SAdBhbu")
            )
        ),
        NewFollowFeature { newFollow ->
            listOf(SendMessage("Merci du follow ${newFollow.follower.name}"))
        },
        NewSubFeature { newSub ->
            listOf(SendMessage("Merci pour le sub ${newSub.sub.name}"))
        },
        StreamOfflineFeature { listOf(SendMessage("Le stream est fini, au revoir !")) },
        StreamOnlineFeature { listOf(SendMessage("Le stream démarre, ravi de vous revoir !")) },
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
        StreamUpdateFeature { changes ->
            listOf(
                SendMessage(
                    changes.joinToString(" | ") { change ->
                        when (change) {
                            is StreamChanges.Title -> {
                                "\uD83D\uDCDD Nouveau titre : ${change.newTitle}"
                            }
                            is StreamChanges.Game -> {
                                "\uD83D\uDD04 ${change.oldGame.label} ➡ ${change.newGame.label}"
                            }
                        }
                    }
                )
            )
        }
    )
}