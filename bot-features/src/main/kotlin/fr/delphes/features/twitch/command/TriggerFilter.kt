package fr.delphes.features.twitch.command

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.feature.featureNew.IncomingEventFilter

class TriggerFilter(private val command: Command) : IncomingEventFilter<NewTwitchCommandState> {
    override fun isApplicable(event: IncomingEvent, state: NewTwitchCommandState): Boolean {
        return event is CommandAsked && event.command == command
    }
}