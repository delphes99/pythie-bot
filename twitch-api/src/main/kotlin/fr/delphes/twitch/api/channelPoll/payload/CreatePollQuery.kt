@file:UseSerializers(DurationToUnitSerializer::class)

package fr.delphes.twitch.api.channelPoll.payload

import fr.delphes.twitch.api.channelPoll.CreatePoll
import fr.delphes.utils.serialization.DurationToUnitSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.Duration

@Serializable
data class CreatePollQuery(
    val broadcaster_id: String,
    val title: String,
    val choices: List<CreatePollQueryChoice>,
    val channel_points_voting_enabled: Boolean,
    val channel_points_per_vote: Long,
    val duration: Duration,
) {
    companion object {
        fun of(createPoll: CreatePoll, userId: String): CreatePollQuery {
            return CreatePollQuery(
                userId,
                createPoll.title,
                createPoll.choices.map { CreatePollQueryChoice(it.title) },
                createPoll.channelPointVote != null,
                createPoll.channelPointVote?.cost ?: 0,
                createPoll.duration
            )
        }
    }
}