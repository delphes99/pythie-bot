package fr.delphes.utils.serialization

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.time.Duration

class DurationSerializerTest : ShouldSpec({
    should("deserialize") {
        val wrapper = Serializer.decodeFromString<Wrapper>(
            """
            { "duration": "PT1S" }
            """
        )

        wrapper shouldBe Wrapper(Duration.ofSeconds(1))
    }

    should("serialize") {
        val json = Serializer.encodeToString(Wrapper(Duration.ofSeconds(1)))

        json shouldEqualJson """
            { "duration":"PT1S" }
            """
    }
}) {
    companion object {
        @Serializable
        private data class Wrapper(
            @Serializable(with = DurationSerializer::class)
            val duration: Duration
        )
    }
}