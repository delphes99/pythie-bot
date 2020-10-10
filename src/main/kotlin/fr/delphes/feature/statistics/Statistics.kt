package fr.delphes.feature.statistics

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HaveState

class Statistics(
    override val state: StatisticsState = StatisticsState()
) : AbstractFeature(), HaveState<StatisticsState> {
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = listOf(MessageReceivedHandler())

    inner class MessageReceivedHandler : EventHandler<MessageReceived> {
        override fun handle(event: MessageReceived): List<OutgoingEvent> {
            return if (event.text.toLowerCase() == "!stats") {
                listOf(SendMessage(
                    "Number of chatters : ${state.numberOfChatters}" +
                            "Number of messages : ${state.numberMessages}"
                ))
            } else {
                state.addMessage(UserMessage(event.user, event.text))
                emptyList()
            }
        }
    }
}