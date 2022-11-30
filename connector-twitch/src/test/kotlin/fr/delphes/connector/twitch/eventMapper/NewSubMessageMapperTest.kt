package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscriptionMessageCondition
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec

class NewSubMessageMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.subscription.message.json"
            .shouldBeMappedTo(
                NewSubMessageMapper(),
                ChannelSubscriptionMessageCondition::class,
                NewSub(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User")
                )
            )
    }
})