package fr.delphes.bot.overlay

import fr.delphes.bot.Bot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun Application.Overlays(bot: Bot) {
    val repository = bot.overlayRepository
    routing {
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