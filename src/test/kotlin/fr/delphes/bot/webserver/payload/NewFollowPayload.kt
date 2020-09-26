package fr.delphes.bot.webserver.payload

import fr.delphes.storage.serialization.Serializer
import org.junit.jupiter.api.Test
import kotlinx.serialization.decodeFromString
import org.assertj.core.api.Assertions.assertThat

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
                listOf(
                    NewFollowPayloadData(
                        "1336",
                        "ebi",
                        "1337",
                        "oliver0823nagy",
                        "2017-08-22T22:55:24Z"
                    )
                )
            )
        )
    }
}