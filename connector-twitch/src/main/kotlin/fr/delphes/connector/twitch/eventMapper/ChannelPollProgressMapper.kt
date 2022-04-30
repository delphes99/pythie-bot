package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.Poll
import fr.delphes.connector.twitch.incomingEvent.PollChoice
import fr.delphes.connector.twitch.incomingEvent.PollUpdated
import fr.delphes.connector.twitch.incomingEvent.PollVote
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollProgressPayload

class ChannelPollProgressMapper : TwitchIncomingEventMapper<ChannelPollProgressPayload> {
    override suspend fun handle(
        twitchEvent: ChannelPollProgressPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            PollUpdated(
                channel,
                Poll(
                    twitchEvent.id,
                    twitchEvent.title,
                    twitchEvent.choices.map { choice -> PollChoice(choice.title) }
                ),
                twitchEvent
                    .choices.associate { choice ->
                        PollChoice(choice.title) to PollVote(
                            choice.votes.toLong(),
                            choice.channel_points_votes.toLong(),
                            choice.bits_votes.toLong()
                        )
                    }
            )
        )
    }
}