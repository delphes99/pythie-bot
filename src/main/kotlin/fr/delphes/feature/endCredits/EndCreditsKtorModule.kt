package fr.delphes.feature.endCredits

import com.kennycason.kumo.CollisionMode
import com.kennycason.kumo.WordCloud
import com.kennycason.kumo.WordFrequency
import com.kennycason.kumo.bg.PixelBoundryBackground
import com.kennycason.kumo.font.FontWeight
import com.kennycason.kumo.font.KumoFont
import com.kennycason.kumo.font.scale.LinearFontScalar
import com.kennycason.kumo.palette.ColorPalette
import fr.delphes.User
import fr.delphes.bot.Channel
import fr.delphes.bot.state.Statistics
import fr.delphes.bot.state.UserMessage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondOutputStream
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import java.awt.Color
import java.awt.Dimension

private val NUMBER_OF_WORDS = 300

fun EndCreditsModule(
    channel: Channel,
    channelName: String
): Application.() -> Unit {
    return {
        routing {
            get("/$channelName/endcredits") {
                val statistics = channel.statistics

                if (statistics != null) {
                    this.call.respondOutputStream(
                        contentType = ContentType.parse("image/png"),
                        status = HttpStatusCode.OK
                    ) {
                        val wordFrequencies = statistics.wordFrequencies(NUMBER_OF_WORDS)

                        //TODO retrieve file dimension
                        val dimension = Dimension(1920, 1080)
                        val wordCloud = WordCloud(dimension, CollisionMode.PIXEL_PERFECT)
                        wordCloud.setPadding(2)
                        wordCloud.setBackground(PixelBoundryBackground(javaClass.classLoader.getResourceAsStream("endcredits/endcredits.png")))
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
                } else {
                    this.call.respondText(ContentType.parse("text/html"), HttpStatusCode.OK) { "Offline" }
                }
            }
        }
    }
}

private fun Statistics.wordFrequencies(numberOfWords: Int): List<WordFrequency> {
    if(messages.isEmpty()) {
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