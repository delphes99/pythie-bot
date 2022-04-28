package fr.delphes.feature

import fr.delphes.bot.Bot
import io.ktor.server.application.Application

interface HaveHttp {
    //TODO remove Ktor dependency
    val module: (Bot) -> (Application.() -> Unit)}