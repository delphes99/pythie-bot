package fr.delphes.feature

import fr.delphes.bot.Channel
import io.ktor.application.Application

interface HaveAdmin {
    //TODO remove Ktor dependency
    val module: (Channel) -> (Application.() -> Unit)
}