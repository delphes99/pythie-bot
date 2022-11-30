package fr.delphes.utils.serialization

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.time.LocalDateTime

class LocalDateTimeSerializerTest : ShouldSpec({
    context("standard serializer") {
        should("deserialize") {
            val wrapper = Serializer.decodeFromString<WrapperStandard>(
                """
               { "date" : "2020-06-07T11:30:00" } 
            """
            )

            wrapper shouldBe WrapperStandard(LocalDateTime.of(2020, 6, 7, 11, 30))
        }

        should("serialize") {
            val json = Serializer.encodeToString(WrapperStandard(LocalDateTime.of(2020, 6, 7, 11, 30)))

            json shouldEqualJson """
                { "date" : "2020-06-07T11:30:00" }
            """
        }
    }

    context("instant serializer") {
        should("deserialize") {
            val wrapper = Serializer.decodeFromString<WrapperInstant>(
                """
                { "date" : "2020-06-07T11:30:00" }
                """
            )

            wrapper shouldBe WrapperInstant(LocalDateTime.of(2020, 6, 7, 11, 30))
        }

        should("serialize") {
            val json = Serializer.encodeToString(WrapperInstant(LocalDateTime.of(2020, 6, 7, 11, 30)))

            json shouldEqualJson """
                { "date" : "2020-06-07T11:30:00" } 
                """
        }
    }
}) {
    companion object {
        @Serializable
        private data class WrapperStandard(
            @Serializable(with = LocalDateTimeSerializer::class)
            val date: LocalDateTime
        )

        @Serializable
        private data class WrapperInstant(
            @Serializable(with = LocalDateTimeSerializer::class)
            val date: LocalDateTime
        )
    }
}