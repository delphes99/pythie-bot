package fr.delphes.feature.statistics

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing

fun StatisticsModule(
    feature: Statistics,
    channelName: String
) : Application.() -> Unit {
    return {
        routing {
            get("/$channelName/stats") {
                val state = feature.state

                this.call.respondText(
                    "<table>" +
                            "<tr><td>Number of chatters</td><td>${state.numberOfChatters}</td></tr>" +
                            "<tr><td>Number of messages</td><td>${state.numberMessages}</td></tr>" +
                            "<tr><td>Number of follow</td><td>${state.numberOfFollow}</td></tr>" +
                            "<tr><td>Number of sub</td><td>${state.numberOfSub}</td></tr>" +
                            "</table>",
                    ContentType.parse("text/html")
                )
            }
        }
    }
}