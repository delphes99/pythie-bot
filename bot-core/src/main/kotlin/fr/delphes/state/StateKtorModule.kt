package fr.delphes.state

import fr.delphes.bot.Bot
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.StateModule(bot: Bot) {
    routing {
        get("states/enumerations/{id}") {
            val id = this.context.parameters["id"]?.let { StateIdQualifier(it) }
                ?: throw IllegalArgumentException("Id is mandatory")
            val state = bot.enumerationStates.firstOrNull { it.id.qualifier == id }
                ?: throw IllegalArgumentException("State is not an enumeration")

            this.context.respond(HttpStatusCode.OK, state.getItems())
        }
    }
}