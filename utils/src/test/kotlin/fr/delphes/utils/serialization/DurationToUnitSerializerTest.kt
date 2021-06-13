package fr.delphes.utils.serialization

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import java.time.Duration

internal class DurationToUnitSerializerTest {
    @Serializable
    data class Wrapper(
        @Serializable(with = DurationToUnitSerializer::class)
        val duration: Duration
    )

    @Test
    internal fun deserialize() {
        val wrapper = Serializer.decodeFromString<Wrapper>("""
            {"duration": 1}
        """)

        wrapper shouldBe Wrapper(Duration.ofSeconds(1))
    }

    @Test
    internal fun serialize() {
        val json = Serializer.encodeToString(Wrapper(Duration.ofSeconds(1)))

        json shouldEqualJson """
            {"duration": 1}
        """
    }
}