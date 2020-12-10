package fr.delphes.twitch.api.channelPointsCustomRewardRedemption

import fr.delphes.twitch.api.parseToModel
import fr.delphes.twitch.api.reward.Reward
import fr.delphes.twitch.api.reward.RewardConfiguration
import fr.delphes.twitch.api.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class CustomRewardRedemptionEventSubConfigurationTest {
    val configured_reward = RewardConfiguration("reward_title", 100)

    @Test
    fun `transform RewardRedemption`() {
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "channel.channel_points_custom_reward_redemption.add",
                    "version": "1",
                    "condition": {
                        "broadcaster_user_id": "1337"
                    },
                     "transport": {
                        "method": "webhook",
                        "callback": "https://example.com/webhooks/callback"
                    },
                    "created_at": "2019-11-16T10:11:12.123Z"
                },
                "event": {
                    "id": "1234",
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cool_user",
                    "user_id": "9001",
                    "user_name": "cooler_user",
                    "user_input": "pogchamp",
                    "status": "unfulfilled",
                    "reward": {
                        "id": "9001",
                        "title": "reward_title",
                        "cost": 100,
                        "prompt": "reward prompt"
                    },
                    "redeemed_at": "2020-07-15T17:16:03.17106713Z"
                }
            }
        """.trimIndent()

        val model = CustomRewardRedemptionEventSubConfiguration({ }, listOf(configured_reward)).parseToModel(payload)

        assertThat(model).isEqualTo(
            RewardRedemption(
                Reward(
                    "9001",
                    configured_reward
                ),
                User("cooler_user"),
                100,
                "1234"
            )
        )
    }


    @Test
    fun `don't transform RewardRedemption if not configured reward`() {
        val reward = RewardConfiguration("reward_title", 100)
        val payload = """
            {
                "subscription": {
                    "id": "f1c2a387-161a-49f9-a165-0f21d7a4e1c4",
                    "status": "enabled",
                    "type": "channel.channel_points_custom_reward_redemption.add",
                    "version": "1",
                    "condition": {
                        "broadcaster_user_id": "1337"
                    },
                     "transport": {
                        "method": "webhook",
                        "callback": "https://example.com/webhooks/callback"
                    },
                    "created_at": "2019-11-16T10:11:12.123Z"
                },
                "event": {
                    "id": "1234",
                    "broadcaster_user_id": "1337",
                    "broadcaster_user_name": "cool_user",
                    "user_id": "9001",
                    "user_name": "cooler_user",
                    "user_input": "pogchamp",
                    "status": "unfulfilled",
                    "reward": {
                        "id": "9001",
                        "title": "unconfigured_reward",
                        "cost": 100,
                        "prompt": "reward prompt"
                    },
                    "redeemed_at": "2020-07-15T17:16:03.17106713Z"
                }
            }
        """.trimIndent()

        val model = CustomRewardRedemptionEventSubConfiguration({ }, listOf(configured_reward)).parseToModel(payload)

        assertThat(model).isNull()
    }
}