package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscribeCondition
import fr.delphes.twitch.api.user.User
import org.junit.jupiter.api.Test

internal class NewSubMapperTest {
    private val newSubMapper = NewSubMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.subscribe.json"
            .shouldBeMappedTo(
                newSubMapper,
                ChannelSubscribeCondition::class,
                NewSub(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User")
                )
            )
    }
}