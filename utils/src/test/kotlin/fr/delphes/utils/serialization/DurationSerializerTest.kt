package fr.delphes.utils.serialization

import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import java.time.Duration
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.assertj.core.api.Assertions.assertThat

internal class DurationSerializerTest {
    @Serializable
    data class Wrapper(
        @Serializable(with = DurationSerializer::class)
        val duration: Duration
    )

    @Test
    internal fun deserialize() {
        val wrapper = Serializer.decodeFromString<Wrapper>("{\"duration\":\"PT1S\"}")

        assertThat(wrapper).isEqualTo(Wrapper(Duration.ofSeconds(1)))
    }

    @Test
    internal fun serialize() {
        val json = Serializer.encodeToString(Wrapper(Duration.ofSeconds(1)))

        assertThat(json).isEqualTo("{\"duration\":\"PT1S\"}")
    }
}