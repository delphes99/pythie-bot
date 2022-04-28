package fr.delphes.features.twitch.statistics

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.TwitchChannel
import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun StatisticsModule(
    channel: TwitchChannel,
    connector: TwitchConnector
): Application.() -> Unit {
    return {
        routing {
            get("/${channel.normalizeName}/stats") {
                val stats = connector.statistics.of(channel)
                val statistics = stats.statistics
                val streamStatistics = stats.streamStatistics
                call.respondText(
                    """
                        <h2>Current stream stats</h2>
                        <table>
                            <tr><td>Number of chatters</td><td>${streamStatistics?.numberOfChatters ?: ""}</td></tr>
                            <tr><td>Number of messages</td><td>${streamStatistics?.numberMessages ?: ""}</td></tr>
                            <tr><td>Number of follow</td><td>${streamStatistics?.numberOfFollow ?: ""}</td></tr>
                            <tr><td>Number of sub</td><td>${streamStatistics?.numberOfSub ?: ""}</td></tr>
                        </table>
                        <h2>Global stats</h2>
                        <table>
                            <tr><td>Number of chatters</td><td>${statistics.numberOfChatters}</td></tr>
                            <tr><td>Number of messages</td><td>${statistics.numberMessages}</td></tr>
                            <tr><td>Number of follow</td><td>${statistics.numberOfFollow}</td></tr>
                            <tr><td>Number of sub</td><td>${statistics.numberOfSub}</td></tr>
                        </table>
                    """.trimIndent(),
                    ContentType.parse("text/html")
                )
            }
        }
    }
}