@file:UseSerializers(DurationToUnitSerializer::class)

package fr.delphes.twitch.api.channelPoll

import fr.delphes.utils.serialization.DurationToUnitSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
data class CreatePoll(
    val title: String,
    val choices: List<PollChoice>,
    val duration: Duration,
    val channelPointVote: ChannelPointVote? = null
)