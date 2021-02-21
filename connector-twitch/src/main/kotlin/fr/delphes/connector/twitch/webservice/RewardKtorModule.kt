package fr.delphes.connector.twitch.webservice

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.TwitchChannel
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

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