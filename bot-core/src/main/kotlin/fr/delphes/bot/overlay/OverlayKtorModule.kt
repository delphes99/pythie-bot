package fr.delphes.bot.overlay

import fr.delphes.bot.Bot
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.delete
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing

fun Application.Overlays(bot: Bot) {
    val repository = bot.overlayRepository
    routing {
        get("/overlays") {
            this.call.respond(HttpStatusCode.OK, repository.load())
        }
        post("/overlay") {
            repository.upsert(call.receive())

            this.context.respond(HttpStatusCode.OK)
        }
        delete("/overlay/{id}") {
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