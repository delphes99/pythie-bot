package fr.delphes.configuration.channel.delphes99

import fr.delphes.bot.event.outgoing.Alert
import fr.delphes.bot.event.outgoing.Pause
import fr.delphes.bot.event.outgoing.PlaySound
import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.configuration.channel.Games
import fr.delphes.connector.discord.outgoingEvent.DiscordEmbeddedMessage
import fr.delphes.connector.discord.outgoingEvent.DiscordMessage
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.connector.obs.outgoingEvent.ActivateFilter
import fr.delphes.connector.obs.outgoingEvent.ChangeItemPosition
import fr.delphes.connector.obs.outgoingEvent.ChangeItemVisibility
import fr.delphes.connector.obs.outgoingEvent.DeactivateFilter
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.outgoingEvent.ActivateReward
import fr.delphes.connector.twitch.outgoingEvent.CreatePoll
import fr.delphes.connector.twitch.outgoingEvent.DeactivateReward
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.features.discord.NewGuildMember
import fr.delphes.features.obs.SceneChanged
import fr.delphes.features.obs.SourceFilterActivated
import fr.delphes.features.overlay.Overlay
import fr.delphes.features.twitch.bitCheer.BitCheer
import fr.delphes.features.twitch.clipCreated.ClipCreated
import fr.delphes.features.twitch.command.Command
import fr.delphes.features.twitch.commandList.CommandList
import fr.delphes.features.twitch.endCredits.EndCredits
import fr.delphes.features.twitch.gameDescription.GameDescription
import fr.delphes.features.twitch.gameReward.GameReward
import fr.delphes.features.twitch.incomingRaid.IncomingRaid
import fr.delphes.features.twitch.newFollow.NewFollow
import fr.delphes.features.twitch.newSub.NewSub
import fr.delphes.features.twitch.rewardRedeem.RewardRedeem
import fr.delphes.features.twitch.statistics.Statistics
import fr.delphes.features.twitch.streamOffline.StreamOffline
import fr.delphes.features.twitch.streamOnline.StreamOnline
import fr.delphes.features.twitch.streamUpdate.StreamUpdate
import fr.delphes.features.twitch.streamerHighlight.FileStreamerHighlightRepository
import fr.delphes.features.twitch.streamerHighlight.StreamerHighlightFeature
import fr.delphes.features.twitch.voth.FileVOTHStateRepository
import fr.delphes.features.twitch.voth.VOTH
import fr.delphes.features.twitch.voth.VOTHConfiguration
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.time.prettyPrint
import fr.delphes.utils.time.secondsOf
import kotlinx.serialization.InternalSerializationApi
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random.Default.nextDouble

/**
 * Example for delphes99 channel : https://www.twitch.tv/delphes99
 */
val channel = TwitchChannel("delphes99")
val matrixFilter = SourceFilter("webcam", "matrix")
val blackAndWhiteFilter = SourceFilter("main_capture", "black_and_white")

val discordInvitationLink = "https://discord.com/invite/SAdBhbu"

