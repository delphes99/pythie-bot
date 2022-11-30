package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowCondition
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec

class NewFollowMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.follow.json"
            .shouldBeMappedTo(
                NewFollowMapper(),
                ChannelFollowCondition::class,
                NewFollow(
                    TwitchChannel("Cooler_User"), User("Cool_User")
                )
            )
    }
})
