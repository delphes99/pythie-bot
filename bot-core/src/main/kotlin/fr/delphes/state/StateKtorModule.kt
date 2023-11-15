package fr.delphes.state

import fr.delphes.bot.Bot
import fr.delphes.state.enumeration.EnumerationState
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.StateModule(bot: Bot) {
    routing {
        get("states/enumerations/{id}") {
            val id = this.context.parameters["id"] ?: throw IllegalArgumentException("Id is mandatory")
            val state = bot.stateManager.getState(StateIdQualifier(id))
            if (state is EnumerationState) {
                this.context.respond(HttpStatusCode.OK, state.getItems())
            } else {
                this.context.respond(HttpStatusCode.BadRequest, "State is not an enumeration")
            }
        }
    }
}