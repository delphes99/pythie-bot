package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.connector.twitch.twitchTestSerializer
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

        val json = twitchTestSerializer.encodeToString(builder.description())

        json shouldEqualJson """
            {
              "type": "twitch-send-message",
              "descriptors": [
                        {
                            "type": "STRING",
                            "fieldName": "text",
                            "description": "description of text",
                            "value": "Hello"
                        },
                        {
                            "type": "STRING",
                            "fieldName": "channel",
                            "description": "description of channel",
                            "value": "channelName"
                        }
                    ]
            }
            """
    }
})