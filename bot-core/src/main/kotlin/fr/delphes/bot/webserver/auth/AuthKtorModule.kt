package fr.delphes.bot.webserver.auth

import fr.delphes.bot.ClientBot
import fr.delphes.twitch.auth.AuthToken
import fr.delphes.bot.util.http.httpClient
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.call.receive
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.takeFrom
import io.ktor.request.receiveText
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing

//TODO move to twitch connector
fun Application.AuthModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            get("/${channel.name}/registerToken") {
                println(this.call.receiveText())
                this.context.response.status(HttpStatusCode.OK)
            }
        }
    }
}

fun Application.AuthInternalModule(bot: ClientBot) {
    routing {
        bot.channels.forEach { channel ->
            get("/${channel.name}/requestToken") {
                call.respondRedirect(permanent = false) {
                    takeFrom("https://id.twitch.tv/oauth2/authorize")
                    parameters.append("response_type", "code")
                    parameters.append("client_id", bot.appCredential.clientId)
                    parameters.append("redirect_uri", "http://localhost:8080/authFlow/")
                    parameters.append("scope", "bits:read channel:read:hype_train channel:read:subscriptions chat:read channel:moderate channel:read:redemptions channel:manage:redemptions")
                    parameters.append("state", channel.name)
                }
            }
        }
        get("/authFlow") {
            val code = this.call.parameters["code"]
            val channelName = this.call.parameters["state"]

            val channel = channelName?.let { bot.findChannelBy(it) }
            if(channel != null) {
                val auth = (httpClient.post<HttpResponse>("https://id.twitch.tv/oauth2/token") {
                    this.parameter("code", code)
                    this.parameter("client_id", bot.appCredential.clientId)
                    this.parameter("client_secret", bot.appCredential.clientSecret)
                    this.parameter("grant_type", "authorization_code")
                    this.parameter("redirect_uri", "http://localhost:8080/authFlow/")
                }.receive<AuthToken>())

                channel.newAuth(auth)
            }

            this.context.response.status(HttpStatusCode.OK)
        }
    }
}