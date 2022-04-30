package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.Poll
import fr.delphes.connector.twitch.incomingEvent.PollChoice
import fr.delphes.connector.twitch.incomingEvent.PollUpdated
import fr.delphes.connector.twitch.incomingEvent.PollVote
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollProgressCondition
import org.junit.jupiter.api.Test

internal class ChannelPollProgressMapperTest {
    private val channelPollProgressMapper = ChannelPollProgressMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.poll.progress.json"
            .shouldBeMappedTo(
                channelPollProgressMapper,
                ChannelPollProgressCondition::class,
                PollUpdated(
                    TwitchChannel("Cool_User"),
                    Poll(
                        "1243456",
                        "Aren’t shoes just really hard socks?",
                        listOf(
                            PollChoice("Yeah!"),
                            PollChoice("No!"),
                            PollChoice("Maybe!"),
                        )
                    ),
                    mapOf(
                        PollChoice("Yeah!") to PollVote(14, 7, 5),
                        PollChoice("No!") to PollVote(14, 4, 10),
                        PollChoice("Maybe!") to PollVote(7, 7, 0),
                    )
                )
            )
    }
}