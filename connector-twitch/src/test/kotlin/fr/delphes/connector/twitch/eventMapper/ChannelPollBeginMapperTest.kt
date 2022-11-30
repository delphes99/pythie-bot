package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewPoll
import fr.delphes.connector.twitch.incomingEvent.Poll
import fr.delphes.connector.twitch.incomingEvent.PollChoice
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollBeginCondition
import io.kotest.core.spec.style.ShouldSpec

class ChannelPollBeginMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.poll.begin.json"
            .shouldBeMappedTo(
                ChannelPollBeginMapper(),
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
})