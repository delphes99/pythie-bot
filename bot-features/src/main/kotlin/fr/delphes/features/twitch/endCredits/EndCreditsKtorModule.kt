package fr.delphes.features.twitch.endCredits

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.PixelBoundryBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.palette.ColorPalette
import fr.delphes.bot.state.StreamStatistics
import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.api.user.User
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondOutputStream
import io.ktor.routing.get
import io.ktor.routing.routing
import java.awt.Color
import java.awt.Dimension

private val NUMBER_OF_WORDS = 300

fun EndCreditsModule(
    twitchConnector: TwitchConnector
): Application.() -> Unit {
    return {
        routing {
            twitchConnector.configuration?.listenedChannels?.forEach { channelConfiguration ->
                get("/${channelConfiguration.channel.normalizeName}/endcredits") {
                    twitchConnector.whenRunning(
                        whenRunning = {
                            val channel = this.clientBot.channelOf(channelConfiguration.channel)!!
                            channel.streamStatistics?.let { statistics ->
                                this@get.call.respondOutputStream(
                                    contentType = ContentType.parse("image/png"),
                                    status = HttpStatusCode.OK
                                ) {
                                    val wordFrequencies = statistics.wordFrequencies(NUMBER_OF_WORDS)

                                    //TODO retrieve file dimension
                                    val dimension = Dimension(1920, 1080)
                                    val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
                                    wordCloud.setPadding(2)
                                    wordCloud.setBackground(
                                        PixelBoundryBackground(
                                            javaClass.classLoader.getResourceAsStream(
                                                "endcredits/endcredits.png"
                                            )
                                        )
                                    )
                                    wordCloud.setColorPalette(
                                        ColorPalette(
                                            Color(0x4055F1),
                                            Color(0x408DF1)
                                        )
                                    )
                                    wordCloud.setKumoFont(KumoFont("LaserCutRegular", FontWeight.BOLD))
                                    wordCloud.setFontScalar(LinearFontScalar(20, 40))
                                    wordCloud.build(wordFrequencies)
                                    wordCloud.writeToStreamAsPNG(this)
                                }
                            } ?: call.respond(HttpStatusCode.NoContent)
                        },
                        whenNotRunning = {
                            call.respond(HttpStatusCode.NoContent)
                        }
                    )
                }
            }
        }
    }
}

private fun StreamStatistics.wordFrequencies(numberOfWords: Int): List<WordFrequency> {
    if (messages.isEmpty()) {
        return emptyList()
    }

    val occurrences = messages.map(UserMessage::user).groupingBy(User::name).eachCount()

    return occurrences.map { entry ->
        WordFrequency(
            entry.key,
            entry.value
        )
    } + (2..(numberOfWords / occurrences.size)).flatMap {
        occurrences.map { entry ->
            WordFrequency(
                entry.key,
                1
            )
        }
    }
}