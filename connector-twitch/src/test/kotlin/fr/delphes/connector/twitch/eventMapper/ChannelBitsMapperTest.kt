package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerCondition
import fr.delphes.twitch.api.user.User
import org.junit.jupiter.api.Test

internal class ChannelBitsMapperTest {
    private val channelBitsMapper = ChannelBitsMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.cheer.json"
            .shouldBeMappedTo(
                channelBitsMapper,
                ChannelCheerCondition::class,
                BitCheered(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User"),
                    1000,
                    "pogchamp"
                )
            )
    }
}