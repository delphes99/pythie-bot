package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.TwitchChannel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

internal fun Application.RewardKtorModule(connector: TwitchConnector) {
    routing {
        get("/twitch/{channel}/rewards") {
            val channelName = this.context.parameters["channel"]
            val configuration = connector.channels.firstOrNull { it.channel == channelName?.let(::TwitchChannel) }

            if (configuration != null) {
                call.respond(configuration.rewards)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}