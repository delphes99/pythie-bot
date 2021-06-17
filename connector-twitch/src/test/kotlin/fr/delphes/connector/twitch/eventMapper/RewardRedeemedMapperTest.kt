package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionCondition
import fr.delphes.twitch.api.reward.RewardId
import fr.delphes.twitch.api.user.User
import org.junit.jupiter.api.Test

internal class RewardRedeemedMapperTest {
    private val rewardRedeemedMapper = RewardRedeemedMapper()

    @Test
    internal fun `map to incomingEvent`() {
        "/eventsub/payload/channel.points.custom.reward.redemption.add.json"
            .shouldBeMappedTo(
                rewardRedeemedMapper,
                ChannelPointsCustomRewardRedemptionCondition::class,
                RewardRedemption(
                    channel = TwitchChannel("Cool_User"),
                    reward = RewardId(
                        id = "9001",
                        name = "title"
                    ),
                    user = User("Cooler_User"),
                    cost = 100,
                )
            )
    }
}