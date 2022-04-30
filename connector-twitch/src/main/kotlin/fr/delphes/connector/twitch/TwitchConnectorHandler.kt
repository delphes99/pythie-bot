package fr.delphes.connector.twitch

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
import fr.delphes.connector.twitch.incomingEvent.VIPListReceived
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.exhaustive

//TODO State + Reducer
class TwitchConnectorHandler(
    private val connector: TwitchConnector,
) {
    suspend fun handle(event: TwitchIncomingEvent) {
        when (event) {
            is BitCheered -> {
                connector.statistics.of(event.channel).newCheer(event.cheerer, event.bitsUsed)
            }
            is ClipCreated -> Nothing
            is CommandAsked -> Nothing
            is IncomingRaid -> Nothing
            is MessageReceived -> {
                connector.statistics.of(event.channel).addMessage(UserMessage(event.user, event.text))
            }
            is NewFollow -> {
                connector.statistics.of(event.channel).newFollow(event.follower)
            }
            is NewSub -> {
                connector.statistics.of(event.channel).newSub(event.sub)
            }
            is RewardRedemption -> Nothing
            is StreamChanged -> Nothing
            is StreamOffline -> {
                connector.statistics.of(event.channel).changeCurrentStream(null)
            }
            is StreamOnline -> {
                connector.statistics.of(event.channel).changeCurrentStream(
                    Stream(
                        event.id,
                        event.title,
                        event.start,
                        event.game,
                        event.thumbnailUrl
                    )
                )
            }
            is VIPListReceived -> Nothing
            is NewPoll -> Nothing
            is PollUpdated -> Nothing
            is PollClosed -> Nothing
        }.exhaustive()
    }
}

private typealias Nothing = Unit