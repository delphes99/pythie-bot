package fr.delphes.features.twitch.streamerHighlight

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.TwitchEventHandler
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.feature.Feature
import fr.delphes.twitch.TwitchChannel

class StreamerHighlightFeature(
    private val channel: TwitchChannel,
    private val response: (MessageReceived) -> List<OutgoingEvent>
) : Feature {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(MessageReceivedHandler())
    }

    inner class MessageReceivedHandler : TwitchEventHandler<MessageReceived>(channel) {
        override suspend fun handleIfGoodChannel(event: MessageReceived, bot: Bot): List<OutgoingEvent> {
            return bot.connector<TwitchConnector>()!!.whenRunning(
                whenRunning = {
                    val user = this.clientBot.userCache.getUser(event.user)
                    if(user.isStreamer()) {
                        response(event)
                    } else {
                        emptyList()
                    }
                },
                whenNotRunning = {
                    emptyList()
                }
            )
        }
    }
}