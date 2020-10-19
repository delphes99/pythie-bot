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
import fr.delphes.bot.util.time.prettyPrint
import fr.delphes.feature.bitCheer.BitCheerFeature
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
                                "\uD83D\uDC51 @${announce.rewardRedemption.user.name} est le nouveau VIP. \uD83D\uDC51",
                                announce.oldVOTH?.let { oldVOTH -> " | \uD83D\uDC80 RIP @${oldVOTH.user} [règne : ${announce.durationOfReign?.prettyPrint()}] ! \uD83D\uDC80" }
                            ).joinToString(" ")
                        )
                    )
                },
                statsCommand = "!vothstats",
                statsResponseEvents = { stats ->
                    listOf(
                        SendMessage(
                            "⏲️Durée totale : ${stats.totalTime.prettyPrint()} | " +
                                    "\uD83C\uDFC6 Victoires : ${stats.numberOfReigns} | " +
                                    "\uD83D\uDCB8 Dépensés : ${stats.totalCost}"
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
                SendMessage("\uD83E\uDD16 C'est moi : https://github.com/delphes99/pythie-bot")
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
            listOf(SendMessage("\uD83D\uDC9C Merci du follow ${newFollow.follower.name} \uD83D\uDE4F"))
        },
        NewSubFeature { newSub ->
            listOf(SendMessage("⭐ Merci pour le sub ${newSub.sub.name} \uD83D\uDE4F"))
        },
        StreamOfflineFeature { listOf(SendMessage("\uD83D\uDE2D Le stream est fini, à la prochaine et des bisous ! \uD83D\uDE18")) },
        StreamOnlineFeature { listOf(SendMessage("\uD83D\uDC4B Le stream démarre, ravi de vous revoir !")) },
        Statistics(),
        CommandList(
            "!help"
        ) { commands ->
            listOf(
                SendMessage(
                    "ℹ️ Commandes : ${commands.joinToString(", ")}"
                )
            )
        },
        StreamUpdateFeature { changes ->
            listOf(
                SendMessage(
                    changes.joinToString(" | ") { change ->
                        when (change) {
                            is StreamChanges.Title -> {
                                "\uD83D\uDCDD ${change.newTitle}"
                            }
                            is StreamChanges.Game -> {
                                "\uD83D\uDD04 ${change.oldGame.label} ➡ ${change.newGame.label}"
                            }
                        }
                    }
                )
            )
        },
        BitCheerFeature { bitCheered ->
            listOf(
                SendMessage("💎 ${bitCheered.cheerer.name} vient d'envoyer ${bitCheered.bitsUsed} bits. Merci beaucoup ! 💎")
            )
        }
    )
}