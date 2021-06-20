package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

internal fun Application.StateTwitchKtorModule(connector: TwitchConnector) {
    routing {
        get("/twitch/state") {
            call.respond(connector.state.currentState)
        }
    }
}