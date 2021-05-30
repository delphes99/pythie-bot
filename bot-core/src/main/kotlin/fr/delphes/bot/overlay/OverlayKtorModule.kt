package fr.delphes.bot.overlay

import fr.delphes.bot.Bot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun Application.Overlays(bot: Bot) {
    routing {
        get("/overlays") {
            this.call.respond(HttpStatusCode.OK, bot.overlayRepository.load())
        }
        post("/overlay") {
            val overlay = this.call.receive<Overlay>()
            bot.overlayRepository.add(overlay)

            this.context.respond(HttpStatusCode.OK)
        }
    }
}