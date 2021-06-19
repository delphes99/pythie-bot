package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewPoll
import fr.delphes.connector.twitch.incomingEvent.Poll
import fr.delphes.connector.twitch.incomingEvent.PollChoice
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollBeginCondition
import org.junit.jupiter.api.Test

internal class ChannelPollBeginMapperTest {
    private val channelPollBeginMapper = ChannelPollBeginMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.poll.begin.json"
            .shouldBeMappedTo(
                channelPollBeginMapper,
                ChannelPollBeginCondition::class,
                NewPoll(
                    TwitchChannel("Cool_User"),
                    Poll(
                        "1243456",
                        "Aren’t shoes just really hard socks?",
                        listOf(
                            PollChoice("Yeah!"),
                            PollChoice("No!"),
                            PollChoice("Maybe!"),
                        )
                    )
                )
            )
    }
}