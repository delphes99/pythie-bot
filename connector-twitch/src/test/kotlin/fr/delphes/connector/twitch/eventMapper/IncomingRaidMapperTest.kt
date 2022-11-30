package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelRaid.payload.ChannelRaidCondition
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec

class IncomingRaidMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.raid.json"
            .shouldBeMappedTo(
                IncomingRaidMapper(),
                ChannelRaidCondition::class,
                IncomingRaid(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User"),
                    9001
                )
            )
    }
})