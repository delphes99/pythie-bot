package fr.delphes.features.twitch.command

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.command.Command
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.feature.ExperimentalFeatureRuntime
import fr.delphes.twitch.TwitchChannel

class ExperimentalTwitchCommandRuntime(
    override val feature: ExperimentalTwitchCommand
) : ExperimentalFeatureRuntime {
    val command = Command(feature.trigger)

    override fun execute(incomingEvent: IncomingEvent): List<OutgoingEvent> {
        return if(incomingEvent is CommandAsked
            && incomingEvent.isFor(TwitchChannel(feature.channel))
            && incomingEvent.command == command) {
                feature.responses.map { it.build() }
        } else {
            emptyList()
        }
    }
}
