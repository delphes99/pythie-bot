package fr.delphes.utils.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class LocalDateTimeSerializerTest {
    @Serializable
    data class WrapperStandard(
        @Serializable(with = LocalDateTimeSerializer::class)
        val date: LocalDateTime
    )

    @Nested
    @DisplayName("RewardRedemption")
    inner class Standard {
        @Test
        internal fun deserialize() {
            val wrapper = Serializer.decodeFromString<WrapperStandard>("{\"date\":\"2020-06-07T11:30:00\"}")

            Assertions.assertThat(wrapper).isEqualTo(WrapperStandard(LocalDateTime.of(2020, 6, 7, 11, 30)))
        }

        @Test
        internal fun serialize() {
            val json = Serializer.encodeToString(WrapperStandard(LocalDateTime.of(2020, 6, 7, 11, 30)))

            Assertions.assertThat(json).isEqualTo("{\"date\":\"2020-06-07T11:30:00\"}")
        }
    }

    @Serializable
    data class WrapperInstant(
        @Serializable(with = LocalDateTimeSerializer::class)
        val date: LocalDateTime
    )

    @Nested
    @DisplayName("RewardRedemption")
    inner class Instant {
        @Test
        internal fun deserialize() {
            val wrapper = Serializer.decodeFromString<WrapperInstant>("{\"date\":\"2020-06-07T11:30:00\"}")

            Assertions.assertThat(wrapper).isEqualTo(WrapperInstant(LocalDateTime.of(2020, 6, 7, 11, 30)))
        }

        @Test
        internal fun serialize() {
            val json = Serializer.encodeToString(WrapperInstant(LocalDateTime.of(2020, 6, 7, 11, 30)))

            Assertions.assertThat(json).isEqualTo("{\"date\":\"2020-06-07T11:30:00\"}")
        }
    }
}