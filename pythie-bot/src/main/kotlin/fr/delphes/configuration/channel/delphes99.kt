package fr.delphes.configuration.channel

import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.DesactivateReward
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.command.Command
import fr.delphes.feature.newFollow.NewFollow
import fr.delphes.feature.newSub.NewSub
import fr.delphes.feature.statistics.Statistics
import fr.delphes.feature.streamOffline.StreamOffline
import fr.delphes.feature.streamOnline.StreamOnline
import fr.delphes.feature.voth.FileVOTHStateRepository
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHConfiguration
import fr.delphes.bot.util.time.prettyPrint
import fr.delphes.feature.bitCheer.BitCheer
import fr.delphes.feature.commandList.CommandList
import fr.delphes.feature.endCredits.EndCredits
import fr.delphes.feature.streamUpdate.StreamUpdate
import fr.delphes.feature.gameDescription.GameDescription
import fr.delphes.feature.gameReward.GameReward
import fr.delphes.feature.overlay.Overlay
import fr.delphes.feature.rewardRedeem.RewardRedeem
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.Reward
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
                                "\uD83D\uDC51 ${announce.rewardRedemption.user.name} est le nouveau VIP. \uD83D\uDC51",
                                announce.oldVOTH?.let { oldVOTH -> " | \uD83D\uDC80 RIP ${oldVOTH.user} [règne : ${announce.durationOfReign?.prettyPrint()}] ! \uD83D\uDC80" }
                            ).joinToString(" ")
                        )
                    )
                },
                statsCommand = "!vothstats",
                statsResponse = { stats ->
                    listOf(
                        SendMessage(
                            "⏲️Durée totale : ${stats.totalTime.prettyPrint()} | " +
                                    "\uD83C\uDFC6 Victoires : ${stats.numberOfReigns} | " +
                                    "\uD83D\uDCB8 Dépensés : ${stats.totalCost}"
                        )
                    )
                },
                top3Command = "!vothtop",
                top3Response = { top1, top2, top3 ->
                    if (top1 == null) {
                        emptyList()
                    } else {
                        listOf(
                            SendMessage(
                                listOfNotNull(
                                    top1?.let { "\uD83E\uDD47 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
                                    top2?.let { "\uD83E\uDD48 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
                                    top3?.let { "\uD83E\uDD49 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
                                ).joinToString(" ")
                            )
                        )
                    }
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
        NewFollow { newFollow ->
            listOf(SendMessage("\uD83D\uDC9C Merci du follow ${newFollow.follower.name} \uD83D\uDE4F"))
        },
        NewSub { newSub ->
            listOf(SendMessage("⭐ Merci pour le sub ${newSub.sub.name} \uD83D\uDE4F"))
        },
        StreamOffline { listOf(SendMessage("\uD83D\uDE2D Le stream est fini, à la prochaine et des bisous ! \uD83D\uDE18")) },
        StreamOnline { listOf(SendMessage("\uD83D\uDC4B Le stream démarre, ravi de vous revoir !")) },
        Statistics(),
        EndCredits(),
        Overlay(),
        CommandList(
            "!help"
        ) { commands ->
            listOf(
                SendMessage(
                    "ℹ️ Commandes : ${commands.joinToString(", ")}"
                )
            )
        },
        StreamUpdate { changes ->
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
        BitCheer { bitCheered ->
            listOf(
                SendMessage("💎 ${bitCheered.cheerer?.name ?: "Un utilisateur anonyme"} vient d'envoyer ${bitCheered.bitsUsed} bits. Merci beaucoup ! 💎")
            )
        },
        GameDescription(
            "!tufekoi",
            Games.SCIENCE_TECHNOLOGY to "Développement d'un bot \uD83E\uDD16 twitch en kotlin : https://github.com/delphes99/pythie-bot",
            Games.JUST_CHATTING to "\uD83D\uDDE3️ bla bla bla",
            Games.SATISFACTORY to "Jeu de construction d'usine \uD83C\uDFED où on doit coloniser une planète inconnue",
            Games.PATH_OF_EXILE to "⚔️Hack'n slash free to play, avec un système de ligue de quelques mois. Constellation de talent, lien entre gemmes, craft, mécaniques... complexe mais passionnant",
            Games.GEOGUESSR to "Vous entrez dans un streetview dans un lieu aléatoire, vous devez vous retrouver sur une carte \uD83D\uDDFA️"
        ),
        RewardRedeem(
            "Test dev"
        ) { event ->
            listOf(
                Alert("test")
            )
        },
        GameReward(
            mapOf(
                Games.SCIENCE_TECHNOLOGY to listOf(Reward("676834c5-fb74-4e9e-96bf-d10605d3f2b1", "Test dev"))
            )
        ),
        Command(
            "!test",
            responses = listOf(DesactivateReward(Reward("676834c5-fb74-4e9e-96bf-d10605d3f2b1", "Test dev")))
        ),
        Command(
            "!ping",
            responses = listOf(SendMessage("pong"))
        )
    )
}