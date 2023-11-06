package fr.delphes.connector.twitch

import fr.delphes.bot.connector.ConnectorInternalIncomingEventHandler
import fr.delphes.bot.event.incoming.BotStarted
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.IncomingEventWrapper

class BotStartedInternalEventHandler(
    private val connector: TwitchConnector,
) : ConnectorInternalIncomingEventHandler {
    override suspend fun handleIncomingEvent(event: IncomingEventWrapper<out IncomingEvent>) {
        if (event.data is BotStarted) {
            connector.rewardService.synchronizeRewards()
        }
    }
}