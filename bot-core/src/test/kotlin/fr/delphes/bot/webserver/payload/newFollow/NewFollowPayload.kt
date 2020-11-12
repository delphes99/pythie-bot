package fr.delphes.bot.webserver.payload.newFollow

import fr.delphes.bot.util.serialization.Serializer
import org.junit.jupiter.api.Test
import kotlinx.serialization.decodeFromString
import org.assertj.core.api.Assertions.assertThat
import java.time.LocalDateTime

internal class NewFollowPayloadTest {
    @Test
    internal fun deserialize() {
        assertThat(
            Serializer.decodeFromString<NewFollowPayload>(
                """
                    {
                      "data": [
                        {
                          "from_id": "1336",
                          "from_name": "ebi",
                          "to_id": "1337",
                          "to_name": "oliver0823nagy",
                          "followed_at": "2017-08-22T22:55:24Z"
                        }
                      ]
                    }
                """.trimIndent()
            )
        ).isEqualTo(
            NewFollowPayload(
                NewFollowData(
                    "1336",
                    "ebi",
                    "1337",
                    "oliver0823nagy",
                    LocalDateTime.of(2017, 8, 22, 22, 55, 24)
                )
            )
        )
    }
}