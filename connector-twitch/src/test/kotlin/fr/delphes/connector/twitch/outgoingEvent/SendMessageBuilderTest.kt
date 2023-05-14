package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.connector.twitch.twitchTestSerializer
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.twitch.TwitchChannel
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class SendMessageBuilderTest : ShouldSpec({
    should("serialize") {
        val builder = SendMessage.Builder("Hello", TwitchChannel("channel"))

        val json = twitchTestSerializer.encodeToString<OutgoingEventBuilder>(builder)

        json shouldEqualJson """
            {
              "type": "twitch-send-message",
              "text": "Hello",
              "channel": "channel"
            }
            """
    }

    should("deserialize") {
        val json = """
            {
              "type": "twitch-send-message",
              "text": "Hello",
              "channel": "channel"
            }
            """

        val builder = twitchTestSerializer.decodeFromString<OutgoingEventBuilder>(json)

        with(builder) {
            this.shouldBeInstanceOf<SendMessage.Builder>()
            text shouldBe "Hello"
            channel shouldBe TwitchChannel("channel")
        }
    }

    should("description") {
        val builder = SendMessage.Builder("Hello", TwitchChannel("channelName"))

        val description = builder.description()

        description shouldBe OutgoingEventBuilderDescription(
            "twitch-send-message",
            StringFeatureDescriptor(
                "text",
                "description of text",
                "Hello"
            ),
            StringFeatureDescriptor(
                "channel",
                "description of channel",
                "channelName"
            )
        )
    }
})