package fr.delphes.feature.statistics

import fr.delphes.bot.Channel
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun StatisticsModule(
    channel: Channel,
    channelName: String
) : Application.() -> Unit {
    return {
        routing {
            get("/$channelName/stats") {
                val statistics = channel.statistics
                this.call.respondText(
                    "<table>" +
                            "<tr><td>Number of chatters</td><td>${statistics?.numberOfChatters ?: ""}</td></tr>" +
                            "<tr><td>Number of messages</td><td>${statistics?.numberMessages ?: ""}</td></tr>" +
                            "<tr><td>Number of follow</td><td>${statistics?.numberOfFollow ?: ""}</td></tr>" +
                            "<tr><td>Number of sub</td><td>${statistics?.numberOfSub ?: ""}</td></tr>" +
                            "</table>",
                    ContentType.parse("text/html")
                )
            }
        }
    }
}