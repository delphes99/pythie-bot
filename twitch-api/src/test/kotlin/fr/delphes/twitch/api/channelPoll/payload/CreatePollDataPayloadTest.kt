package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.utils.serialization.Serializer
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import org.junit.jupiter.api.Test

internal class CreatePollDataPayloadTest {
    @Test
    internal fun deserialize() {
        Serializer.decodeFromString<CreatePollDataPayload>(
            """
            {
              "id": "ed961efd-8a3f-4cf5-a9d0-e616c590cd2a",
              "broadcaster_id": "141981764",
              "broadcaster_name": "TwitchDev",
              "broadcaster_login": "twitchdev",
              "title": "Heads or Tails?",
              "choices": [
                {
                  "id": "4c123012-1351-4f33-84b7-43856e7a0f47",
                  "title": "Heads",
                  "votes": 0,
                  "channel_points_votes": 0,
                  "bits_votes": 0
                },
                {
                  "id": "279087e3-54a7-467e-bcd0-c1393fcea4f0",
                  "title": "Tails",
                  "votes": 0,
                  "channel_points_votes": 0,
                  "bits_votes": 0
                }
              ],
              "bits_voting_enabled": false,
              "bits_per_vote": 0,
              "channel_points_voting_enabled": true,
              "channel_points_per_vote": 100,
              "status": "ACTIVE",
              "duration": 1800,
              "started_at": "2021-03-19T06:08:33.871278372Z"
            }
        """
        ).shouldBe(
            CreatePollDataPayload(
                id = "ed961efd-8a3f-4cf5-a9d0-e616c590cd2a"
            )
        )
    }
}