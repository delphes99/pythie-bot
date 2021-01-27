package fr.delphes.connector.twitch.webservice

import fr.delphes.configuration.ChannelConfiguration
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveText
import io.ktor.routing.get
import io.ktor.routing.routing

fun Application.AuthModule(channels: List<ChannelConfiguration>) {
    routing {
        channels.forEach { channel ->
            get("/${channel.ownerChannel}/registerToken") {
                println(this.call.receiveText())
                this.context.response.status(HttpStatusCode.OK)
            }
        }
    }
}