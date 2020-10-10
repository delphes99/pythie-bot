package fr.delphes.feature.statistics

import fr.delphes.bot.Channel
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.event.incoming.NewFollow
import fr.delphes.bot.event.incoming.NewSub
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HaveAdmin
import fr.delphes.feature.HaveState
import io.ktor.application.Application

class Statistics(
    override val state: StatisticsState = StatisticsState()
) : AbstractFeature(), HaveState<StatisticsState>, HaveAdmin {
    override val module: (Channel) -> Application.() -> Unit = { channel ->
        StatisticsModule(this, channel.name)
    }

    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = listOf(MessageReceivedHandler())
    override val newFollowHandlers: List<EventHandler<NewFollow>> = listOf(NewFollowHandler())
    override val newSubHandlers: List<EventHandler<NewSub>> = listOf(NewSubHandler())

    inner class NewFollowHandler : EventHandler<NewFollow> {
        override fun handle(event: NewFollow): List<OutgoingEvent> {
            state.newFollow(event.follower)
            return emptyList()
        }
    }

    inner class NewSubHandler : EventHandler<NewSub> {
        override fun handle(event: NewSub): List<OutgoingEvent> {
            state.newSub(event.sub)
            return emptyList()
        }
    }

    inner class MessageReceivedHandler : EventHandler<MessageReceived> {
        override fun handle(event: MessageReceived): List<OutgoingEvent> {
            state.addMessage(UserMessage(event.user, event.text))
            return emptyList()
        }
    }
}