package fr.delphes

import fr.delphes.bot.Bot
import fr.delphes.configuration.PropertiesConfiguration
import fr.delphes.feature.DisplayChat
import fr.delphes.feature.VOTH

fun main(args: Array<String>) {
    Bot.build(
        PropertiesConfiguration(),
        listOf(
            VOTH(
                "27e063b4-b6f8-44c0-8d1b-039745a4772a"
            ) { e -> "${e.displayName} est le nouveau VIP Of The Hill" },
            DisplayChat()
        )
    )
}