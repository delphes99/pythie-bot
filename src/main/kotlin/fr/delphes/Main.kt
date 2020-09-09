package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.configuration.PropertiesConfiguration
import fr.delphes.feature.command.Command
import fr.delphes.feature.voth.VOTH
import fr.delphes.time.prettyPrint
import java.time.Duration

fun main() {
    Bot.build(
        PropertiesConfiguration(),
        listOf(
            VOTH("27e063b4-b6f8-44c0-8d1b-039745a4772a") { announce ->
                val announceOldVOTH = announce.oldVOTH?.let { oldVOTH -> " \r\nRIP @${oldVOTH.user}, ancien VIP depuis ${announce.durationOfReign?.prettyPrint()} !" } ?: ""
                "@${announce.rewardRedemption.user.name} est le nouveau VIP.$announceOldVOTH"
            },
            Command(
                "!bot",
                "C'est moi : https://github.com/delphes99/pythie-bot",
                cooldown = Duration.ofMinutes(2)
            ),
            Command(
                "!discord",
                "https://discord.com/invite/SAdBhbu",
                cooldown = Duration.ofSeconds(10)
            )
        )
    )
}