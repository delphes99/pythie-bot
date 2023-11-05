package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.reward.RewardId
import fr.delphes.connector.twitch.reward.RewardTitle
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPointsCustomRewardRedemption.payload.ChannelPointsCustomRewardRedemptionCondition
import fr.delphes.twitch.api.user.UserName
import io.kotest.core.spec.style.ShouldSpec

class RewardRedeemedMapperTest : ShouldSpec({
    should("map to incomingEvent") {
        "/eventsub/payload/channel.points.custom.reward.redemption.add.json"
            .shouldBeMappedTo(
                RewardRedeemedMapper(),
                ChannelPointsCustomRewardRedemptionCondition::class,
                RewardRedemption(
                    rewardId = RewardId(
                        channel = TwitchChannel("Cool_User"),
                        title = RewardTitle("title"),
                    ),
                    user = UserName("Cooler_User"),
                    cost = 100,
                )
            )
    }
})