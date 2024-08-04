package fr.delphes.configuration.channel.delphes99

import fr.delphes.configuration.ChannelConfiguration
import fr.delphes.configuration.channel.Games
import fr.delphes.connector.discord.outgoingEvent.DiscordEmbeddedMessage
import fr.delphes.connector.discord.outgoingEvent.DiscordMessage
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.connector.obs.outgoingEvent.ActivateFilter
import fr.delphes.connector.obs.outgoingEvent.ChangeItemPosition
import fr.delphes.connector.obs.outgoingEvent.ChangeItemVisibility
import fr.delphes.connector.obs.outgoingEvent.DeactivateFilter
import fr.delphes.connector.obs.outgoingEvent.RefreshSource
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.StreamChanges
import fr.delphes.connector.twitch.outgoingEvent.ActivateReward
import fr.delphes.connector.twitch.outgoingEvent.CreatePoll
import fr.delphes.connector.twitch.outgoingEvent.DeactivateReward
import fr.delphes.connector.twitch.outgoingEvent.PromoteModerator
import fr.delphes.connector.twitch.outgoingEvent.RemoveModerator
import fr.delphes.connector.twitch.outgoingEvent.RemoveVIP
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.connector.twitch.outgoingEvent.ShoutOut
import fr.delphes.connector.twitch.state.CommandListState
import fr.delphes.feature.NonEditableFeature
import fr.delphes.features.core.botStarted.BotStartedFeature
import fr.delphes.features.discord.NewGuildMemberFeature
import fr.delphes.features.obs.SceneChangedFeature
import fr.delphes.features.obs.SourceFilterActivatedFeature
import fr.delphes.features.overlay.Overlay
import fr.delphes.features.twitch.bitCheer.CustomBitCheer
import fr.delphes.features.twitch.clipCreated.CustomClipCreated
import fr.delphes.features.twitch.command.CustomCommand
import fr.delphes.features.twitch.endCredits.EndCredits
import fr.delphes.features.twitch.gameDescription.GameDescriptionFeature
import fr.delphes.features.twitch.gameReward.GameReward
import fr.delphes.features.twitch.incomingRaid.IncomingRaidFeature
import fr.delphes.features.twitch.newFollow.CustomNewFollow
import fr.delphes.features.twitch.newSub.CustomMessageReceived
import fr.delphes.features.twitch.newSub.CustomNewSub
import fr.delphes.features.twitch.rewardRedeem.RewardRedeem
import fr.delphes.features.twitch.streamOffline.CustomStreamOffline
import fr.delphes.features.twitch.streamOnline.CustomStreamOnline
import fr.delphes.features.twitch.streamUpdated.CustomStreamUpdated
import fr.delphes.features.twitch.streamerHighlight.StreamerHighlightFeature
import fr.delphes.features.twitch.voth.Stats
import fr.delphes.features.twitch.voth.VOTH
import fr.delphes.features.twitch.voth.VOTHState
import fr.delphes.overlay.event.outgoing.Alert
import fr.delphes.overlay.event.outgoing.Pause
import fr.delphes.overlay.event.outgoing.PlaySound
import fr.delphes.rework.feature.FeatureDefinition
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.utils.time.prettyPrint
import fr.delphes.utils.time.secondsOf
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
val wizzFilter = SourceFilter("in_game", "Wizz")

const val discordInvitationLink = "https://discord.com/invite/SAdBhbu"

val moderators = listOf("delphes99", "vivalinux", "gnu_coding_cafe", "delphestest")

fun buildShoutOut(user: UserName): ShoutOut {
    return ShoutOut(
        UserName(user.name),
        channel
    ) { userInfos, channelInformation ->
        val lastStream = userInfos.lastStreamTitle?.let { "« $it », ça vous intrigue ?" } ?: ""
        val currentCategory = channelInformation?.game?.label?.let { " ($it) " } ?: ""

        "\uD83D\uDCFA $lastStream $currentCategory N'hésitez pas à aller voir ${user.name} : https://www.twitch.tv/${user.name.lowercase()}."
    }
}

//TODO item name > item id
private const val RAIN_ITEM_ID = 3L
private const val WEBCAM_ID = 8L

val delphes99Features = listOf<NonEditableFeature>(
    EndCredits(),
    Overlay(channel),
)

val delphes99Channel = ChannelConfiguration.build("configuration-delphes99.properties") { properties ->
    ChannelConfiguration(
        TwitchChannel(properties.getProperty("channel.name")),
        //Sync reward with twitch
        emptyList()
    )
}

