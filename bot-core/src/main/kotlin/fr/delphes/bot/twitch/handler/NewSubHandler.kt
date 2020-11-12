package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.bot.webserver.payload.newSub.NewSubPayload

class NewSubHandler : TwitchIncomingEventHandler<NewSubPayload> {
    override fun handle(
        twitchEvent: NewSubPayload,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val incomingEvents = twitchEvent.data.map { d -> NewSub(d) }
        incomingEvents.forEach { newSub ->
            changeState.newSub(newSub.sub)
        }
        return incomingEvents
    }
}