package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelRaid.payload.ChannelRaidCondition
import fr.delphes.twitch.api.user.User
import org.junit.jupiter.api.Test

internal class IncomingRaidMapperTest {
    private val incomingRaidMapper = IncomingRaidMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.raid.json"
            .shouldBeMappedTo(
                incomingRaidMapper,
                ChannelRaidCondition::class,
                IncomingRaid(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User"),
                    9001
                )
            )
    }
}