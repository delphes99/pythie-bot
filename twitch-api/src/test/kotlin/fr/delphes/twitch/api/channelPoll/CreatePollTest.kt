package fr.delphes.twitch.api.channelPoll

import fr.delphes.twitch.api.channelPoll.payload.CreatePollQuery
import fr.delphes.utils.serialization.Serializer
import io.kotest.assertions.json.shouldEqualJson
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test
import java.time.Duration

internal class CreatePollTest {
    @Test
    internal fun serialize() {
        val createPoll = CreatePoll(
            title = "Heads or Tails?",
            choices = listOf(
                PollChoice("Heads"),
                PollChoice("Tails")
            ),
            channelPointVote = ChannelPointVote(cost = 100),
            duration = Duration.ofSeconds(1800)
        )

        Serializer.encodeToString(
            CreatePollQuery.of(createPoll, "141981764")
        ).shouldEqualJson(
            """
                {
                  "broadcaster_id":"141981764",
                  "title":"Heads or Tails?",
                  "choices":[{
                    "title":"Heads"
                  },
                  {
                    "title":"Tails"
                  }],
                  "channel_points_voting_enabled":true,
                  "channel_points_per_vote":100,
                  "duration":1800
                }
            """
        )
    }
}