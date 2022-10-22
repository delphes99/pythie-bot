package fr.delphes.bot.overlay

import fr.delphes.bot.Bot
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.Overlays(bot: Bot) {
    val repository = bot.overlayRepository
    routing {
        static("/overlay-legacy") {
            resources("overlay-legacy")
        }
        static("/overlay") {
            resources("overlay")
        }
        get("/api/overlays/{id}") {
            val id = this.call.parameters["id"]
            val overlay = repository.load().firstOrNull() { it.id == id }

            if(overlay == null) {
                this.context.respond(HttpStatusCode.BadRequest, "unknown overlay id")
            } else {
                this.context.respond(HttpStatusCode.OK, overlay)
            }
        }
        get("/api/overlays") {
            this.call.respond(HttpStatusCode.OK, repository.load())
        }
        post("/api/overlay") {
            repository.upsert(call.receive())

            this.context.respond(HttpStatusCode.OK)
        }
        delete("/api/overlay/{id}") {
            val id = this.call.parameters["id"]
            val oldList = repository.load()
            val newList = oldList.filter { it.id != id }

            if(oldList == newList) {
                this.context.respond(HttpStatusCode.BadRequest, "unknown overlay id")
                return@delete
            }

            repository.save(newList)

            this.context.respond(HttpStatusCode.OK)
        }
    }
}