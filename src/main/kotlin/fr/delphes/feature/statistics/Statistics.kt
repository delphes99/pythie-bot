package fr.delphes.feature.statistics

import fr.delphes.bot.command.Command
import fr.delphes.bot.command.CommandHandler
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.feature.HaveState

class Statistics(
    override val state: StatisticsState = StatisticsState()
) : AbstractFeature(), HaveState<StatisticsState> {
    private val statsCommand = CommandHandler(
        "!stats"
    ) { _, _ ->
        listOf(
            SendMessage(
                "Number of chatters : ${state.numberOfChatters}" +
                        "Number of messages : ${state.numberMessages}"
            )
        )
    }

    override val commands: Iterable<Command> = listOf(statsCommand)
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = listOf(MessageReceivedHandler())

    inner class MessageReceivedHandler : EventHandler<MessageReceived> {
        override fun handle(event: MessageReceived): List<OutgoingEvent> {
            state.addMessage(UserMessage(event.user, event.text))
            return emptyList()
        }
    }
}