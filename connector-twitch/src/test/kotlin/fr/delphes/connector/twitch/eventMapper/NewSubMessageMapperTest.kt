package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelSubscribe.payload.ChannelSubscriptionMessageCondition
import fr.delphes.twitch.api.user.User
import org.junit.jupiter.api.Test

internal class NewSubMessageMapperTest {
    private val newSubMessageMapper = NewSubMessageMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.subscription.message.json"
            .shouldBeMappedTo(
                newSubMessageMapper,
                ChannelSubscriptionMessageCondition::class,
                NewSub(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User")
                )
            )
    }
}