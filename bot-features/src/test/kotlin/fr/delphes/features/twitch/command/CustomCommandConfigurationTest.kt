package fr.delphes.features.twitch.command

import fr.delphes.connector.twitch.command.Command
import fr.delphes.rework.feature.FeatureId
import fr.delphes.twitch.TwitchChannel
import fr.delphes.utils.serialization.Serializer
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.time.Duration

class CustomCommandConfigurationTest : ShouldSpec({
    should("create feature") {
        val feature = Serializer.decodeFromString<CustomCommandConfiguration>(
            """
                {
                    "channel": "test",
                    "command": "!cmd",
                    "id": "some-id",
                    "cooldown": "PT30S"
                }
            """
        ).buildFeature()

        with(feature) {
            channel shouldBe TwitchChannel(name = "test")
            triggerCommand shouldBe Command("!cmd")
            id shouldBe FeatureId("some-id")
            cooldown shouldBe Duration.ofSeconds(30)
        }
    }

    should("deserialize") {
        val configuration = CustomCommandConfiguration(
            channel = TwitchChannel(name = "test"),
            command = "!cmd",
            id = FeatureId("some-id"),
            cooldown = Duration.ofSeconds(60)
        )

        Serializer.encodeToString(configuration) shouldEqualJson """
             {
                "channel": "test",
                "command": "!cmd",
                "id": "some-id",
                "cooldown": "PT1M"
            }
        """
    }
})