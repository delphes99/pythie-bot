package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelFollow.payload.ChannelFollowCondition
import fr.delphes.twitch.api.user.User
import org.junit.jupiter.api.Test

class NewFollowMapperTest {
    private val newFollowMapper = NewFollowMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.follow.json"
            .shouldBeMappedTo(
                newFollowMapper,
                ChannelFollowCondition::class,
                NewFollow(
                    TwitchChannel("Cooler_User"), User("Cool_User")
                )
            )
    }
}