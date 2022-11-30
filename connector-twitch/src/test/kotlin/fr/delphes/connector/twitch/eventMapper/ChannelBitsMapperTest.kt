package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelCheer.payload.ChannelCheerCondition
import fr.delphes.twitch.api.user.User
import io.kotest.core.spec.style.ShouldSpec

class ChannelBitsMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.cheer.json"
            .shouldBeMappedTo(
                ChannelBitsMapper(),
                ChannelCheerCondition::class,
                BitCheered(
                    TwitchChannel("Cooler_User"),
                    User("Cool_User"),
                    1000,
                    "pogchamp"
                )
            )
    }
})