@InternalSerializationApi
val delphes99Features = listOf(
    VOTH(
        channel,
        VOTHConfiguration(
            reward = DelphesReward.VOTH,
            newVipAnnouncer = { announce ->
                listOf(
                    SendMessage(
                        listOfNotNull(
                            "\uD83D\uDC51 ${announce.rewardRedemption.user.name} devient notre VIP. \uD83D\uDC51",
                            announce.oldVOTH?.let { oldVOTH -> " | \uD83D\uDC80 RIP ${oldVOTH.user} [règne : ${announce.durationOfReign?.prettyPrint()}] ! \uD83D\uDC80" }
                        ).joinToString(" "),
                        channel
                    ),
                    PlaySound(listOf("kill-1.mp3", "kill-2.mp3", "kill-3.mp3", "kill-4.mp3", "kill-5.mp3").random()),
                )
            },
            statsCommand = "!vothstats",
            statsResponse = { stats ->
                listOf(
                    SendMessage(
                        "⏲️Durée totale : ${stats.totalTime.prettyPrint()} | " +
                                "\uD83C\uDFC6 Victoires : ${stats.numberOfReigns} | " +
                                "\uD83D\uDCB8 Dépensés : ${stats.totalCost}",
                        channel
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
                            ).joinToString(" "),
                            channel
                        )
                    )
                }
            }
        ),
        stateRepository = FileVOTHStateRepository(
            "A:\\pythiebot\\feature\\voth.json"
        )
    ),
    Command(
        channel,
        "!bot",
        cooldown = Duration.ofMinutes(2),
        responses = {
            listOf(
                SendMessage(
                    "\uD83E\uDD16 C'est moi : https://github.com/delphes99/pythie-bot, roadmap disponible : https://git.io/JOyd6, n'hésitez pas à poster vos idées !",
                    channel
                )
            )
        }
    ),
    Command(
        channel,
        "!discord",
        cooldown = Duration.ofSeconds(10),
        responses = {
            listOf(
                SendMessage(discordInvitationLink, channel)
            )
        }
    ),
    NewFollow(channel) { newFollow ->
        listOf(
            SendMessage("\uD83D\uDC9C Merci du follow ${newFollow.follower.name} \uD83D\uDE4F", channel),
            PlaySound("welcome.mp3"),
        )
    },
    NewSub(channel) { newSub ->
        listOf(
            SendMessage("⭐ Merci pour le sub ${newSub.sub.name} \uD83D\uDE4F", channel),
            PlaySound("thank-you.mp3"),
        )
    },
    StreamOffline(channel) {
        listOf(
            SendMessage(
                "\uD83D\uDE2D Le stream est fini, à la prochaine et des bisous ! \uD83D\uDE18",
                channel
            )
        )
    },
    StreamOnline(channel) {
        listOf(
            SendMessage("\uD83D\uDC4B Le stream démarre, ravi de vous revoir !", channel),
            DiscordEmbeddedMessage(
                it.title,
                "https://www.twitch.tv/delphes99",
                "${it.thumbnailUrl.withResolution(320, 160)}?r=${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
                }",
                708949759725010984,
                "Delphes99",
                "https://www.twitch.tv/delphes99",
                "https://static-cdn.jtvnw.net/jtv_user_pictures/9bda888d-167b-4e12-83d3-d8519fa45bcd-profile_image-300x300.png",
                "Catégorie" to it.game.label
            )
        )
    },
    Statistics(channel),
    EndCredits(),
    Overlay(channel),
    CommandList(
        channel,
        "!help"
    ) { commands ->
        listOf(
            SendMessage(
                "ℹ️ Commandes : ${commands.joinToString(", ")}",
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
                            "\uD83D\uDCDD ${change.newTitle}"
                        }
                        is StreamChanges.Game -> {
                            "\uD83D\uDD04 ${change.oldGame.label} ➡ ${change.newGame.label}"
                        }
                    }
                },
                channel
            )
        )
    },
    BitCheer(channel) { bitCheered ->
        listOf(
            SendMessage(
                "💎 ${bitCheered.cheerer?.name ?: "Un utilisateur anonyme"} vient d'envoyer ${bitCheered.bitsUsed} bits. Merci beaucoup ! 💎",
                channel
            )
        )
    },
    GameDescription(
        channel,
        "!tufekoi",
        Games.SOFTWARE_DEVELOPMENT to "Développement d'un bot \uD83E\uDD16 twitch en kotlin : https://github.com/delphes99/pythie-bot",
        Games.JUST_CHATTING to "\uD83D\uDDE3️ bla bla bla",
        Games.SATISFACTORY to "Pionnier, vous êtes envoyé colonniser Jeu de construction d'usine \uD83C\uDFED en première personne, automatisation, optimisation, exploration !",
        Games.PATH_OF_EXILE to "Vous êtes un exilé errant sur Wraeclast, cherchant à vous venger de ceux qui vous ont banni. ⚔️Hack'n slash free to play, avec un système de ligue de quelques mois. Constellation de talent, lien entre gemmes, craft, mécaniques... complexe mais passionnant",
        Games.GEOGUESSR to "Vous entrez dans un streetview dans un lieu aléatoire, vous devez vous retrouver sur une carte \uD83D\uDDFA️",
        Games.GHOSTRUNNER to "Mara, le Maître des clés règne sur la Tour du Dharma, le dernier refuge de l'humanité. Grimpez la Tour et prennez votre revanche. \uD83C\uDFC3 Runner en première personne dans un environnement cyberpunk.",
    ),
    RewardRedeem(
        channel,
        DelphesReward.DEV_TEST
    ) {
        listOf(
            SendMessage("-> test dev", channel),
            Alert("test"),
            PlaySound(listOf("kill-1.mp3", "kill-2.mp3", "kill-3.mp3", "kill-4.mp3", "kill-5.mp3").random())
        )
    },
    RewardRedeem(
        channel,
        DelphesReward.DEV_TEST2
    ) {
        listOf(
            ChangeItemPosition("webcam", 1028.0, 784.0),
        )
    },
    RewardRedeem(
        channel,
        DelphesReward.DEV_TEST3
    ) {
        listOf(
            ChangeItemPosition("webcam", nextDouble(0.0, (1920.0 - 486.0)), nextDouble(0.0, (1080.0 - 273.0))),
        )
    },
    RewardRedeem(
        channel,
        DelphesReward.ENTER_THE_MATRIX
    ) {
        listOf(
            ActivateFilter(matrixFilter),
        )
    },
    SourceFilterActivated { sourceFilterActivated ->
        if (sourceFilterActivated.filter == matrixFilter) {
            listOf(
                Pause(secondsOf(30)),
                DeactivateFilter(matrixFilter),
            )
        } else {
            emptyList()
        }
    },
    GameReward(
        channel,
        DelphesReward.DEV_TEST to Games.SOFTWARE_DEVELOPMENT,
        DelphesReward.DEV_TEST2 to Games.SOFTWARE_DEVELOPMENT,
        DelphesReward.DEV_TEST3 to Games.SOFTWARE_DEVELOPMENT,
        DelphesReward.SATISFACTORY_COLOR to Games.SATISFACTORY
    ),
    Command(
        channel,
        "!deactivateTest",
        responses = {
            listOf(DeactivateReward(DelphesReward.DEV_TEST, channel))
        }
    ),
    Command(
        channel,
        "!activateTest",
        responses = {
            listOf(ActivateReward(DelphesReward.DEV_TEST, channel))
        }
    ),
    Command(
        channel,
        "!ping",
        responses = {
            listOf(SendMessage("pong", channel))
        }
    ),
    Command(
        channel,
        "!helloDiscord",
        responses = {
            listOf(DiscordMessage("Coucou discord depuis une commande !", 789537633487159396))
        }
    ),
    Command(
        channel,
        "!bluff",
        responses = {
            listOf(
                PlaySound(
                    listOf(
                        "bluff-1.mp3",
                        "bluff-2.mp3",
                        "bluff-3.mp3",
                        "bluff-4.mp3",
                        "bluff-5.mp3",
                        "bluff-6.mp3",
                        "bluff-7.mp3",
                        "bluff-8.mp3",
                        "bluff-9.mp3",
                        "bluff-10.mp3",
                        "bluff-11.mp3",
                        "bluff-12.mp3",
                        "bluff-13.mp3",
                        "bluff-14.mp3",
                        "bluff-15.mp3",
                    ).random()
                )
            )
        }
    ),
    Command(
        channel,
        "!clair",
        responses = {
            listOf(
                PlaySound(
                    "clair.mp3"
                )
            )
        }
    ),
    RewardRedeem(
        channel,
        DelphesReward.RIP,
    ) {
        listOf(
            PlaySound("sad.mp3"),
            ActivateFilter(blackAndWhiteFilter),
            ChangeItemVisibility("rain", true, "main_capture")
        )
    },
    SourceFilterActivated { sourceFilterActivated ->
        if (sourceFilterActivated.filter == blackAndWhiteFilter) {
            listOf(
                Pause(Duration.ofMillis(9700)),
                DeactivateFilter(blackAndWhiteFilter),
                ChangeItemVisibility("rain", false, "main_capture")
            )
        } else {
            emptyList()
        }
    },
    NewGuildMember { newGuildMember ->
        listOf(
            SendMessage(
                "${newGuildMember.user} vient de rejoindre le discord \uD83D\uDC6A, n'hésitez à faire de même ➡ $discordInvitationLink !",
                channel
            )
        )
    },
    ClipCreated(channel) { clipCreated ->
        val clip = clipCreated.clip
        listOf(
            SendMessage(
                "\uD83C\uDFAC ${clip.creator.name} vient de poster un nouveau clip « ${clip.title} » : ${clip.url}",
                channel
            ),
            DiscordEmbeddedMessage(
                clip.title,
                clip.url,
                "${clip.thumbnailUrl.withResolution(320, 160)}?r=${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
                }",
                752185895050018937,
                clip.creator.name,
                "https://www.twitch.tv/${clip.creator.name}",
                null,
                "Catégorie" to clip.gameId
            )
        )
    },
    StreamerHighlightFeature(
        channel = channel,
        highlightExpiration = Duration.ofHours(2),
        stateRepository = FileStreamerHighlightRepository(
            "A:\\pythiebot\\feature\\streamer_highlight.json"
        ),
        response = { messageReceived, user ->
            if (user.name == "streamlabs") {
                emptyList()
            } else {
                listOf(
                    SendMessage(
                        "\uD83D\uDCFA Vous voulez voir du ${user.categories.joinToString(" ou ")}, n'hésitez pas à aller voir ${messageReceived.user.name} : https://www.twitch.tv/${messageReceived.user.normalizeName}",
                        channel
                    )
                )
            }
        }
    ),
    IncomingRaid(channel) { incomingRaid ->
        listOf(
            SendMessage(
                "\uD83E\uDDED ${incomingRaid.leader.name} explore twitch et fait escale ici avec ses ${incomingRaid.numberOfRaiders} acolytes.",
                channel
            ),
            CreatePoll(
                channel,
                "Une présentation ?",
                Duration.ofMinutes(1),
                "Qui es-tu ?",
                "Que fais-tu ?",
                "La totale !",
                "On te connait (ou OSEF)"
            ),
        )
    },
    Command(
        channel,
        "!coucou",
        cooldown = secondsOf(10),
        responses = { commandAsked ->
            listOf(
                SendMessage("Coucou ${commandAsked.by.name} !", channel)
            )
        }
    ),
    SceneChanged { sceneChanged ->
        if (sceneChanged.newScene == "End credits") {
            listOf(SendMessage("Ca sent la fin", channel))
        } else {
            emptyList()
        }
    },
)
val delphes99Channel = ChannelConfiguration.build("configuration-delphes99.properties") { properties ->
    ChannelConfiguration(
        TwitchChannel(properties.getProperty("channel.name")),
        DelphesReward.toRewardList()
    )
}