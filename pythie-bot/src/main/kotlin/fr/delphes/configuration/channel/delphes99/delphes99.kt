package fr.delphes.configuration.channel.delphes99

import fr.delphes.bot.event.incoming.StreamChanges
import fr.delphes.bot.event.outgoing.ActivateReward
import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.DesactivateReward
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.configuration.channel.Games
import fr.delphes.connector.discord.outgoingEvent.DiscordEmbeddedMessage
import fr.delphes.connector.discord.outgoingEvent.DiscordMessage
import fr.delphes.feature.endCredits.EndCredits
import fr.delphes.feature.statistics.Statistics
import fr.delphes.features.discord.NewGuildMember
import fr.delphes.features.overlay.Overlay
import fr.delphes.features.twitch.bitCheer.BitCheer
import fr.delphes.features.twitch.command.Command
import fr.delphes.features.twitch.commandList.CommandList
import fr.delphes.features.twitch.gameDescription.GameDescription
import fr.delphes.features.twitch.gameReward.GameReward
import fr.delphes.features.twitch.newFollow.NewFollow
import fr.delphes.features.twitch.newSub.NewSub
import fr.delphes.features.twitch.rewardRedeem.RewardRedeem
import fr.delphes.features.twitch.streamOffline.StreamOffline
import fr.delphes.features.twitch.streamOnline.StreamOnline
import fr.delphes.features.twitch.streamUpdate.StreamUpdate
import fr.delphes.features.twitch.voth.FileVOTHStateRepository
import fr.delphes.features.twitch.voth.VOTH
import fr.delphes.features.twitch.voth.VOTHConfiguration
import fr.delphes.utils.time.prettyPrint
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Example for delphes99 channel : https://www.twitch.tv/delphes99
 */
val delphes99Channel = ChannelConfiguration.build("configuration-delphes99.properties") { properties ->
    ChannelConfiguration(
        properties.getProperty("channel.name"),
        properties.getProperty("account.oAuth"),
        DelphesReward.toRewardList(),
        VOTH(
            VOTHConfiguration(
                reward = DelphesReward.VOTH,
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
                                    top1.let { "\uD83E\uDD47 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
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
        StreamOnline {
            listOf(
                SendMessage("\uD83D\uDC4B Le stream démarre, ravi de vous revoir !"),
                DiscordEmbeddedMessage(
                    it.title,
                    "https://www.twitch.tv/delphes99",
                    "${it.thumbnailUrl.withResolution(320, 160)}?r=${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))}",
                    708949759725010984,
                    "Delphes99",
                    "https://www.twitch.tv/delphes99",
                    "https://static-cdn.jtvnw.net/jtv_user_pictures/9bda888d-167b-4e12-83d3-d8519fa45bcd-profile_image-300x300.png",
                    "Catégorie" to it.game.label
                )
            )
        },
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
            DelphesReward.DEV_TEST
        ) {
            listOf(
                SendMessage("-> test dev"),
                Alert("test")
            )
        },
        RewardRedeem(
            DelphesReward.DEV_TEST2
        ) {
            listOf(
                DiscordMessage("Coucou discord depuis une récompense !", 789537633487159396)
            )
        },
        GameReward(
            DelphesReward.DEV_TEST to Games.SCIENCE_TECHNOLOGY,
            DelphesReward.DEV_TEST2 to Games.SCIENCE_TECHNOLOGY,
            DelphesReward.SATISFACTORY_COLOR to Games.SATISFACTORY
        ),
        Command(
            "!deactivateTest",
            responses = listOf(DesactivateReward(DelphesReward.DEV_TEST))
        ),
        Command(
            "!activateTest",
            responses = listOf(ActivateReward(DelphesReward.DEV_TEST))
        ),
        Command(
            "!ping",
            responses = listOf(SendMessage("pong"))
        ),
        Command(
            "!helloDiscord",
            responses = listOf(DiscordMessage("Coucou discord depuis une commande !", 789537633487159396))
        ),
        NewGuildMember { newGuildMember ->
            listOf(
                SendMessage("${newGuildMember.user} vient de rejoindre le discord \uD83D\uDC6A, n'hésitez à faire de même !")
            )
        }
    )
}