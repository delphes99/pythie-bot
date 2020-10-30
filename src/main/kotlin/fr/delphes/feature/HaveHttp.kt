package fr.delphes.feature

import fr.delphes.bot.Channel
import io.ktor.application.Application

interface HaveHttp {
    //TODO remove Ktor dependency
    val module: (Channel) -> (Application.() -> Unit)
}