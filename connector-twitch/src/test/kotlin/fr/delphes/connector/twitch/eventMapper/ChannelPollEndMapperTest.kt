package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.Poll
import fr.delphes.connector.twitch.incomingEvent.PollChoice
import fr.delphes.connector.twitch.incomingEvent.PollClosed
import fr.delphes.connector.twitch.incomingEvent.PollVote
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollEndCondition
import io.kotest.core.spec.style.ShouldSpec

class ChannelPollEndMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.poll.end.json"
            .shouldBeMappedTo(
                ChannelPollEndMapper(),
                ChannelPollEndCondition::class,
                PollClosed(
                    TwitchChannel("Cool_User"),
                    Poll(
                        "1243456",
                        "Aren’t shoes just really hard socks?",
                        listOf(
                            PollChoice("Blue"),
                            PollChoice("Yellow"),
                            PollChoice("Green"),
                        )
                    ),
                    mapOf(
                        PollChoice("Blue") to PollVote(150, 70, 50),
                        PollChoice("Yellow") to PollVote(140, 40, 100),
                        PollChoice("Green") to PollVote(80, 70, 10),
                    )
                )
            )
    }
})