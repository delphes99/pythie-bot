package fr.delphes.features.twitch.rewardRedeem

import fr.delphes.features.twitch.TwitchFeatureDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("twitch-reward-redeem")
class RewardRedeemDescription(
    override val channel: String
) : TwitchFeatureDescription {
    override val editable = false
    override val id: String = UUID.randomUUID().toString()
}