package fr.delphes.connector.twitch

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
import fr.delphes.twitch.api.streams.Stream
import fr.delphes.utils.exhaustive

//TODO State + Reducer
class TwitchConnectorHandler(
    private val connector: TwitchConnector
) {
    suspend fun handle(event: TwitchIncomingEvent) {
        when(event) {
            is BitCheered -> {
                connector.whenRunning {
                    clientBot.channelOf(event.channel)?.state?.newCheer(event.cheerer, event.bitsUsed)
                }
            }
            is ClipCreated -> Nothing
            is CommandAsked -> Nothing
            is IncomingRaid -> Nothing
            is MessageReceived -> Nothing
            is NewFollow -> {
                connector.whenRunning {
                    clientBot.channelOf(event.channel)?.state?.newFollow(event.follower)
                }
            }
            is NewSub -> {
                connector.whenRunning {
                    clientBot.channelOf(event.channel)?.state?.newSub(event.sub)
                }
            }
            is RewardRedemption -> Nothing
            is StreamChanged -> Nothing
            is StreamOffline -> {
                connector.whenRunning {
                    clientBot.channelOf(event.channel)?.state?.changeCurrentStream(null)
                }
            }
            is StreamOnline -> {
                connector.whenRunning {
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
            }
            is VIPListReceived -> Nothing
            is NewPoll -> Nothing
        }.exhaustive()
    }
}

private typealias Nothing = Unit