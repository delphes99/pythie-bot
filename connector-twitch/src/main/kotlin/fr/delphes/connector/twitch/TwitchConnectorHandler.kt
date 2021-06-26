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
import fr.delphes.connector.twitch.incomingEvent.RewardRedemption
import fr.delphes.connector.twitch.incomingEvent.StreamChanged
import fr.delphes.connector.twitch.incomingEvent.StreamOffline
import fr.delphes.connector.twitch.incomingEvent.StreamOnline
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.incomingEvent.VIPListReceived
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.connector.twitch.state.action.StreamChangeAction
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.exhaustive
import fr.delphes.utils.store.ActionDispatcher

//TODO State + Reducer
class TwitchConnectorHandler(
    private val connector: TwitchConnector,
    private val actionDispatcher: ActionDispatcher = connector.bot
) {
    suspend fun handle(event: TwitchIncomingEvent) {
        connector.whenRunning {
            when (event) {
                is BitCheered -> {
                    clientBot.channelOf(event.channel)?.state?.newCheer(event.cheerer, event.bitsUsed)
                }
                is ClipCreated -> Nothing
                is CommandAsked -> Nothing
                is IncomingRaid -> Nothing
                is MessageReceived -> {
                    actionDispatcher.applyAction(MessageReceivedAction(event.channel, event.user, event.text))
                }
                is NewFollow -> {
                    clientBot.channelOf(event.channel)?.state?.newFollow(event.follower)
                }
                is NewSub -> {
                    clientBot.channelOf(event.channel)?.state?.newSub(event.sub)
                }
                is RewardRedemption -> Nothing
                is StreamChanged -> {
                    actionDispatcher.applyAction(StreamChangeAction(event.channel, event.changes))
                }
                is StreamOffline -> {
                    clientBot.channelOf(event.channel)?.state?.changeCurrentStream(null)
                }
                is StreamOnline -> {
                    clientBot.channelOf(event.channel)?.state?.changeCurrentStream(
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
            }.exhaustive()
        }
    }
}

private typealias Nothing = Unit