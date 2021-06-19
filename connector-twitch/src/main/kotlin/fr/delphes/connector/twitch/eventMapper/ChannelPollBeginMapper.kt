package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.NewPoll
import fr.delphes.connector.twitch.incomingEvent.Poll
import fr.delphes.connector.twitch.incomingEvent.PollChoice
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channelPoll.payload.ChannelPollBeginPayload

class ChannelPollBeginMapper : TwitchIncomingEventMapper<ChannelPollBeginPayload> {
    override suspend fun handle(
        twitchEvent: ChannelPollBeginPayload
    ): List<TwitchIncomingEvent> {
        val channel = TwitchChannel(twitchEvent.broadcaster_user_name)

        return listOf(
            NewPoll(
                channel,
                Poll(
                    twitchEvent.id,
                    twitchEvent.title,
                    twitchEvent.choices.map { choice -> PollChoice(choice.title) }
                )
            )
        )
    }
}