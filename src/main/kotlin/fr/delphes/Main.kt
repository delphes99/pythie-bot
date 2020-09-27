package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.configuration.PropertiesConfiguration
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.command.Command
import fr.delphes.feature.newFollow.NewFollowFeature
import fr.delphes.feature.streamOffline.StreamOfflineFeature
import fr.delphes.feature.streamOnline.StreamOnlineFeature
import fr.delphes.feature.voth.FileVOTHStateRepository
import fr.delphes.feature.voth.VOTH
import fr.delphes.feature.voth.VOTHConfiguration
import fr.delphes.time.prettyPrint
import java.time.Duration

fun main() {
    Bot.build(
        PropertiesConfiguration(),
        listOf(
            VOTH(
                VOTHConfiguration(
                    featureId = "27e063b4-b6f8-44c0-8d1b-039745a4772a",
                    newVipAnnouncer = { announce ->
                        listOf(
                            SendMessage(
                                listOfNotNull(
                                    "@${announce.rewardRedemption.user.name} est le nouveau VIP.",
                                    announce.oldVOTH ?.let { oldVOTH -> " \r\nRIP @${oldVOTH.user}, ancien VIP depuis ${announce.durationOfReign?.prettyPrint()} !" }
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
                reponses = listOf(
                    SendMessage("C'est moi : https://github.com/delphes99/pythie-bot")
                )
            ),
            Command(
                "!discord",
                cooldown = Duration.ofSeconds(10),
                reponses = listOf(
                    SendMessage("https://discord.com/invite/SAdBhbu")
                )
            ),
            NewFollowFeature {
                newFollow -> listOf(SendMessage("Merci du follow ${newFollow.follower.name}"))
            },
            StreamOfflineFeature { listOf(SendMessage("Le stream est fini, au revoir !")) },
            StreamOnlineFeature { listOf(SendMessage("Le stream démarre, ravi de vous revoir !")) }
        )
    )
}