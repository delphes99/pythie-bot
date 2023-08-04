package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import fr.delphes.connector.twitch.state.ChannelRewardsState
import fr.delphes.connector.twitch.twitchTestSerializer
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.state.StateManager
import fr.delphes.twitch.TwitchChannel
import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class DeactivateRewardBuilderTest : ShouldSpec({
    should("serialize") {
        val builder = DeactivateReward.Companion.Builder("someReward", TwitchChannel("channel"))

        val json = twitchTestSerializer.encodeToString<LegacyOutgoingEventBuilder>(builder)

        json shouldEqualJson """
            {
              "type": "twitch-deactivate-reward",
              "rewardName": "someReward",
              "channel": "channel"
            }
            """
    }

    should("deserialize") {
        val json = """
            {
              "type": "twitch-deactivate-reward",
              "rewardName": "someReward",
              "channel": "channel"
            }
            """

        val builder = twitchTestSerializer.decodeFromString<DeactivateReward.Companion.Builder>(json)

        with(builder) {
            this.shouldBeInstanceOf<DeactivateReward.Companion.Builder>()
            rewardName shouldBe "someReward"
            channel shouldBe TwitchChannel("channel")
        }
    }

    should("build DeactivateReward") {
        val channel = TwitchChannel("channel")
        val channelRewardsState = mockk<ChannelRewardsState> {
            every { id } returns ChannelRewardsState.id(channel)
            coEvery { getReward("reward")?.rewardConfiguration } returns mockk {
                every { title } returns "reward"
            }
        }
        val stateManager = StateManager(channelRewardsState)

        val activateReward = DeactivateReward.Companion.Builder(
            rewardName = "reward",
            channel = channel
        ).build(stateManager)

        activateReward.reward.rewardConfiguration.title shouldBe "reward"
        activateReward.channel.name shouldBe "channel"
    }

    should("description") {
        val builder = DeactivateReward.Companion.Builder("someReward", TwitchChannel("channelName"))

        val description = builder.description()

        description shouldBe OutgoingEventBuilderDescription(
            type = OutgoingEventType("twitch-deactivate-reward"),
            descriptors = listOf(
                StringFeatureDescriptor(
                    fieldName = "rewardName",
                    description = "Reward to deactivate",
                    value = "someReward"
                ),
                StringFeatureDescriptor(
                    fieldName = "channel",
                    description = "Channel owner of the reward",
                    value = "channelName"
                )
            )
        )
    }
})