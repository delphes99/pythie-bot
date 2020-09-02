package fr.delphes.feature

import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.outgoing.OutgoingEvent

class DisplayChat : AbstractFeature() {
    override val messageReceivedHandlers = listOf(DisplayChatMessageReceivedHandler())

    inner class DisplayChatMessageReceivedHandler : EventHandler<MessageReceived> {
        override fun handle(event: MessageReceived): List<OutgoingEvent> {
            println(event.user + ": " + event.text)
            return emptyList()
        }
    }
}