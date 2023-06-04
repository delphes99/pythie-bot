package fr.delphes.connector.twitch

import fr.delphes.bot.event.incoming.IncomingEventWrapper
import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.incomingEvent.BitCheered
import fr.delphes.connector.twitch.incomingEvent.ClipCreated
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.incomingEvent.IncomingRaid
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.incomingEvent.NewFollow
import fr.delphes.connector.twitch.incomingEvent.NewPoll
import fr.delphes.connector.twitch.incomingEvent.NewSub
import fr.delphes.connector.twitch.incomingEvent.PollClosed
import fr.delphes.connector.twitch.incomingEvent.PollUpdated
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.exhaustive

//TODO State + Reducer
class TwitchConnectorHandler(
    private val connector: TwitchConnector,
) {
    suspend fun handle(event: IncomingEventWrapper<TwitchIncomingEvent>) {
        if (!event.isReplay()) {
            when (val data = event.data) {
                is BitCheered -> {
                    connector.statistics.of(data.channel).newCheer(data.cheerer, data.bitsUsed)
                }

                is ClipCreated -> Nothing
                is CommandAsked -> {
                }

                is IncomingRaid -> Nothing
                is MessageReceived -> {
                    connector.statistics.of(data.channel).addMessage(UserMessage(data.user, data.text))
                }

                is NewFollow -> {
                    connector.statistics.of(data.channel).newFollow(data.follower)
                }

                is NewSub -> {
                    connector.statistics.of(data.channel).newSub(data.sub)
                }

                is RewardRedemption -> Nothing
                is StreamChanged -> Nothing
                is StreamOffline -> {
                    connector.statistics.of(data.channel).changeCurrentStream(null)
                }

                is StreamOnline -> {
                    connector.statistics.of(data.channel).changeCurrentStream(
                        Stream(
                            data.id,
                            data.title,
                            data.start,
                            data.game,
                            data.thumbnailUrl
                        )
                    )
                }

                is NewPoll -> Nothing
                is PollUpdated -> Nothing
                is PollClosed -> Nothing
            }.exhaustive()
        }
    }
}

private typealias Nothing = Unit