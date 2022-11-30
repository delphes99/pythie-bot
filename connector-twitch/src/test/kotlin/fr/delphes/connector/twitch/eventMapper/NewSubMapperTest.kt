package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeCondition
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec

class NewSubMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.subscribe.json"
            .shouldBeMappedTo(
                NewSubMapper(),
                ChannelSubscribeCondition::class,
                NewSub(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User")
                )
            )
    }
})