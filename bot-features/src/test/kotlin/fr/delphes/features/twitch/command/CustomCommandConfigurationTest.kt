package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.Command
import fr.delphes.feature.FeatureConfiguration
import fr.delphes.rework.feature.FeatureId
import fr.delphes.testSerializer
import fr.delphes.twitch.TwitchChannel
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.time.Duration

class CustomCommandConfigurationTest : ShouldSpec({
    should("create feature") {
        val feature = testSerializer.decodeFromString<FeatureConfiguration>(
            """
                    {
                        "type":"TwitchCustomCommandConfiguration",
                        "channel": "test",
                        "command": "!cmd",
                        "id": "some-id",
                        "cooldown": "PT30S"
                    }
                """
        ).buildFeature()

        feature.shouldBeInstanceOf<CustomCommand>()

        with(feature) {
            channel shouldBe TwitchChannel(name = "test")
            triggerCommand shouldBe Command("!cmd")
            id shouldBe FeatureId("some-id")
            cooldown shouldBe Duration.ofSeconds(30)
        }
    }

    should("serialize") {
        val configuration = CustomCommandConfiguration(
            channel = TwitchChannel(name = "test"),
            command = "!cmd",
            id = FeatureId("some-id"),
            cooldown = Duration.ofSeconds(60)
        )

        testSerializer.encodeToString(configuration) shouldEqualJson """
             {
                "channel": "test",
                "command": "!cmd",
                "id": "some-id",
                "cooldown": "PT1M"
            }
        """
    }

    should("description") {
        val configuration = CustomCommandConfiguration(
            channel = TwitchChannel(name = "test"),
            command = "!cmd",
            id = FeatureId("some-id"),
            cooldown = Duration.ofSeconds(60)
        )

        testSerializer.encodeToString(configuration.description()) shouldEqualJson """
            {
                "type": "TwitchCustomCommandConfiguration",
                "descriptors": [
                    {
                        "fieldName": "channel",
                        "type": "STRING",
                        "description": "Channel name",
                        "value": "test"
                    },
                    {
                        "fieldName": "command",
                        "type": "STRING",
                        "description": "command",
                        "value": "!cmd"
                    },
                    {
                        "fieldName": "cooldown",
                        "type": "STRING",
                        "description": "cooldown",
                        "value": "PT1M"
                    }
                ]
            }
        """
    }
})