val delphes99CustomFeatures = listOf<FeatureDefinition>(
    CustomCommand(
        channel,
        "!discord",
        cooldown = Duration.ofSeconds(10)
    ) {
        executeOutgoingEvent(
            SendMessage(channel, discordInvitationLink)
        )
    },
    CustomCommand(
        channel,
        "!bot",
        cooldown = Duration.ofMinutes(2)
    ) {
        executeOutgoingEvent(
            SendMessage(
                channel,
                "\uD83E\uDD16 C'est moi : https://github.com/delphes99/pythie-bot, roadmap disponible : https://git.io/JOyd6, n'hésitez pas à poster vos idées !"
            )
        )
    },
    CustomCommand(
        channel,
        "!so"
    ) {
        if (moderators.contains(event.data.by.normalizeName)) {
            event.data.parameters.firstOrNull()?.also { promotedUser ->
                executeOutgoingEvent(buildShoutOut(UserName(promotedUser)))
            }
        }
    },
    CustomCommand(
        channel,
        "!deactivateTest"
    ) {
        executeOutgoingEvent(DeactivateReward(Delphes99Rewards.DEV_TEST))
    },
    CustomCommand(
        channel,
        "!activateTest"
    ) {
        executeOutgoingEvent(ActivateReward(Delphes99Rewards.DEV_TEST))
    },
    CustomCommand(
        channel,
        "!ping"
    ) {
        executeOutgoingEvent(SendMessage(channel, "pong"))
    },
    CustomCommand(
        channel,
        "!helloDiscord"
    ) {
        executeOutgoingEvent(DiscordMessage("Coucou discord depuis une commande !", 789537633487159396))
    },
    CustomCommand(
        channel,
        "!mod"
    ) {
        if (moderators.contains(event.data.by.normalizeName)) {
            executeOutgoingEvent(RemoveVIP(event.data.by, event.data.channel))
            executeOutgoingEvent(PromoteModerator(event.data.by, event.data.channel))
        }
    },
    CustomCommand(
        channel,
        "!unmod"
    ) {
        if (moderators.contains(event.data.by.normalizeName)) {
            executeOutgoingEvent(RemoveModerator(event.data.by, event.data.channel))
        }
    },
    CustomCommand(
        channel,
        "!clair"
    ) {
        executeOutgoingEvent(
            PlaySound(
                "clair.mp3"
            )
        )
    },
    CustomCommand(
        channel,
        "!bluff"
    ) {
        executeOutgoingEvent(
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
    },
    CustomCommand(
        channel,
        "!coucou",
        cooldown = secondsOf(10)
    ) {
        executeOutgoingEvent(
            SendMessage(channel, "Coucou ${event.data.by.name} !")
        )
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
                    "ℹ️ Commandes : ${commands.map(Command::triggerMessage).joinToString(", ")}"
                )
            )
        }
    },
    CustomNewFollow(
        channel
    ) {
        executeOutgoingEvent(
            SendMessage(
                channel,
                "\uD83D\uDC9C Merci du follow ${event.data.follower.name} \uD83D\uDE4F"
            )
        )
        executeOutgoingEvent(PlaySound("welcome.mp3"))
    },
    CustomBitCheer(
        channel
    ) {
        executeOutgoingEvent(
            SendMessage(
                channel,
                "💎 ${event.data.cheerer?.name ?: "Un utilisateur anonyme"} vient d'envoyer ${event.data.bitsUsed} bits. Merci beaucoup ! 💎"
            )
        )
    },
    CustomNewSub(
        channel
    ) {
        executeOutgoingEvent(
            SendMessage(channel, "⭐ Merci pour le sub ${event.data.sub.name} \uD83D\uDE4F")
        )
        executeOutgoingEvent(
            PlaySound("thank-you.mp3")
        )
    },
    CustomStreamOnline(
        channel
    ) {
        executeOutgoingEvent(SendMessage(channel, "\uD83D\uDC4B Le stream démarre, ravi de vous revoir !"))
        executeOutgoingEvent(
            DiscordEmbeddedMessage(
                event.data.title,
                "https://www.twitch.tv/delphes99",
                "${event.data.thumbnailUrl.withResolution(320, 160)}?r=${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"))
                }",
                708949759725010984,
                "Delphes99",
                "https://www.twitch.tv/delphes99",
                "https://static-cdn.jtvnw.net/jtv_user_pictures/9bda888d-167b-4e12-83d3-d8519fa45bcd-profile_image-300x300.png",
                "Catégorie" to (event.data.game?.label ?: "Sans catégorie"),
            )
        )
    },
    CustomStreamOffline(
        channel
    ) {
        executeOutgoingEvent(
            SendMessage(channel, "\uD83D\uDE2D Le stream est fini, à la prochaine et des bisous ! \uD83D\uDE18")
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
                            "\uD83D\uDCDD ${change.newTitle}"
                        }

                        is StreamChanges.Game -> {
                            "\uD83D\uDD04 ${change.oldGame?.label ?: "Sans catégorie"} ➡ ${change.newGame?.label ?: "Sans catégorie"}"
                        }
                    }
                }
            )
        )
    },
    CustomClipCreated(
        channel
    ) {
        val clip = event.data.clip
        executeOutgoingEvent(
            SendMessage(
                channel,
                "\uD83C\uDFAC ${clip.creator.name} vient de poster un nouveau clip « ${clip.title} » : ${clip.url}"
            )
        )
        executeOutgoingEvent(
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
        activeStreamer = Duration.ofDays(30),
        excludedUserNames = listOf(UserName("streamlabs")),
        shoutOut = { messageReceived, _ ->
            buildShoutOut(messageReceived.user)
        }
    ),
    IncomingRaidFeature(
        channel = channel,
    ) {
        executeOutgoingEvent(
            SendMessage(
                channel,
                "\uD83E\uDDED ${event.data.leader.name} explore twitch et fait escale ici avec ses ${event.data.numberOfRaiders} acolytes."
            )
        )
        executeOutgoingEvent(buildShoutOut(event.data.leader))
        executeOutgoingEvent(
            CreatePoll(
                channel,
                "Une présentation ?",
                Duration.ofMinutes(1),
                "Qui es-tu ?",
                "Que fais-tu ?",
                "La totale !",
                "On te connait (ou OSEF)"
            )
        )
    },
    BotStartedFeature {
        val overlayItemId = 4L //TODO

        executeOutgoingEvent(Pause(Duration.ofSeconds(5))) //Waiting for connectors connections
        executeOutgoingEvent(RefreshSource("in_game", overlayItemId))
        executeOutgoingEvent(Pause(Duration.ofSeconds(1)))
        executeOutgoingEvent(PlaySound("msn-start.mp3"))
        executeOutgoingEvent(SendMessage(channel, "Ready to go"))
    },
    NewGuildMemberFeature {
        executeOutgoingEvent(
            SendMessage(
                channel,
                "${event.data.user} vient de rejoindre le discord \uD83D\uDC6A, n'hésitez à faire de même ➡ $discordInvitationLink !"
            )
        )
    },
    SceneChangedFeature {
        if (event.data.newScene == "End credits") {
            executeOutgoingEvent(SendMessage(channel, "Ca sent la fin"))
        }
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.DEV_TEST
    ) {
        executeOutgoingEvent(Alert("test"))
        executeOutgoingEvent(
            PlaySound(
                listOf(
                    "kill-1.mp3",
                    "kill-2.mp3",
                    "kill-3.mp3",
                    "kill-4.mp3",
                    "kill-5.mp3"
                ).random()
            )
        )
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.DEV_TEST2
    ) {
        executeOutgoingEvent(SendMessage(channel, "-> test dev 2"))
        executeOutgoingEvent(
            ChangeItemPosition(
                WEBCAM_ID,
                1425.0,
                691.0,
                "in_game"
            )
        )
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.DEV_TEST3
    ) {
        executeOutgoingEvent(SendMessage(channel, "-> test dev 3"))
        executeOutgoingEvent(
            ChangeItemPosition(
                WEBCAM_ID,
                nextDouble(0.0, (1920.0 - 486.0)),
                nextDouble(0.0, (1080.0 - 273.0)),
                "in_game"
            )
        )
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.ENTER_THE_MATRIX
    ) {
        executeOutgoingEvent(ActivateFilter(matrixFilter))
    },
    //TODO : handle pause/async event differently : eventsub timeout
    SourceFilterActivatedFeature {
        if (event.data.filter == matrixFilter) {
            executeOutgoingEvent(Pause(secondsOf(30)))
            executeOutgoingEvent(DeactivateFilter(matrixFilter))
        }
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.RIP,
    ) {
        executeOutgoingEvent(PlaySound("sad.mp3"))
        executeOutgoingEvent(ActivateFilter(blackAndWhiteFilter))
        executeOutgoingEvent(ChangeItemVisibility(RAIN_ITEM_ID, true, "main_capture"))
    },
    SourceFilterActivatedFeature {
        if (event.data.filter == blackAndWhiteFilter) {
            executeOutgoingEvent(Pause(Duration.ofMillis(9700)))
            executeOutgoingEvent(DeactivateFilter(blackAndWhiteFilter))
            executeOutgoingEvent(ChangeItemVisibility(RAIN_ITEM_ID, false, "main_capture"))
        }
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.WIZZ,
    ) {
        executeOutgoingEvent(PlaySound("wizz.mp3"))
        executeOutgoingEvent(ActivateFilter(wizzFilter))
        executeOutgoingEvent(Pause(Duration.ofMillis(800)))
        executeOutgoingEvent(DeactivateFilter(wizzFilter))
    },
    GameDescriptionFeature(
        channel,
        "!tufekoi",
        Games.SOFTWARE_DEVELOPMENT to "Développement d'un bot \uD83E\uDD16 twitch en kotlin : https://github.com/delphes99/pythie-bot",
        Games.JUST_CHATTING to "\uD83D\uDDE3️ bla bla bla",
        Games.SATISFACTORY to "Pionnier, vous êtes envoyé colonniser Jeu de construction d'usine \uD83C\uDFED en première personne, automatisation, optimisation, exploration !",
        Games.PATH_OF_EXILE to "Vous êtes un exilé errant sur Wraeclast, cherchant à vous venger de ceux qui vous ont banni. ⚔️Hack'n slash free to play, avec un système de ligue de quelques mois. Constellation de talent, lien entre gemmes, craft, mécaniques... complexe mais passionnant",
        Games.GEOGUESSR to "Vous entrez dans un streetview dans un lieu aléatoire, vous devez vous retrouver sur une carte \uD83D\uDDFA️",
        Games.GHOSTRUNNER to "Mara, le Maître des clés règne sur la Tour du Dharma, le dernier refuge de l'humanité. Grimpez la Tour et prennez votre revanche. \uD83C\uDFC3 Runner en première personne dans un environnement cyberpunk.",
    ),
    GameReward(
        channel = channel,
        Games.SOFTWARE_DEVELOPMENT to Delphes99Rewards.DEV_TEST,
        Games.SOFTWARE_DEVELOPMENT to Delphes99Rewards.DEV_TEST2,
        Games.SOFTWARE_DEVELOPMENT to Delphes99Rewards.DEV_TEST3,
        Games.SATISFACTORY to Delphes99Rewards.SATISFACTORY_COLOR,
    ),
    VOTH(
        channel,
        rewardId = Delphes99Rewards.VOTH,
        newVipAnnouncer = {
            executeOutgoingEvent(
                SendMessage(
                    channel,
                    listOfNotNull(
                        "\uD83D\uDC51 ${event.rewardRedemption.user.name} devient notre VIP. \uD83D\uDC51",
                        event.oldVOTH?.let { oldVOTH -> " | \uD83D\uDC80 RIP ${oldVOTH.user} [règne : ${event.durationOfReign?.prettyPrint()}] ! \uD83D\uDC80" }
                    ).joinToString(" ")
                )
            )
            executeOutgoingEvent(
                PlaySound(listOf("kill-1.mp3", "kill-2.mp3", "kill-3.mp3", "kill-4.mp3", "kill-5.mp3").random()),
            )
        },
        saveStatePath = "A:\\pythiebot\\feature\\voth.json"
    ),
    CustomCommand(
        channel,
        "!vothstats",
    ) {
        state(VOTHState.idFor(channel))?.getReignsFor(event.data.by)?.also { stats ->
            executeOutgoingEvent(
                SendMessage(
                    channel,
                    "⏲️Durée totale : ${stats.totalTime.prettyPrint()} | " +
                            "\uD83C\uDFC6 Victoires : ${stats.numberOfReigns} | " +
                            "\uD83D\uDCB8 Dépensés : ${stats.totalCost}"
                )
            )
        }
    },
    CustomMessageReceived(
        channel
    ) {
        if (event.data.text.contains("botte", ignoreCase = true)) {
            executeOutgoingEvent(SendMessage(channel, "bot"))
        }
    },
    CustomCommand(
        channel,
        "!vothtop",
    ) {
        state(VOTHState.idFor(channel))
            ?.getTopVip(3)
            ?.takeIf(List<Stats>::isNotEmpty)
            ?.also { stats ->
                val top1 = stats.getOrNull(0)
                val top2 = stats.getOrNull(1)
                val top3 = stats.getOrNull(2)
                executeOutgoingEvent(
                    SendMessage(
                        channel,
                        listOfNotNull(
                            top1?.let { "\uD83E\uDD47 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
                            top2?.let { "\uD83E\uDD48 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
                            top3?.let { "\uD83E\uDD49 ${it.user.name} [${it.totalTime.prettyPrint()}]" },
                        ).joinToString(" ")
                    )
                )
            }
    },
    RewardRedeem(
        channel,
        Delphes99Rewards.DEV_HORS_BOT
    ) {
        executeOutgoingEvent(
            SendMessage(
                channel,
                "⚠️ ${event.data.user.name} a utilisé le reward hors bot ! ⚠️"
            )
        )
    },
)